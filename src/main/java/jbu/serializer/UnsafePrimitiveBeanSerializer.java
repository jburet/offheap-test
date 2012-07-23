package jbu.serializer;


import jbu.offheap.Allocator;
import jbu.offheap.UnsafeUtil;
import sun.misc.Unsafe;

import java.lang.reflect.Array;
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
        registeredClassOffset.put(c, new ClassDesc(types, offsets, fields));
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
                Object array = unsafe.getObject(obj, cd.offsets[i]);
                UnsafeReflection.debugArray(array);
                int arrayContentLength = UnsafeReflection.getArraySizeContentInMem(array);
                int arrayLength = UnsafeReflection.getArrayLength(array);
                int arrayBaseOffset = UnsafeReflection.arrayBaseOffset(array);
                // Store only length on 4 Bytes on after content length
                sc.storeInt(arrayLength);
                // FIXME In 64bits with oops compression need to add 4 bytes of padding
                // FIXME In 64bits without oops compression need 8 bytes..
                sc.storeSomething(obj, cd.offsets[i] + arrayBaseOffset+4, arrayContentLength);
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
                // Retrieve length encoded in 4 bytes
                int arrayLength = lc.loadInt();
                // Instanciate a new array with length
                // FIXME Verify cd.field[i].getType is an array
                Object newArray = Array.newInstance(cd.fields[i].getType().getComponentType(), arrayLength);
                // Replace content in heap from content serialized in offheap
                Object heapArray = UnsafeReflection.getObject(cd.fields[i], obj);
                try {
                    cd.fields[i].set(obj, newArray);
                } catch (IllegalAccessException e) {
                    // FIXME manage this exception
                    e.printStackTrace();
                }
                lc.loadSomething2(UnsafeReflection.getObject(cd.fields[i], obj), UnsafeReflection.arrayBaseOffset(heapArray), UnsafeReflection.getArraySizeContentInMem(newArray));
                // put array in object field

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
                Object array = unsafe.getObject(obj, cd.offsets[i]);
                int arrayLength = UnsafeReflection.getArraySizeContentInMem(array);
                size += arrayLength;
            } else {
                size += cd.types[i].getLength();
            }
        }
        return size;
    }

}
