package jbu.serializer;


import jbu.offheap.Allocator;
import jbu.offheap.LoadContext;
import jbu.offheap.UnsafeUtil;
import sun.misc.Unsafe;

import java.lang.reflect.Array;

public class UnsafePrimitiveBeanSerializer {

    private static final Unsafe unsafe = UnsafeUtil.unsafe;

    public void serialize(Object obj, Allocator.StoreContext sc) {
        Class clazz = obj.getClass();
        ClassDesc cd = ClassDesc.resolveByClass(clazz);

        // Serialize class reference
        sc.storeInt(cd.classReference);
        for (int i = 0; i < cd.nbFields; i++) {
            if (cd.types[i].isArray) {
                Object array = unsafe.getObject(obj, cd.offsets[i]);
                int arrayContentLength = UnsafeReflection.getArraySizeContentInMem(array);
                int arrayLength = UnsafeReflection.getArrayLength(array);
                int arrayBaseOffset = UnsafeReflection.arrayBaseOffset(array);
                // Store only length on 4 Bytes on after content length
                sc.storeInt(arrayLength);
                sc.storeSomething(array, arrayBaseOffset, arrayContentLength);
            } else {
                sc.storeSomething(obj, cd.offsets[i], cd.types[i].getLength());
            }
        }
    }

    /**
     * Deserialize inside obj. Offset of loadcontext must be placed at the beginning of serialized object
     *
     * @param lc
     */
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
            if (cd.types[i].isArray) {
                // Retrieve length encoded in 4 bytes
                int arrayLength = lc.loadInt();
                // Instanciate a new array with length
                // FIXME Verify cd.field[i].getType is an array
                Object newArray = Array.newInstance(cd.fields[i].getType().getComponentType(), arrayLength);
                // Replace content in heap from content serialized in offheap
                Object heapArray = UnsafeReflection.getObject(cd.fields[i], res);
                try {
                    cd.fields[i].set(res, newArray);
                } catch (IllegalAccessException e) {
                    // FIXME manage this exception
                    e.printStackTrace();
                }
                lc.loadSomething2(UnsafeReflection.getObject(cd.fields[i], res), UnsafeReflection.arrayBaseOffset(heapArray),
                        UnsafeReflection.getArraySizeContentInMem(newArray));
            } else {
                lc.loadSomething(res, cd.offsets[i], cd.types[i], cd.types[i].getLength());
            }
        }
        return res;
    }

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

            } else {
                size += cd.types[i].getLength();
            }
        }
        return size;
    }
}
