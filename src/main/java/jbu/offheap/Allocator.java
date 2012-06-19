package jbu.offheap;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Frontend of off-heap store
 */
public class Allocator {

    private final Map<Integer, Bins> binsByAddr = new HashMap<Integer, Bins>();
    private final TreeMap<Integer, Bins> binsBySize = new TreeMap<Integer, Bins>();

    private final AtomicInteger allocatedMemory = new AtomicInteger(0);
    private volatile int usedMemory = 0;
    private final int maxMemory;

    public Allocator(int maxMemory, boolean unsafe) {
        constructWithLogScale(maxMemory, 10, unsafe);
        this.maxMemory = maxMemory;
    }

    private void constructWithLogScale(int initialMemory, int maxBins, boolean unsafe) {
        // Construct scale
        // Always start from 128 Bytes
        // FIXME... I don't think size distribution must be equal
        int binsSize = initialMemory / maxBins;
        int currentChunkSize = 128;
        for (int i = 0; i < maxBins; i++) {
            Bins bbb;
            if (unsafe) {
                bbb = new UnsafeBins(binsSize / currentChunkSize, currentChunkSize, i);
            } else {
                bbb = new ByteBufferBins(binsSize / currentChunkSize, currentChunkSize, i);
            }
            binsBySize.put(currentChunkSize, bbb);
            binsByAddr.put(i, bbb);
            currentChunkSize = currentChunkSize * 2;
        }
    }

    // Allocate with less waste but much more chunks
    public long alloc2(int memorySize) {
        int usedMemoryByAllocate = 0;
        int memoryToAllocate = memorySize;
        long previousChunkAddr = -1;
        long firstChunk = -1;
        // Search for the bin with just size lesser
        while (memoryToAllocate > 0) {
            Map.Entry<Integer, Bins> usedBin;
            // Search upper and lesser
            Map.Entry<Integer, Bins> lesserBin = binsBySize.floorEntry(memoryToAllocate);
            Map.Entry<Integer, Bins> upperBin = binsBySize.ceilingEntry(memoryToAllocate);
            // Fill factor determine when stop cutting in two memory to allocate
            if (lesserBin == null || (((double) memorySize / (double) lesserBin.getKey()) > 0.75d && upperBin != null)) {
                // take upper
                usedBin = upperBin;
            } else {
                usedBin = lesserBin;
            }
            // Allocate one chunk
            long chunkAddr = usedBin.getValue().allocateOneChunk();

            // If no chunk available
            while (chunkAddr < 0) {
                // take just lower bins and try to allocated
                usedBin = binsBySize.lowerEntry(usedBin.getKey());
                chunkAddr = usedBin.getValue().allocateOneChunk();
            }

            // update next chunk of previous element
            if (previousChunkAddr > 0) {
                setNextChunk(previousChunkAddr, chunkAddr);
            } else {
                // First chunk
                firstChunk = chunkAddr;
            }
            previousChunkAddr = chunkAddr;
            // update memory to allocate
            memoryToAllocate -= usedBin.getKey();
            // update used memory
            usedMemoryByAllocate += usedBin.getValue().finalChunkSize;
        }
        // Set no next chunk to last chunk
        setNextChunk(previousChunkAddr, -1);
        this.usedMemory += usedMemoryByAllocate;
        return firstChunk;
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

        Bins bin = binsBySize.get(upperKey);
        int nbChunck = memorySize / upperKey;
        int endSize = memorySize % upperKey;
        long lastchunk = -1;
        // Manage efficiency vs group chunk for less fragment
        if (nbChunck != 0 && endSize > 0 && endSize <= upperKey / 2) {
            // In this case choose efficiency and store end in another bin
            Bins endBin = binsBySize.ceilingEntry(endSize).getValue();
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
        usedMemory += bin.finalChunkSize * nbChunck;
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
            Bins bin = getBinFromAddr(currentAdr);
            nextAdr = bin.getNextChunkId(AddrAlign.getChunkId(currentAdr));
            // and free current
            bin.freeChunk(currentAdr);
            currentAdr = nextAdr;
            // update counter
            usedMemory -= bin.finalChunkSize;
        } while (nextAdr != -1);
    }


    public void store(long firstChunkAdr, byte[] data) {
        // store in first chunk all data that's I can store
        long currentChunkAdr = firstChunkAdr;
        int currentOffset = 0;
        while (currentOffset < data.length) {
            int currentChunkId = AddrAlign.getChunkId(currentChunkAdr);
            // Check if currentChunkId = -1 Error
            if (currentChunkAdr == -1) {
                throw new BufferOverflowException("Data is too large");
            }
            Bins bin = getBinFromAddr(currentChunkAdr);
            int chunkSize = bin.chunkSize;
            bin.storeInChunk(currentChunkId, data, currentOffset,
                    (data.length - currentOffset > chunkSize) ? chunkSize : data.length - currentOffset);
            currentOffset += chunkSize;
            currentChunkAdr = bin.getNextChunkId(currentChunkId);
        }
    }

    public void store(long firstChunkAdr, ByteBuffer data) {
        // store in first chunk all data that's I can store
        // The byte buffer must be ready for reading...
        // Store only from position to limit
        long currentChunkAdr = firstChunkAdr;
        while (data.remaining() > 0) {
            int currentChunkId = AddrAlign.getChunkId(currentChunkAdr);
            // Check if currentChunkId = -1 Error
            if (currentChunkAdr == -1) {
                throw new BufferOverflowException("Data is too large");
            }
            Bins bin = getBinFromAddr(currentChunkAdr);
            int chunkSize = bin.chunkSize;
            bin.storeInChunk(currentChunkId, data);
            currentChunkAdr = bin.getNextChunkId(currentChunkId);
        }
    }

    public byte[] load(long firstChunkAdr) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte[] tmpB;
        long currentChunkAdr = firstChunkAdr;
        // Get data of current chunk
        while (currentChunkAdr != -1) {
            int currentChunkId = AddrAlign.getChunkId(currentChunkAdr);
            Bins bin = getBinFromAddr(currentChunkAdr);
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

    private Bins getBinFromAddr(long chunkAddr) {
        return binsByAddr.get(AddrAlign.getBinId(chunkAddr));
    }
}
