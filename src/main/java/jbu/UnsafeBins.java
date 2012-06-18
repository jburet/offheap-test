package jbu;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class UnsafeBins extends Bins {

    static final long UNSAFE_COPY_THRESHOLD = 1024L * 1024L;

    private long arrayBaseOffset = (long)unsafe.arrayBaseOffset(byte[].class);

    // Unsafe instanciation
    private static final Unsafe unsafe = getUnsafeInstance();

    private static Unsafe getUnsafeInstance() {
        try {
            Field theUnsafeInstance = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafeInstance.setAccessible(true);
            return (Unsafe) theUnsafeInstance.get(Unsafe.class);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    private final long binAddr;

    UnsafeBins(int initialChunkNumber, int chunkSize, int baseAddr) {
        super(initialChunkNumber, chunkSize, baseAddr);
        // FIXME Cannot allocate more than Integer.MAX_VALUE. Check this
        binAddr = unsafe.allocateMemory(initialChunkNumber * finalChunkSize);
    }

    @Override
    boolean storeInChunk(int chunkId, byte[] data, int currentOffset, int length) {
        // check size for avoid overflow on other chunk
        if (length > this.chunkSize) {
            throw new BufferOverflowException("Try to store too many data. Store "
                    + data.length + " in " + this.chunkSize);
        }
        long baseAddr = findOffsetForChunkId(chunkId) + binAddr;

        // put the length
        unsafe.putInt(baseAddr, length);

        long dstAddr = baseAddr + 4;
        long offset = arrayBaseOffset;
        while (length > 0) {
            long size = (length > UNSAFE_COPY_THRESHOLD) ? UNSAFE_COPY_THRESHOLD : length;
            unsafe.copyMemory(data, offset, null, dstAddr, size);
            length -= size;
            offset += size;
            dstAddr += size;
        }

        return true;
    }

    @Override
    byte[] loadFromChunk(int chunkId) {
        long baseAddr = binAddr + findOffsetForChunkId(chunkId);
        int size = unsafe.getInt(baseAddr);
        byte[] data = new byte[size];
        for (int i = 0; i < size; i++) {
            data[i] = unsafe.getByte(baseAddr + 4 + i);
        }
        return data;
    }

    @Override
    void setNextChunkId(int currentChunkId, long nextChunkId) {
        // Set nextChunkId to last 8 bytes of currentChunkId
        long nextChunkOffset = binAddr + findOffsetForChunkId(currentChunkId) + chunkSize + 4;
        unsafe.putLong(nextChunkOffset, nextChunkId);
    }

    @Override
    long getNextChunkId(int currentChunkId) {
        // Set nextChunkId to last 8 bytes of currentChunkId
        long nextChunkOffset = binAddr + findOffsetForChunkId(currentChunkId) + chunkSize + 4;
        return unsafe.getLong(nextChunkOffset);
    }

}
