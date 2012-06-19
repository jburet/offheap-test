package jbu.offheap;

import javax.management.*;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Frontend of off-heap store
 */
public class Allocator implements AllocatorMBean {

    //Thread safe until cannot be modified at runtime
    private final Map<Integer, Bins> binsByAddr = new HashMap<Integer, Bins>();
    //Thread safe until cannot be modified at runtime
    private final TreeMap<Integer, Bins> binsBySize = new TreeMap<Integer, Bins>();

    private final AtomicInteger allocatedMemory = new AtomicInteger(0);
    private final AtomicInteger usedMemory = new AtomicInteger(0);
    private final AtomicLong nbAllocation = new AtomicLong(0);
    private final AtomicLong nbFree = new AtomicLong(0);


    public Allocator(int maxMemory, boolean unsafe) {
        constructWithLogScale(maxMemory, 16, unsafe);
    }

    private void constructWithLogScale(int initialMemory, int maxBins, boolean unsafe) {
        // Construct scale
        // Always start from 128 Bytes

        int binsSize = initialMemory / maxBins;
        int currentChunkSize = 32;

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
    public long alloc(int memorySize) {
        int usedMemoryByAllocate = 0;
        int memoryToAllocate = memorySize;
        long previousChunkAddr = -1;
        long firstChunk = -1;
        int nbAllocateChunk = 0;
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

            nbAllocateChunk++;
        }
        // Set no next chunk to last chunk
        setNextChunk(previousChunkAddr, -1);
        this.usedMemory.getAndAdd(usedMemoryByAllocate);
        this.nbAllocation.getAndAdd(nbAllocateChunk);
        return firstChunk;
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
            usedMemory.getAndAdd(-bin.finalChunkSize);
            nbFree.incrementAndGet();
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

    public void registerInMBeanServer(MBeanServer mbs) {
        try {
            mbs.registerMBean(this, new ObjectName("Allocator:name=allocator"));
            for (Bins bbb : binsBySize.values()) {
                if (bbb instanceof ByteBufferBins) {
                    mbs.registerMBean(bbb, new ObjectName("Allocator.ByteBufferBins:maxChunk=" + bbb.chunkSize));
                }else if (bbb instanceof UnsafeBins) {
                    mbs.registerMBean(bbb, new ObjectName("Allocator.UnsafeBins:maxChunk=" + bbb.chunkSize));
                }
            }
        } catch (InstanceAlreadyExistsException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MBeanRegistrationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NotCompliantMBeanException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public void unRegisterInMBeanServer(MBeanServer mbs) {
        try {
            mbs.unregisterMBean(new ObjectName("Allocator:name=allocator"));
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MBeanRegistrationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public int getAllocatedMemory() {
        return allocatedMemory.intValue();
    }

    @Override
    public int getUsedMemory() {
        return usedMemory.intValue();
    }

    @Override
    public long getNbAllocation() {
        return nbAllocation.longValue();
    }

    @Override
    public long getNbFree() {
        return nbFree.longValue();
    }
}
