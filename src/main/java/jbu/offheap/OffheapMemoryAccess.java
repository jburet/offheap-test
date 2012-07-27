package jbu.offheap;

import jbu.exception.BufferOverflowException;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class OffheapMemoryAccess {


    private final Allocator allocator;

    public OffheapMemoryAccess(Allocator allocator) {
        this.allocator = allocator;
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
            Bins bin = allocator.getBinFromAddr(currentChunkAdr);
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
            Bins bin = allocator.getBinFromAddr(currentChunkAdr);
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
            Bins bin = allocator.getBinFromAddr(currentChunkAdr);
            tmpB = bin.loadFromChunk(currentChunkId);
            bout.write(tmpB, 0, tmpB.length);
            // get next chunk
            currentChunkAdr = bin.getNextChunkId(currentChunkId);
        }
        return bout.toByteArray();
    }

}
