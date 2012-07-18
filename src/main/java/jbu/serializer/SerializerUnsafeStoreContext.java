package jbu.serializer;

import jbu.offheap.Allocator;
import jbu.offheap.UnsafeUtil;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

public class SerializerUnsafeStoreContext implements SerializerSink, SerializerSource {

    private static final Unsafe unsafe = UnsafeUtil.unsafe;

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

    private Allocator.StoreContext sc;

    public SerializerUnsafeStoreContext(Allocator.StoreContext sc) {
        this.sc = sc;
    }

    @Override
    public void writeBoolean(Boolean bool) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void writeByte(Byte b) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void writeChar(Character c) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void writeDouble(Double d) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void writeFloat(Float f) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void writeInteger(Integer i) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void writeShort(Short s) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void writeLong(Long l) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void writeString(String s) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Boolean readBoolean() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Byte readByte() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Character readChar() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Double readDouble() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Float readFloat() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Integer readInteger() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Short readShort() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Long readLong() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String readString() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
