package jbu.serializer.unsafe.type;

import jbu.offheap.LoadContext;
import jbu.offheap.StoreContext;
import jbu.offheap.UnsafeUtil;
import jbu.serializer.unsafe.ClassDesc;
import jbu.serializer.unsafe.UnsafeReflection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class StringSerializer extends TypeSerializer<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StringSerializer.class);
    private static Field OFFSET;
    private static Field COUNT;
    private static Field VALUE;

    static {
        try {
            OFFSET = String.class.getDeclaredField("offset");
            COUNT = String.class.getDeclaredField("count");
            VALUE = String.class.getDeclaredField("value");
        } catch (NoSuchFieldException e) {
            // ?? not a standart JVM ?
            // exit
            LOGGER.error("Not a standard JVM ? Cannot find field in String");
            System.exit(1);
        }

    }

    @Override
    public void serialize(Object sourceObject, StoreContext sc, ClassDesc cd, int fieldIndex) {
        Object string = unsafe.getObject(sourceObject, cd.offsets[fieldIndex]);
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
    public void deserialize(LoadContext lc, ClassDesc cd, Object dest, int index) {

        Object string = unsafe.getObject(dest, cd.offsets[index]);
        // For serializing String
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
        // FIXME must be replaced by unsafe impl
        UnsafeReflection.setObject(cd.fields[index], dest, destString);
    }
}
