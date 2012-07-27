package jbu.offheap;
import static jbu.Primitive.*;
import static jbu.UnsafeUtil.unsafe;

public class StoreContext {

    private long firstChunkAdr;
    private long currentBaseAdr;
    private int currentOffset;
    private int remaining;
    private final Allocator allocator;

    StoreContext(Allocator allocator, long firstChunkAdr) {
        this.firstChunkAdr = firstChunkAdr;
        this.allocator = allocator;
        beginNewChunk(firstChunkAdr);
    }

    public void reuse() {
        beginNewChunk(this.firstChunkAdr);
    }

    public void storeInt(int value) {
        unsafe.putInt(this.currentBaseAdr + this.currentOffset, value);
        // FIXME 4 (int length) must be constant
        this.currentOffset += INT_LENGTH;
        this.remaining -= INT_LENGTH;
    }

    public void storeSomething(Object object, final long offset, final int length) {
        int byteRemaining = length;
        do {
            int byteToCopy = (remaining > byteRemaining) ? byteRemaining : remaining;
            unsafe.copyMemory(object, offset + (length - byteRemaining), null, this.currentBaseAdr + this.currentOffset, byteToCopy);
            byteRemaining -= byteToCopy;
            this.currentOffset += byteToCopy;
            this.remaining -= byteToCopy;
            // If remaining in currentChunk == 0 load next chunk
            if (this.remaining == 0) {
                // Get next chunk address in last 4 byte
                beginNewChunk(unsafe.getLong(this.currentBaseAdr + this.currentOffset));
            }
        } while (byteRemaining > 0);
    }

    private void beginNewChunk(long chunkAdr) {
        // get bins
        // FIXME Suport only unsafebin
        // Get baseAdr of allocated memory
        // Get baseOffset of chunk
        // And store this in currentBaseAdr
        UnsafeBins b = (UnsafeBins) allocator.getBinFromAddr(chunkAdr);
        this.currentBaseAdr = b.binAddr + b.findOffsetForChunkId(AddrAlign.getChunkId(chunkAdr));
        this.currentOffset = 0;
        this.remaining = b.chunkSize;
        // put the size of data in 4 first byte
        // FIXME with store context always use full size
        unsafe.putInt(this.currentBaseAdr + this.currentOffset, this.remaining);
        this.currentOffset += INT_LENGTH;
    }
}