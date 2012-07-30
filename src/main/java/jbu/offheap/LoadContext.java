package jbu.offheap;

import static jbu.UnsafeUtil.unsafe;
import static jbu.Primitive.*;

public class LoadContext {

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

    public boolean loadBoolean() {
        boolean res = unsafe.getBoolean(null, this.currentBaseAdr + this.currentOffset);
        this.currentOffset += BOOLEAN_LENGTH;
        this.remaining -= BOOLEAN_LENGTH;
        return res;
    }

    public char loadChar() {
        char res = unsafe.getChar(this.currentBaseAdr + this.currentOffset);
        this.currentOffset += CHAR_LENGTH;
        this.remaining -= CHAR_LENGTH;
        return res;
    }

    public byte loadByte() {
        byte res = unsafe.getByte(this.currentBaseAdr + this.currentOffset);
        this.currentOffset += BYTE_LENGTH;
        this.remaining -= BYTE_LENGTH;
        return res;
    }

    public short loadShort() {
        short res = unsafe.getShort(this.currentBaseAdr + this.currentOffset);
        this.currentOffset += SHORT_LENGTH;
        this.remaining -= SHORT_LENGTH;
        return res;
    }

    public int loadInt() {
        int res = unsafe.getInt(this.currentBaseAdr + this.currentOffset);
        this.currentOffset += INT_LENGTH;
        this.remaining -= INT_LENGTH;
        return res;
    }

    public long loadLong() {
        long res = unsafe.getLong(this.currentBaseAdr + this.currentOffset);
        this.currentOffset += LONG_LENGTH;
        this.remaining -= LONG_LENGTH;
        return res;
    }

    public float loadFloat() {
        float res = unsafe.getFloat(this.currentBaseAdr + this.currentOffset);
        this.currentOffset += FLOAT_LENGTH;
        this.remaining -= FLOAT_LENGTH;
        return res;
    }

    public double loadDouble() {
        double res = unsafe.getDouble(this.currentBaseAdr + this.currentOffset);
        this.currentOffset += DOUBLE_LENGTH;
        this.remaining -= DOUBLE_LENGTH;
        return res;
    }

    // NOT USE IT for another thing than array .... NYI (Not yet implemented)
    // Can be used for array...
    public void loadArray(Object dest, final long offset, final int arrayLength) {
        int byteRemaining = arrayLength;
        do {
            int byteToCopy = (byteRemaining > remaining) ? remaining : byteRemaining;
            //int d = unsafe.getInt(this.currentBaseAdr + this.currentOffset);
            // FIXME WHY DIRECT MEMORY DON'T WORK !!!!!!!!!! (throw illegalArgument)
            // FIXME Marked as NotYetImplemented in code work only if destination are primitive array
            // See http://mail.openjdk.java.net/pipermail/hotspot-runtime-dev/2012-March/003322.html
            unsafe.copyMemory(null, this.currentBaseAdr + this.currentOffset, dest,
                    offset + (arrayLength - byteRemaining), byteToCopy);

            byteRemaining -= byteToCopy;

            // Update LoadContext state
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
    public void loadPrimitive(Object dest, final long destOffset, final int primitiveLength) {
        int totalByteRemaining = primitiveLength;
        do {
            int byteToCopy = (totalByteRemaining > remaining) ? remaining : totalByteRemaining;
            int byteRemaining = byteToCopy;

            // primitive take 8,4,2 or 1 byte
            // Take the greatest size under totalByteRemaining
            if (byteRemaining / LONG_LENGTH == 1) {
                // copy 8 byte
                long b = unsafe.getLong(null, this.currentBaseAdr + this.currentOffset);
                unsafe.putLong(dest, destOffset, b);
                byteRemaining -= LONG_LENGTH;
            }
            if (byteRemaining / INT_LENGTH == 1) {
                // copy 4 byte
                int b = unsafe.getInt(null, this.currentBaseAdr + this.currentOffset);
                unsafe.putInt(dest, destOffset, b);
                byteRemaining -= INT_LENGTH;
            }
            if (byteRemaining / SHORT_LENGTH == 1) {
                // copy 2 byte
                short b = unsafe.getShort(null, this.currentBaseAdr + this.currentOffset);
                unsafe.putShort(dest, destOffset, b);
                byteRemaining -= SHORT_LENGTH;
            }
            if (byteRemaining / BYTE_LENGTH == 1) {
                // copy 1 byte
                byte b = unsafe.getByte(null, this.currentBaseAdr + this.currentOffset);
                unsafe.putByte(dest, destOffset, b);
                byteRemaining -= BYTE_LENGTH;
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
        // get bins
        // FIXME Suport only unsafebin
        // Get baseAdr of allocated memory
        // Get baseOffset of chunk
        // And store this in currentBaseAdr
        UnsafeBins b = (UnsafeBins) allocator.getBinFromAddr(chunkAdr);
        this.currentBaseAdr = b.binAddr + b.findOffsetForChunkId(AddrAlign.getChunkId(chunkAdr));
        // Put the offset to 4... Don't read chunk size. Always same value as chunk size
        this.currentOffset = INT_LENGTH;
        this.remaining = b.chunkSize;

    }

}