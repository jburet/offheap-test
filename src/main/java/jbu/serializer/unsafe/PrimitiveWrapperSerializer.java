package jbu.serializer.unsafe;

import jbu.UnsafeReflection;
import jbu.exception.InvalidJvmException;
import jbu.offheap.LoadContext;
import jbu.offheap.StoreContext;

import java.lang.reflect.Field;

import static jbu.UnsafeUtil.unsafe;

class PrimitiveWrapperSerializer<T> extends TypeSerializer<T> {

    private final long offset;
    private final Field value;

    PrimitiveWrapperSerializer(Class<?> wrapper) {
        Field value = null;
        try {
            value = wrapper.getDeclaredField("value");
        } catch (NoSuchFieldException e) {
            // not a standart JVM
            throw new InvalidJvmException("Not a standard JVM ? Cannot find value field in primitive wrapper", e);
        }
        this.offset = UnsafeReflection.getOffset(value);
        this.value = value;
    }

    @Override
    void serialize(Object sourceObject, StoreContext sc, ClassDesc cd, int fieldIndex) {
        // Get the wrapper object
        serialize((T) unsafe.getObject(sourceObject, cd.offsets[fieldIndex]), cd.types[fieldIndex], sc);
    }

    @Override
    void serialize(T objectToSerialize, Type type, StoreContext sc) {
        sc.storeSomething(objectToSerialize, offset, type.typeSize);
    }

    @Override
    void deserialize(LoadContext lc, ClassDesc cd, Object dest, int fieldIndex) {
        // instanciate wrapper object
        if (cd.types[fieldIndex].clazz.equals(Boolean.class)) {
            UnsafeReflection.setObject(cd.fields[fieldIndex], dest, new Boolean(lc.loadBoolean()));
        } else if (cd.types[fieldIndex].clazz.equals(Character.class)) {
            UnsafeReflection.setObject(cd.fields[fieldIndex], dest, new Character(lc.loadChar()));
        } else if (cd.types[fieldIndex].clazz.equals(Byte.class)) {
            UnsafeReflection.setObject(cd.fields[fieldIndex], dest, new Byte(lc.loadByte()));
        } else if (cd.types[fieldIndex].clazz.equals(Short.class)) {
            UnsafeReflection.setObject(cd.fields[fieldIndex], dest, new Short(lc.loadShort()));
        } else if (cd.types[fieldIndex].clazz.equals(Integer.class)) {
            UnsafeReflection.setObject(cd.fields[fieldIndex], dest, new Integer(lc.loadInt()));
        } else if (cd.types[fieldIndex].clazz.equals(Long.class)) {
            UnsafeReflection.setObject(cd.fields[fieldIndex], dest, new Long(lc.loadLong()));
        } else if (cd.types[fieldIndex].clazz.equals(Float.class)) {
            UnsafeReflection.setObject(cd.fields[fieldIndex], dest, new Float(lc.loadFloat()));
        } else if (cd.types[fieldIndex].clazz.equals(Double.class)) {
            UnsafeReflection.setObject(cd.fields[fieldIndex], dest, new Double(lc.loadDouble()));
        }
    }

    @Override
    T deserialize(Type type, LoadContext lc) {
        return null;
    }
}
