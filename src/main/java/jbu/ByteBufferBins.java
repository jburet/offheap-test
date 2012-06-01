package jbu;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * TODO
 * Manage thread safety
 * Check ByteBuffer with index API are threadsafe ?
 */

/**
 * Some memory not allocated in heap, storing byte[] in fixed size chunk and keeping status of chunk (used, free)
 * This bins use DirectbyteBuffer for accessing memory
 */
public class ByteBufferBins {

    private static final int FREE = 0;
    private static final int USED = 1;

    /**
     * Bytebuffer allocated for storing all page
     */
    private ByteBuffer bb;

    // Chunk real size
    final int finalChunkSize;
    // Userdata chunk size
    final int chunkSize;
    // Base addr of bin
    final int baseAddr;

    // Helper for find free chunk
    int chunkOffset = 0;

    /**
     * Status of memory chunk (allocated, free)
     */
    final AtomicIntegerArray chunks;
    final AtomicInteger occupation = new AtomicInteger(0);
    final int size;

    ByteBufferBins(int initialChunkNumber, int chunkSize, int baseAddr) {
        this.size = initialChunkNumber;
        if (initialChunkNumber <= 0) {
            // Throw exception
            throw new InvalidParameterException("InitialChunkNumber must be > 0 ");
        }
        if (chunkSize <= 0) {
            // Throw exception
            throw new InvalidParameterException("chunkSize must be > 0 ");
        }
        // In a chunk we also store chunck size in int. Adding 4 byte
        // And addr of next chunk. Adding 8 byte
        // FIXME can be optimised. Using less bytes if chunksize < 256 or < 65000
        this.finalChunkSize = chunkSize + 4 + 8;
        this.chunkSize = chunkSize;
        this.baseAddr = baseAddr;

        // FIXME Cannot allocate more than Integer.MAX_VALUE. Check this
        this.bb = ByteBuffer.allocateDirect(initialChunkNumber * finalChunkSize);
        this.chunks = new AtomicIntegerArray(initialChunkNumber);
    }

    /**
     * Storing data in chunkId
     * Return true if data can be stored in this chunk
     *
     * @param chunkId
     * @param data
     * @param currentOffset
     * @param length        @return
     */
    boolean storeInChunk(int chunkId, byte[] data, int currentOffset, int length) {
        // check size for avoid overflow on other chunk
        if (length > this.chunkSize) {
            throw new BufferOverflowException("Try to store too many data. Store "
                    + data.length + " in " + this.chunkSize);
        }
        int baseAddr = findOffsetForChunkId(chunkId);
        bb.putInt(baseAddr, length);
        for (int i = 0; i < length; i++) {
            bb.put(baseAddr + 4 + i, data[i + currentOffset]);
        }
        return true;
    }

    /**
     * Return data stored in this chunk
     *
     * @param chunkId
     * @return
     */
    byte[] loadFromChunk(int chunkId) {
        int baseAddr = findOffsetForChunkId(chunkId);
        int size = bb.getInt(baseAddr);
        byte[] data = new byte[size];
        for (int i = 0; i < size; i++) {
            data[i] = bb.get(baseAddr + 4 + i);
        }
        return data;
    }

    /**
     * Allocate n chunck.
     * Return null if cannot allocate
     *
     * @param n
     * @return
     */
    long[] allocateNChunk(int n) {
        // Check parameter
        if (n <= 0) {
            return null;
        }
        long[] res = new long[n];
        int nbChunckAllocated = 0;
        // Search for n free chunk
        for (int i = 0; i < this.size; i++) {
            int currentChunkIndex = (i + chunkOffset) % this.size;
            if (chunks.compareAndSet(currentChunkIndex, FREE, USED)) {
                res[nbChunckAllocated] = AddrAlign.constructAddr(baseAddr, currentChunkIndex);
            } else {
                continue;
            }
            if (++nbChunckAllocated == n) {
                chunkOffset += nbChunckAllocated;
                occupation.getAndAdd(nbChunckAllocated);
                return res;
            }
        }
        // Not enough chunk. Unallocate
        for (int i = 0; i < nbChunckAllocated; i++) {
            chunks.compareAndSet(AddrAlign.getChunkId(res[i]), USED, FREE);
        }
        occupation.getAndAdd(-nbChunckAllocated);
        return null;
    }

    /**
     * Free previously allocatedChunk
     *
     * @param chunks
     */
    void freeChunk(long... chunks) {
        for (long chunkAdr : chunks) {
            this.chunks.set(AddrAlign.getChunkId(chunkAdr), FREE);
            occupation.decrementAndGet();
        }
    }

    void setNextChunkId(int currentChunkId, long nextChunkId) {
        // Set nextChunkId to last 8 bytes of currentChunkId
        int nextChunkOffset = findOffsetForChunkId(currentChunkId) + chunkSize + 4;
        bb.putLong(nextChunkOffset, nextChunkId);
    }

    long getNextChunkId(int currentChunkId) {
        // Set nextChunkId to last 8 bytes of currentChunkId
        int nextChunkOffset = findOffsetForChunkId(currentChunkId) + chunkSize + 4;
        return bb.getLong(nextChunkOffset);
    }

    private int findOffsetForChunkId(int chunkId) {
        return chunkId * finalChunkSize;
    }


}
