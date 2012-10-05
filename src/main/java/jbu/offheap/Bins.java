package jbu.offheap;

import jbu.exception.InvalidParameterException;

import static jbu.Primitive.*;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

abstract class Bins {

    protected static final int FREE = 0;
    protected static final int USED = 1;

    // Chunk real size
    final int finalChunkSize;
    // Userdata chunk size
    final int chunkSize;
    // Base addr of bin
    final int baseAddr;

    final int size;

    final AtomicInteger occupation = new AtomicInteger(0);

    /**
     * Status of memory chunk (allocated, free)
     */
    final AtomicIntegerArray chunks;

    // Helper for find free chunk
    final AtomicInteger chunkOffset = new AtomicInteger(0);

    protected Bins(int initialChunkNumber, int chunkSize, int baseAddr) {
        this.size = initialChunkNumber;
        this.chunkSize = chunkSize;
        this.baseAddr = baseAddr;

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
        this.finalChunkSize = chunkSize + INT_LENGTH + LONG_LENGTH;
        this.chunks = new AtomicIntegerArray(initialChunkNumber);
    }

    long allocateOneChunk() {
        // Check if they are some chunk free
        if (occupation.get() < size) {
            int currentChunkOffset = this.chunkOffset.get();
            for (int i = 0; i < this.size; i++) {
                int currentChunkIndex = (i + currentChunkOffset) % this.size;
                if (chunks.compareAndSet(currentChunkIndex, FREE, USED)) {
                    this.chunkOffset.set(currentChunkOffset + i + 1);
                    occupation.incrementAndGet();
                    return AddrAlign.constructAddr(baseAddr, currentChunkIndex);
                }
            }
        }
        // Cannot allocate one chunk
        return -1;
    }

    long[] allocateNChunk(int n) {
        // Check parameter
        if (n <= 0) {
            return null;
        }
        long[] res = new long[n];
        int nbChunckAllocated = 0;
        // Search for n free chunk
        int currentChunkOffet = this.chunkOffset.get();
        for (int i = 0; i < this.size; i++) {
            int currentChunkIndex = (i + currentChunkOffet) % this.size;
            if (chunks.compareAndSet(currentChunkIndex, FREE, USED)) {
                res[nbChunckAllocated] = AddrAlign.constructAddr(baseAddr, currentChunkIndex);
            } else {
                continue;
            }
            if (++nbChunckAllocated == n) {
                this.chunkOffset.set(currentChunkIndex + 1);
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
        this.chunkOffset.set(AddrAlign.getChunkId(chunks[0]));
    }

    abstract void setNextChunkId(int currentChunkId, long nextChunkId);

    abstract long getNextChunkId(int currentChunkId);

    abstract boolean storeInChunk(int chunkId, byte[] data, int currentOffset, int length);

    abstract boolean storeInChunk(int currentChunkId, ByteBuffer data);

    abstract byte[] loadFromChunk(int chunkId);

    long findOffsetForChunkId(int chunkId) {
        return (long)chunkId * (long)finalChunkSize;
    }

    public int getAllocatedChunks() {
        return occupation.intValue();
    }

    public int getUsedSize() {
        return occupation.intValue() * finalChunkSize;
    }
}
