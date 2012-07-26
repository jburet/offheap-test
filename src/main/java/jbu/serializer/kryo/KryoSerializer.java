package jbu.serializer.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import jbu.offheap.LoadContext;
import jbu.offheap.StoreContext;
import jbu.serializer.Serializer;
import jbu.serializer.unsafe.UnsafeReflection;

public class KryoSerializer implements Serializer {

    @Override
    public void serialize(Object obj, StoreContext sc) {
        Kryo kryo = KryoFactory.getInstance();
        Output out = new Output(2048);
        kryo.writeClassAndObject(out, obj);
        byte[] ser = out.toBytes();
        sc.storeInt(ser.length);
        sc.storeSomething(ser, UnsafeReflection.arrayBaseOffset(ser), UnsafeReflection.getArraySizeContentInMem(ser));
    }

    @Override
    public Object deserialize(LoadContext lc) {
        int serSize = lc.loadInt();
        byte[] ser = new byte[serSize];
        lc.loadArray(ser, UnsafeReflection.arrayBaseOffset(ser), UnsafeReflection.getArraySizeContentInMem(ser));
        Kryo kryo = KryoFactory.getInstance();
        Input in = new Input(ser);
        Object res = kryo.readClassAndObject(in);
        return res;
    }

    @Override
    public int estimateSerializedSize(Object obj) {
        return 2048;
    }
}
