package jbu.serializer.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import jbu.offheap.LoadContext;
import jbu.offheap.StoreContext;
import jbu.serializer.Serializer;
import jbu.UnsafeReflection;

/**
 * Serialize/Deserialize with Kryo directly inside StoreContext
 */
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
        return kryo.readClassAndObject(in);
    }

    @Override
    public int calculateSerializedSize(Object obj) {
        return 2048;
    }
}
