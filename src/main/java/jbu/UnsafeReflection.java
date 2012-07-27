package jbu;

import static jbu.Primitive.*;
import static jbu.UnsafeUtil.unsafe;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Reflection without any kind of check...
 * Using bad type or field with not same class than object class can cause JVM crash....
 */
public final class UnsafeReflection {

    private static final int OPEN_JDK_32_ARRAY_OFFSET = 12;
    private static final int OPEN_JDK_32_ARRAY_LENGTH_OFFSET = 8;
    private static final int OPEN_JDK_64_OOPS_ARRAY_OFFSET = 16;
    private static final int OPEN_JDK_64_OOPS_ARRAY_LENGTH_OFFSET = 12;
    private static final int OPEN_JDK_64_ARRAY_OFFSET = 24;
    private static final int OPEN_JDK_64_ARRAY_LENGTH_OFFSET = 16;

    private static Map<Field, Long> offsetCache = new HashMap<Field, Long>();
    private static final int arrayBaseOffset = unsafe.ARRAY_OBJECT_BASE_OFFSET;

    private UnsafeReflection() {
    }

    public static int getInt(Field field, Object instance) {
        long offset = getOffset(field);
        return unsafe.getInt(instance, offset);
    }

    public static void setInt(Field field, Object instance, int value) {
        long offset = getOffset(field);
        unsafe.putInt(instance, offset, value);
    }

    public static boolean getBoolean(Field field, Object instance) {
        long offset = getOffset(field);
        return unsafe.getBoolean(instance, offset);
    }

    public static byte getByte(Field field, Object instance) {
        long offset = getOffset(field);
        return unsafe.getByte(instance, offset);
    }

    public static short getShort(Field field, Object instance) {
        long offset = getOffset(field);
        return unsafe.getShort(instance, offset);
    }

    public static long getLong(Field field, Object instance) {
        long offset = getOffset(field);
        return unsafe.getLong(instance, offset);
    }

    public static char getChar(Field field, Object instance) {
        long offset = getOffset(field);
        return unsafe.getChar(instance, offset);
    }

    public static float getFloat(Field field, Object instance) {
        long offset = getOffset(field);
        return unsafe.getFloat(instance, offset);
    }

    public static double getDouble(Field field, Object instance) {
        long offset = getOffset(field);
        return unsafe.getDouble(instance, offset);
    }

    public static Object getObject(Field field, Object instance) {
        long offset = getOffset(field);
        return unsafe.getObject(instance, offset);
    }

    public static void setObject(Field field, Object instance, Object objectToCopy) {
        long offset = getOffset(field);
        unsafe.putObject(instance, offset, objectToCopy);
    }

    public static long getOffset(Field field) {
        Long offset = offsetCache.get(field);
        if (offset == null) {
            offset = unsafe.objectFieldOffset(field);
            offsetCache.put(field, offset);
        }
        return offset;
    }

    public static String debugArray(Object array) {
        StringBuilder sb = new StringBuilder();
        int scale = arrayIndexScale(array);
        sb.append("Offset : ");
        sb.append(arrayBaseOffset(array));
        sb.append("scale : ");
        sb.append(unsafe.arrayIndexScale(array.getClass()));
        sb.append("b0 : ");
        sb.append(unsafe.getInt(array, 0l));
        sb.append("b4 : ");
        sb.append(unsafe.getInt(array, (long) INT_LENGTH));
        sb.append("b8 : ");
        sb.append(unsafe.getInt(array, (long) 2 * INT_LENGTH));
        int length = unsafe.getInt(array, (long) 2 * INT_LENGTH);
        sb.append("length : ");
        sb.append(length);
        for (int i = 0; i < length; i++) {
            if (scale == INT_LENGTH) {
                sb.append("i : ");
                sb.append(unsafe.getInt(array, i * INT_LENGTH + (long) 4 * INT_LENGTH));
            }
            if (scale == LONG_LENGTH) {
                sb.append(i);
                sb.append(": ");
                sb.append(unsafe.getLong(array, i * LONG_LENGTH + (long) 4 * INT_LENGTH));
            }
        }
        return sb.toString();
    }


    public static int getArrayLength(Object array) {
        // FIXME work only in array with dim = 1
        // FIXME JVM and JVM version dependent code... find best solution
        // (at least) 3 case
        // 32 bits : size 8 - 12
        // 64 bits : size 16 - 20 + padding
        // 64 bits w pc : size 12 - 16
        // tested with recent 1.6 64bit with -d32.. abo = 16 but sometime cannot read (or write) length (0...)
        int abo = arrayBaseOffset(array);
        long lengthOff = 0;
        if (abo == OPEN_JDK_32_ARRAY_OFFSET) {
            lengthOff = OPEN_JDK_32_ARRAY_LENGTH_OFFSET;
        } else if (abo == OPEN_JDK_64_OOPS_ARRAY_OFFSET) {
            lengthOff = OPEN_JDK_64_OOPS_ARRAY_LENGTH_OFFSET;
        } else if (abo == OPEN_JDK_64_ARRAY_OFFSET) {
            lengthOff = OPEN_JDK_64_ARRAY_LENGTH_OFFSET;
        }
        return unsafe.getInt(array, lengthOff);
    }

    public static int arrayBaseOffset(Object array) {
        return arrayBaseOffset;
    }


    public static int getArraySizeContentInMem(Object array) {
        int scale = arrayIndexScale(array);
        int al = getArrayLength(array);
        return scale * al;
    }

    public static int arrayIndexScale(Object array) {
        // Use cached value from unsafe is much faster than use method unsafe.arrayIndexScale
        if (array.getClass().equals(boolean[].class)) {
            return unsafe.ARRAY_BOOLEAN_INDEX_SCALE;
        }
        if (array.getClass().equals(char[].class)) {
            return unsafe.ARRAY_CHAR_INDEX_SCALE;
        }
        if (array.getClass().equals(byte[].class)) {
            return unsafe.ARRAY_BYTE_INDEX_SCALE;
        }
        if (array.getClass().equals(short[].class)) {
            return unsafe.ARRAY_SHORT_INDEX_SCALE;
        }
        if (array.getClass().equals(int[].class)) {
            return unsafe.ARRAY_INT_INDEX_SCALE;
        }
        if (array.getClass().equals(long[].class)) {
            return unsafe.ARRAY_LONG_INDEX_SCALE;
        }
        if (array.getClass().equals(float[].class)) {
            return unsafe.ARRAY_FLOAT_INDEX_SCALE;
        }
        if (array.getClass().equals(double[].class)) {
            return unsafe.ARRAY_DOUBLE_INDEX_SCALE;
        }

        return unsafe.ARRAY_OBJECT_INDEX_SCALE;

    }
}
