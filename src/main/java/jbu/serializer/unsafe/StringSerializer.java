package jbu.serializer.unsafe;

import jbu.Primitive;
import jbu.exception.InvalidJvmException;
import jbu.offheap.LoadContext;
import jbu.offheap.StoreContext;
import jbu.UnsafeReflection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

import static jbu.UnsafeUtil.unsafe;

class StringSerializer extends TypeSerializer<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StringSerializer.class);
    private static final Field OFFSET;
    private static final Field COUNT;
    private static final Field VALUE;

    static {
        Field offset = null;
        Field count = null;
        Field value = null;
        try {
            offset = String.class.getDeclaredField("offset");
            count = String.class.getDeclaredField("count");
            value = String.class.getDeclaredField("value");
        } catch (NoSuchFieldException e) {
            // not a standart JVM
            throw new InvalidJvmException("Not a standard JVM ? Cannot find field in String", e);
        }
        OFFSET = offset;
        COUNT = count;
        VALUE = value;

    }

    @Override
    void serialize(Object sourceObject, StoreContext sc, ClassDesc cd, int fieldIndex) {
        serialize((String) unsafe.getObject(sourceObject, cd.offsets[fieldIndex]), cd.types[fieldIndex], sc);
    }

    @Override
    void serialize(String string, Type type, StoreContext sc) {
        // For serializing String

        // get his offset
        int offet = UnsafeReflection.getInt(OFFSET, string);
        // get his count
        int count = UnsafeReflection.getInt(COUNT, string);
        // then serialize part of his internal char array
        Object internalArray = UnsafeReflection.getObject(VALUE, string);
        int arrayBaseOffset = UnsafeReflection.arrayBaseOffset(internalArray);
        int arrayScale = UnsafeReflection.arrayIndexScale(internalArray);
        // Find memory offset where string begin
        int begin = arrayBaseOffset + (offet * arrayScale);
        // Find how many byte copy
        int length = (count * arrayScale);
        // Store only length on 4 Bytes
        sc.storeInt(count);
        sc.storeSomething(internalArray, begin, length);
    }

    @Override
    void deserialize(LoadContext lc, ClassDesc cd, Object dest, int fieldIndex) {
        UnsafeReflection.setObject(cd.fields[fieldIndex], dest, deserialize(cd.types[fieldIndex], lc));
    }

    @Override
    String deserialize(Type type, LoadContext lc) {
        // get his length
        int stringLength = lc.loadInt();
        // recreate a char[] with content
        char[] internalArray = new char[stringLength];
        // copy content in new array
        lc.loadArray(internalArray, UnsafeReflection.arrayBaseOffset(internalArray),
                UnsafeReflection.getArraySizeContentInMem(internalArray));
        // create a new string
        String destString = new String();
        // replace all his internal value
        UnsafeReflection.setInt(OFFSET, destString, 0);
        UnsafeReflection.setInt(COUNT, destString, stringLength);
        UnsafeReflection.setObject(VALUE, destString, internalArray);
        return destString;
    }

    public int serializedSize(String obj) {
        return (UnsafeReflection.getInt(COUNT, obj) * Primitive.CHAR_LENGTH) + Primitive.INT_LENGTH;
    }
}
