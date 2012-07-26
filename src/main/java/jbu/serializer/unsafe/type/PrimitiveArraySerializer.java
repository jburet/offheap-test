package jbu.serializer.unsafe.type;

import jbu.offheap.LoadContext;
import jbu.offheap.StoreContext;
import jbu.serializer.unsafe.ClassDesc;
import jbu.serializer.unsafe.Type;
import jbu.serializer.unsafe.UnsafeReflection;

import java.lang.reflect.Array;

public class PrimitiveArraySerializer extends TypeSerializer {
    @Override
    public void serialize(Object sourceObject, StoreContext sc, ClassDesc cd, int fieldIndex) {
        // Array of primitive
        serialize(unsafe.getObject(sourceObject, cd.offsets[fieldIndex]), cd.types[fieldIndex], sc);
    }

    @Override
    public void serialize(Object array, Type type, StoreContext sc) {
        int arrayContentLength = UnsafeReflection.getArraySizeContentInMem(array);
        int arrayLength = UnsafeReflection.getArrayLength(array);
        int arrayBaseOffset = UnsafeReflection.arrayBaseOffset(array);
        // Store only length on 4 Bytes
        sc.storeInt(arrayLength);
        sc.storeSomething(array, arrayBaseOffset, arrayContentLength);
    }

    @Override
    public void deserialize(LoadContext lc, ClassDesc cd, Object dest, int fieldIndex) {
        Object newArray = deserialize(cd.types[fieldIndex], lc);
        UnsafeReflection.setObject(cd.fields[fieldIndex], dest, newArray);
    }

    @Override
    public Object deserialize(Type type, LoadContext lc) {
        // Retrieve length encoded in 4 bytes
        int arrayLength = lc.loadInt();
        // Instanciate a new array with length
        // FIXME Verify cd.field[index].getType is an array
        Object newArray = Array.newInstance(type.clazz, arrayLength);
        lc.loadArray(newArray, UnsafeReflection.arrayBaseOffset(newArray),
                UnsafeReflection.getArraySizeContentInMem(newArray));
        return newArray;
    }
}
