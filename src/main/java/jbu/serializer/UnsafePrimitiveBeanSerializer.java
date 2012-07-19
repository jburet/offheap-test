package jbu.serializer;


import jbu.offheap.Allocator;
import jbu.offheap.UnsafeUtil;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.*;

public class UnsafePrimitiveBeanSerializer {

    private static final Map<Class, ClassDesc> registeredClassOffset = new HashMap<Class, ClassDesc>();

    private static final Unsafe unsafe = UnsafeUtil.unsafe;

    public static ClassDesc registerClass(Class c) {
        Field[] fields = c.getDeclaredFields();
        long[] offsets = new long[fields.length];
        Type[] types = new Type[fields.length];
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            f.setAccessible(true);
            offsets[i] = unsafe.objectFieldOffset(f);
            types[i] = Type.resolveType(f);
        }
        registeredClassOffset.put(c, new ClassDesc(types, offsets));
        return registeredClassOffset.get(c);
    }

    public void serialize(Object obj, Allocator.StoreContext sc) {
        Class clazz = obj.getClass();
        ClassDesc cd = null;
        if ((cd = registeredClassOffset.get(clazz)) == null) {
            cd = registerClass(clazz);
        }
        for (int i = 0; i < cd.nbFields; i++) {
            if (cd.types[i].isArray) {
                int[] arrayLength = UnsafeReflection.getArrayLength(obj, cd.offsets[i], cd.types[i].arrayProof);
                sc.storeSomething(obj, cd.offsets[i], cd.types[i].getLength(arrayLength));
            } else {
                sc.storeSomething(obj, cd.offsets[i], cd.types[i].getLength());
            }
        }
    }

    public void deserialize(Object obj, Allocator.LoadContext lc) {
        Class clazz = obj.getClass();
        ClassDesc cd = null;
        if ((cd = registeredClassOffset.get(clazz)) == null) {
            cd = registerClass(clazz);
        }
        for (int i = 0; i < cd.nbFields; i++) {
            if (cd.types[i].isArray) {
                int[] arrayLength = UnsafeReflection.getArrayLength(obj, cd.offsets[i], cd.types[i].arrayProof);
                lc.loadSomething(obj, cd.offsets[i], cd.types[i], cd.types[i].getLength(arrayLength));
            } else {
                lc.loadSomething(obj, cd.offsets[i], cd.types[i], cd.types[i].getLength());
            }
        }
    }

    public int estimateSize(Object obj) {
        Class clazz = obj.getClass();
        ClassDesc cd = null;
        if ((cd = registeredClassOffset.get(clazz)) == null) {
            cd = registerClass(clazz);
        }
        int size = 0;
        for (int i = 0; i < cd.nbFields; i++) {
            // FIXME support only primitive and array
            if (cd.types[i].isArray) {
                int[] arrayLength = UnsafeReflection.getArrayLength(obj, cd.offsets[i], cd.types[i].arrayProof);
                size += cd.types[i].getLength(arrayLength);
            } else {
                size += cd.types[i].getLength();
            }
        }
        return size;
    }

}
