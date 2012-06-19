package jbu.serializer;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

public class SerializerByteBuffer implements SerializerSink, SerializerSource {

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

    private static final Unsafe unsafe = getUnsafeInstance();

    private static final long sValue;
    private static final long sCount;
    private static final long sOffset;
    private static final long sHash;

    static {
        try {
            sValue = unsafe.objectFieldOffset(String.class.getDeclaredField("value"));
            sCount = unsafe.objectFieldOffset(String.class.getDeclaredField("count"));
            sOffset = unsafe.objectFieldOffset(String.class.getDeclaredField("offset"));
            sHash = unsafe.objectFieldOffset(String.class.getDeclaredField("hash"));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Cannot ser/deser on this VM");
        }
    }

    private ByteBuffer bb;

    public SerializerByteBuffer(ByteBuffer bb) {
        this.bb = bb;
    }

    @Override
    public void writeBoolean(Boolean bool) {
        if (bool) {
            bb.put((byte) 1);
        } else {
            bb.put((byte) 0);
        }
    }

    @Override
    public void writeByte(Byte b) {
        bb.put(b);
    }

    @Override
    public void writeChar(Character c) {
        bb.putChar(c);
    }

    @Override
    public void writeDouble(Double d) {
        bb.putDouble(d);
    }

    @Override
    public void writeFloat(Float f) {
        bb.putFloat(f);
    }

    @Override
    public void writeInteger(Integer i) {
        bb.putInt(i);
    }

    @Override
    public void writeShort(Short s) {
        bb.putShort(s);
    }

    @Override
    public void writeLong(Long l) {
        bb.putLong(l);
    }

    @Override
    public void writeString(String s) {
        int sl = s.length();
        bb.putInt(sl);
        for (int i = 0; i < sl; i++) {
            char c = s.charAt(i);
            bb.putChar(c);
        }
    }

    @Override
    public Boolean readBoolean() {
        int res = bb.getInt();
        return res == 1;
    }

    @Override
    public Byte readByte() {
        return bb.get();
    }

    @Override
    public Character readChar() {
        return bb.getChar();
    }

    @Override
    public Double readDouble() {
        return bb.getDouble();
    }

    @Override
    public Float readFloat() {
        return bb.getFloat();
    }

    @Override
    public Integer readInteger() {
        return bb.getInt();
    }

    @Override
    public Short readShort() {
        return bb.getShort();
    }

    @Override
    public Long readLong() {
        return bb.getLong();
    }

    @Override
    public String readString() {
        //int size = bb.getInt();
        //char[] b = new char[size];
        //for (int i = 0; i < size; i++) {
        //    b[i] = bb.getChar();
        //}
        //return new String(b);

        // Try to avoid double copy.. Only one copy

        int size = bb.getInt();
        char[] b = new char[size];
        for (int i = 0; i < size; i++) {
            b[i] = bb.getChar();
        }
        String res = new String();

        unsafe.putObject(res, sValue, b);
        unsafe.putInt(res, sCount, size);
        unsafe.putInt(res, sOffset, 0);
        unsafe.putInt(res, sHash, 0);

        return res;
    }
}
