package jbu.offheap;

import jbu.serializer.Type;
import sun.misc.Unsafe;

import java.util.concurrent.ArrayBlockingQueue;

public class LoadContext {

    private static final Unsafe unsafe = UnsafeUtil.unsafe;

    private long currentChunkAdr;
    private long firstChunkAdr;
    private long currentBaseAdr;
    private int currentOffset;
    private int remaining;
    private final Allocator allocator;

    LoadContext(Allocator allocator, long firstChunkAdr) {
        this.firstChunkAdr = firstChunkAdr;
        this.allocator = allocator;
        beginNewChunk(firstChunkAdr);
    }


    public void reset() {
        beginNewChunk(this.firstChunkAdr);
    }

    public int loadInt() {
        int res = unsafe.getInt(this.currentBaseAdr + this.currentOffset);
        // FIXME 4 (int length) must be constant
        this.currentOffset += 4;
        this.remaining -= 4;
        return res;
    }

    // NOT USE IT for another thing than array .... NYI (Not yet implemented)
    // Can be used for array...
    public void loadArray(Object dest, long offset, int byteRemaining) {
        int copiedBytes = 0;
        do {
            int byteToCopy = (byteRemaining > remaining) ? remaining : byteRemaining;
            //int d = unsafe.getInt(this.currentBaseAdr + this.currentOffset);
            // FIXME WHY DIRECT MEMORY DON'T WORK !!!!!!!!!! (throw illegalArgument)
            // FIXME Marked as NotYetImplemented in code work only if destination are primitive array
            // See http://mail.openjdk.java.net/pipermail/hotspot-runtime-dev/2012-March/003322.html
            unsafe.copyMemory(null, this.currentBaseAdr + this.currentOffset, dest, offset + copiedBytes, byteToCopy);
            copiedBytes += byteToCopy;
            //unsafe.putInt(object, offset, d);
            byteRemaining -= byteToCopy;
            this.currentOffset += byteToCopy;
            this.remaining -= byteToCopy;
            if (this.remaining == 0) {
                // Get next chunk address in last 4 byte
                beginNewChunk(unsafe.getLong(this.currentBaseAdr + this.currentOffset));
            }
        } while (byteRemaining > 0);
    }

    // loadArray has a better impl, but working only when jdk7 are really fully implemented...
    // Workaround for copy memory
    // Allocate tmpbuffer in offheap .. and read with typed method of unsafe
    public void loadPrimitive(Object dest, long destOffset, int totalByteRemaining) {

        // Use tmp memory only if really needed
        do {
            int byteToCopy = (totalByteRemaining > remaining) ? remaining : totalByteRemaining;
            int byteRemaining = byteToCopy;

            // primitive take 8,4,2 or 1 byte
            // Take the greatest size under totalByteRemaining
            if (byteRemaining / 8 == 1) {
                // copy 8 byte
                long b = unsafe.getLong(null, this.currentBaseAdr + this.currentOffset);
                unsafe.putLong(dest, destOffset, b);
                byteRemaining -= 8;
            }
            if (byteRemaining / 4 == 1) {
                // copy 4 byte
                int b = unsafe.getInt(null, this.currentBaseAdr + this.currentOffset);
                unsafe.putInt(dest, destOffset, b);
                byteRemaining -= 4;
            }
            if (byteRemaining / 2 == 1) {
                // copy 2 byte
                short b = unsafe.getShort(null, this.currentBaseAdr + this.currentOffset);
                unsafe.putShort(dest, destOffset, b);
                byteRemaining -= 2;
            }
            if (byteRemaining / 1 == 1) {
                // copy 1 byte
                byte b = unsafe.getByte(null, this.currentBaseAdr + this.currentOffset);
                unsafe.putByte(dest, destOffset, b);
                byteRemaining -= 1;
            }
            totalByteRemaining -= byteToCopy;
            this.remaining -= byteToCopy;
            this.currentOffset += byteToCopy;
            // If all chunk loaded take a new one
            if (this.remaining == 0) {
                // Get next chunk address in last 4 byte
                beginNewChunk(unsafe.getLong(this.currentBaseAdr + this.currentOffset));
            }
        } while (totalByteRemaining > 0);
    }


    private void beginNewChunk(long chunkAdr) {
        this.currentChunkAdr = chunkAdr;
        // get bins
        // FIXME Suport only unsafebin
        // Get baseAdr of allocated memory
        // Get baseOffset of chunk
        // And store this in currentBaseAdr
        UnsafeBins b = (UnsafeBins) allocator.getBinFromAddr(chunkAdr);
        this.currentBaseAdr = b.binAddr + b.findOffsetForChunkId(AddrAlign.getChunkId(chunkAdr));
        // Put the offset to 4... Don't read chunk size. Always same value as chunk size
        // FIXME Use constant
        this.currentOffset = 4;
        this.remaining = b.chunkSize;

    }

}