package jbu;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Frontend of off-heap store
 */
public class Allocator {

    private final Map<Integer, ByteBufferBins> binsByAddr = new HashMap<Integer, ByteBufferBins>();
    private final TreeMap<Integer, ByteBufferBins> binsBySize = new TreeMap<Integer, ByteBufferBins>();

    private final AtomicInteger allocatedMemory = new AtomicInteger(0);
    private final AtomicInteger usedMemory = new AtomicInteger(0);

    public Allocator(int maxMemory) {
        constructWithLogScale(maxMemory, 10);
    }

    private void constructWithLogScale(int maxMemory, int maxBins) {
        // Construct scale
        // Always start from 128 Bytes
        // FIXME... I don't think size distribution must be equal
        int binsSize = maxMemory / maxBins;
        int currentChunkSize = 128;
        for (int i = 0; i < maxBins; i++) {
            ByteBufferBins bbb = new ByteBufferBins(binsSize / currentChunkSize, currentChunkSize, i);
            binsBySize.put(currentChunkSize, bbb);
            binsByAddr.put(i, bbb);
            currentChunkSize = currentChunkSize * 2;
        }
    }

    public long alloc(int memorySize) {
        // Search for the bin with size just upper
        Integer upperKey = binsBySize.ceilingKey(memorySize);
        // upperKey can be null... In this case take the upper's key
        if (upperKey == null) {
            upperKey = binsBySize.lastKey();
        } else if (memorySize < (double) upperKey * 0.75d) {
            Integer floorKey = binsBySize.floorKey(memorySize);
            if (floorKey != null) {
                // use floor key
                upperKey = floorKey;
            }
        }
        // Get the bin

        ByteBufferBins bin = binsBySize.get(upperKey);
        int nbChunck = memorySize / upperKey;
        int endSize = memorySize % upperKey;
        long lastchunk = -1;
        // Manage efficiency vs group chunk for less fragment
        if (nbChunck != 0 && endSize > 0 && endSize <= upperKey / 2) {
            // In this case choose efficiency and store end in another bin
            ByteBufferBins endBin = binsBySize.ceilingEntry(endSize).getValue();
            lastchunk = endBin.allocateNChunk(1)[0];
        } else {
            nbChunck++;
        }
        long[] chunksAddrs = bin.allocateNChunk(nbChunck);
        if (chunksAddrs == null) {
            // Memory cannot be allocated
            return -1;
        }
        // Create chunk linkedlist
        long currentChunkAdr = -1;
        for (long chunkAddr : chunksAddrs) {
            if (currentChunkAdr >= 0) {
                // set nextchunk to currentChunkId
                setNextChunk(currentChunkAdr, chunkAddr);
            }
            currentChunkAdr = chunkAddr;
        }
        if (lastchunk >= 0) {
            setNextChunk(currentChunkAdr, lastchunk);
            currentChunkAdr = lastchunk;
        }
        // On the last chunk put -1 as next chunk reference
        setNextChunk(currentChunkAdr, -1);

        // update counter
        usedMemory.getAndAdd(bin.finalChunkSize * nbChunck);
        // Return first chunk addr
        return chunksAddrs[0];
    }

    public void free(long firstChunkAdr) {
        // get all chunk ref
        // FIXME find bin
        // Get next adr
        long currentAdr = firstChunkAdr;
        long nextAdr;
        do {
            ByteBufferBins bin = getBinFromAddr(currentAdr);
            nextAdr = bin.getNextChunkId(AddrAlign.getChunkId(currentAdr));
            // and free current
            bin.freeChunk(currentAdr);
            currentAdr = nextAdr;
            // update counter
            usedMemory.getAndAdd(-bin.finalChunkSize);
        } while (nextAdr != -1);
    }


    public void store(long firstChunkAdr, byte[] data) {
        // store in first chunk all data that's I can store
        // FIXME get bin
        // FIXME use size of bin
        long currentChunkAdr = firstChunkAdr;
        int currentOffset = 0;
        while (currentOffset < data.length) {
            int currentChunkId = AddrAlign.getChunkId(currentChunkAdr);
            // Check if currentChunkId = -1 Error
            if (currentChunkAdr == -1) {
                throw new BufferOverflowException("Data is too large");
            }
            ByteBufferBins bin = getBinFromAddr(currentChunkAdr);
            int chunkSize = bin.chunkSize;
            bin.storeInChunk(currentChunkId, data, currentOffset,
                    (data.length - currentOffset > chunkSize) ? chunkSize : data.length - currentOffset);
            currentOffset += chunkSize;
            currentChunkAdr = bin.getNextChunkId(currentChunkId);
        }
    }

    public byte[] load(long firstChunkAdr) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        // FIXME get bin
        byte[] tmpB;
        long currentChunkAdr = firstChunkAdr;
        // Get data of current chunk
        while (currentChunkAdr != -1) {
            int currentChunkId = AddrAlign.getChunkId(currentChunkAdr);
            ByteBufferBins bin = getBinFromAddr(currentChunkAdr);
            tmpB = bin.loadFromChunk(currentChunkId);
            bout.write(tmpB, 0, tmpB.length);
            // get next chunk
            currentChunkAdr = bin.getNextChunkId(currentChunkId);
        }
        return bout.toByteArray();
    }

    private void setNextChunk(long currentChunkAdr, long nextChunkAddr) {
        // FIXME find bin
        getBinFromAddr(currentChunkAdr).setNextChunkId(AddrAlign.getChunkId(currentChunkAdr), nextChunkAddr);
    }

    private ByteBufferBins getBinFromAddr(long chunkAddr) {
        return binsByAddr.get(AddrAlign.getBinId(chunkAddr));
    }
}
