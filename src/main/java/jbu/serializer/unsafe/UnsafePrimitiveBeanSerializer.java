package jbu.serializer.unsafe;

import static jbu.UnsafeUtil.unsafe;
import static jbu.Primitive.*;

import jbu.UnsafeReflection;
import jbu.exception.CannotDeserializeException;
import jbu.exception.InvalidJvmException;
import jbu.offheap.LoadContext;
import jbu.offheap.StoreContext;
import jbu.serializer.Serializer;
import jbu.serializer.unsafe.type.ClassDesc;
import jbu.serializer.unsafe.type.Type;

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


    /**
     * Deserialize inside obj. Offset of loadcontext must be placed at the beginning of serialized object
     *
     * @param lc
     */
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
    public int estimateSerializedSize(Object obj) {
        Class clazz = obj.getClass();
        ClassDesc cd = ClassDesc.resolveByClass(clazz);
        int size = 0;

        // Add class reference
        size += LONG_LENGTH;

        // Add size for each field
        for (int i = 0; i < cd.nbFields; i++) {
            // FIXME support only primitive and array
            if (cd.types[i].isArray) {
                Object array = unsafe.getObject(obj, cd.offsets[i]);
                // add array size in memory
                int arrayLength = UnsafeReflection.getArraySizeContentInMem(array);
                size += arrayLength;
                // add memory length stored in a int
                size += INT_LENGTH;

            } else if (cd.types[i].isPrimitive) {
                size += cd.types[i].getLength();
            } else {
                if (cd.types[i].equals(Type.STRING)) {
                    // get count and * 2
                    Object string = unsafe.getObject(obj, cd.offsets[i]);
                    try {
                        size += UnsafeReflection.getInt(String.class.getDeclaredField("count"), string) * CHAR_LENGTH;
                        size += INT_LENGTH;
                    } catch (NoSuchFieldException e) {
                        throw new InvalidJvmException("String class not as exception", e);
                    }
                }
            }
        }
        return size;
    }
}
