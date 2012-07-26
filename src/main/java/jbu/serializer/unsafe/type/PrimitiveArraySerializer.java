package jbu.serializer.unsafe.type;

import jbu.offheap.LoadContext;
import jbu.offheap.StoreContext;
import jbu.serializer.unsafe.ClassDesc;
import jbu.serializer.unsafe.UnsafeReflection;

import java.lang.reflect.Array;

public class PrimitiveArraySerializer extends TypeSerializer {
    @Override
    public void serialize(Object sourceObject, StoreContext sc, ClassDesc cd, int fieldIndex) {
        // Array of primitive
        Object array = unsafe.getObject(sourceObject, cd.offsets[fieldIndex]);
        int arrayContentLength = UnsafeReflection.getArraySizeContentInMem(array);
        int arrayLength = UnsafeReflection.getArrayLength(array);
        int arrayBaseOffset = UnsafeReflection.arrayBaseOffset(array);
        // Store only length on 4 Bytes
        sc.storeInt(arrayLength);
        sc.storeSomething(array, arrayBaseOffset, arrayContentLength);
    }

    @Override
    public void deserialize(LoadContext lc, ClassDesc cd, Object dest, int index) {
        // Retrieve length encoded in 4 bytes
        int arrayLength = lc.loadInt();
        // Instanciate a new array with length
        // FIXME Verify cd.field[index].getType is an array
        Object newArray = Array.newInstance(cd.fields[index].getType().getComponentType(), arrayLength);
        // Replace content in heap from content serialized in offheap
        Object heapArray = UnsafeReflection.getObject(cd.fields[index], dest);
        UnsafeReflection.setObject(cd.fields[index], dest, newArray);
        lc.loadArray(UnsafeReflection.getObject(cd.fields[index], dest), UnsafeReflection.arrayBaseOffset(heapArray),
                UnsafeReflection.getArraySizeContentInMem(newArray));
    }
}
