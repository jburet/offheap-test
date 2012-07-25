package jbu.offheap;

import jbu.serializer.Type;
import sun.misc.Unsafe;

import java.util.concurrent.ArrayBlockingQueue;

public class LoadContext {

    private static final Unsafe unsafe = UnsafeUtil.unsafe;

    // Allocate buffer for each load is very expensive... Allocate only one and reuse
    // Allocate a new buffer for each LoadContext allocation is also too expensive (limit to 250000 load/s)
    // Pooling allocated memory is not a good idea... limit(150000 load/s)
    private long tmpAddr = unsafe.allocateMemory(8);

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

    // Release allocated offheap memory during GC
    @Override
    protected void finalize() throws Throwable {
        unsafe.freeMemory(tmpAddr);
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
    public void loadSomething2(Object dest, long offset, int byteRemaining) {
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

    // loadSomething2 has a better impl, but working only when jdk7 are really fully implemented...
    // Workaround for copy memory
    // Allocate tmpbuffer in offheap .. and read with typed method of unsafe
    public void loadSomething(Object dest, long destOffset, Type type, int byteRemaining) {

        int offset = 0;
        long byteToCopyAddr = this.tmpAddr;
        // Use tmp memory only if really needed
        if (byteRemaining > remaining) {
            do {
                int byteToCopy = (byteRemaining > remaining) ? remaining : byteRemaining;
                unsafe.copyMemory(this.currentBaseAdr + this.currentOffset, byteToCopyAddr + offset, byteToCopy);
                byteRemaining -= byteToCopy;

                if (this.remaining == 0) {
                    // Get next chunk address in last 4 byte
                    beginNewChunk(unsafe.getLong(this.currentBaseAdr + this.currentOffset));
                }
                offset += byteToCopy;
            } while (byteRemaining > 0);

        } else {
            // Tmp addr not really needed
            byteToCopyAddr = this.currentBaseAdr + this.currentOffset;
            this.currentOffset += byteRemaining;
            this.remaining -= byteRemaining;
        }

        if (!type.isArray) {
            if (type.equals(Type.BOOLEAN)) {
                boolean b = unsafe.getBoolean(null, byteToCopyAddr);
                unsafe.putBoolean(dest, destOffset, b);
            }
            if (type.equals(Type.CHAR)) {
                char c = unsafe.getChar(byteToCopyAddr);
                unsafe.putChar(dest, destOffset, c);
            }
            if (type.equals(Type.BYTE)) {
                byte b = unsafe.getByte(byteToCopyAddr);
                unsafe.putByte(dest, destOffset, b);
            }
            if (type.equals(Type.SHORT)) {
                short s = unsafe.getShort(byteToCopyAddr);
                unsafe.putShort(dest, destOffset, s);
            }
            if (type.equals(Type.INT)) {
                int i = unsafe.getInt(byteToCopyAddr);
                unsafe.putInt(dest, destOffset, i);
            }
            if (type.equals(Type.LONG)) {
                long l = unsafe.getLong(byteToCopyAddr);
                unsafe.putLong(dest, destOffset, l);
            }
            if (type.equals(Type.FLOAT)) {
                float f = unsafe.getFloat(byteToCopyAddr);
                unsafe.putFloat(dest, destOffset, f);
            }
            if (type.equals(Type.DOUBLE)) {
                double d = unsafe.getDouble(byteToCopyAddr);
                unsafe.putDouble(dest, destOffset, d);
            }
        }
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