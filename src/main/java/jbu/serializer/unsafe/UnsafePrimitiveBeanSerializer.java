package jbu.serializer.unsafe;

import static jbu.Primitive.*;

import jbu.UnsafeReflection;
import jbu.exception.CannotDeserializeException;
import jbu.offheap.LoadContext;
import jbu.offheap.StoreContext;
import jbu.serializer.Serializer;

/**
 * Serializer implemented with Unsafe class.
 * This class use directly memory copy from Heap to Native Memory
 */
public class UnsafePrimitiveBeanSerializer implements Serializer {

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

    @Override
    public Object deserialize(LoadContext lc) throws CannotDeserializeException {
        ClassDesc cd = ClassDesc.resolveByRef(lc.loadInt());
        Object res = null;
        // constructor without arg must exist...
        try {
            res = cd.clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new CannotDeserializeException("No public default constructor for class " + cd.clazz, e);
        }
        for (int i = 0; i < cd.nbFields; i++) {
            cd.types[i].typeSerializer.deserialize(lc, cd, res, i);
        }
        return res;
    }


    @Override
    public int calculateSerializedSize(Object obj) {
        Class clazz = obj.getClass();
        ClassDesc cd = ClassDesc.resolveByClass(clazz);
        int size = 0;

        // Add class reference
        size += LONG_LENGTH;

        // Add size for each field
        for (int i = 0; i < cd.nbFields; i++) {
            size += cd.types[i].serializedSize(UnsafeReflection.getObject(cd.fields[i], obj));
        }
        return size;
    }
}
