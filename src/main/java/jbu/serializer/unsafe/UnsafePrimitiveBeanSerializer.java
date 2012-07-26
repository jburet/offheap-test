package jbu.serializer.unsafe;


import jbu.offheap.LoadContext;
import jbu.offheap.StoreContext;
import jbu.offheap.UnsafeUtil;
import jbu.serializer.Serializer;
import jbu.serializer.unsafe.type.StringSerializer;
import sun.misc.Unsafe;

import java.lang.reflect.Array;

public class UnsafePrimitiveBeanSerializer implements Serializer {

    private static final Unsafe unsafe = UnsafeUtil.unsafe;

    @Override
    public void serialize(Object obj, StoreContext sc) {
        Class clazz = obj.getClass();
        ClassDesc cd = ClassDesc.resolveByClass(clazz);

        // Serialize class reference
        sc.storeInt(cd.classReference);
        for (int i = 0; i < cd.nbFields; i++) {
            cd.types[i].typeSerializer.serialize(obj, sc, cd, i);
        }
    }


    /**
     * Deserialize inside obj. Offset of loadcontext must be placed at the beginning of serialized object
     *
     * @param lc
     */
    @Override
    public Object deserialize(LoadContext lc) {
        ClassDesc cd = ClassDesc.resolveByRef(lc.loadInt());
        Object res = null;
        // constructor without arg must exist...
        try {
            res = cd.clazz.newInstance();
            // FIXME How manage this exception ?
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < cd.nbFields; i++) {
            cd.types[i].typeSerializer.deserialize(lc, cd, res, i);
        }
        return res;
    }


    @Override
    public int estimateSerializedSize(Object obj) {
        Class clazz = obj.getClass();
        ClassDesc cd = ClassDesc.resolveByClass(clazz);
        int size = 0;

        // Add class reference
        size += 8;

        // Add size for each field
        for (int i = 0; i < cd.nbFields; i++) {
            // FIXME support only primitive and array
            if (cd.types[i].isArray) {
                Object array = unsafe.getObject(obj, cd.offsets[i]);
                // add array size in memory
                int arrayLength = UnsafeReflection.getArraySizeContentInMem(array);
                size += arrayLength;
                // add memory length stored in a int
                size += 4;

            } else if (cd.types[i].isPrimitive) {
                size += cd.types[i].getLength();
            } else {
                if (cd.types[i].equals(Type.STRING)) {
                    // get count and * 2
                    Object string = unsafe.getObject(obj, cd.offsets[i]);
                    try {

                        size += UnsafeReflection.getInt(String.class.getDeclaredField("count"), string) * 2;
                        size += 4;
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            }
        }
        return size;
    }
}
