package jbu;

import sun.reflect.ReflectionFactory;

import static jbu.UnsafeUtil.unsafe;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper for access by reflection on some field.
 * Instead unsing standart reflection API, this helper use unsafe method for accessing value.
 */
public final class UnsafeReflection {

    private static final int OPEN_JDK_32_ARRAY_OFFSET = 12;
    private static final int OPEN_JDK_32_ARRAY_LENGTH_OFFSET = 8;
    private static final int OPEN_JDK_64_OOPS_ARRAY_OFFSET = 16;
    private static final int OPEN_JDK_64_OOPS_ARRAY_LENGTH_OFFSET = 12;
    private static final int OPEN_JDK_64_ARRAY_OFFSET = 24;
    private static final int OPEN_JDK_64_ARRAY_LENGTH_OFFSET = 16;

    private static Map<Field, Long> offsetCache = new HashMap<>();
    private static final int ARRAY_BASE_OFFSET = unsafe.ARRAY_OBJECT_BASE_OFFSET;
    private static final long ARRAY_LENGTH_OFFSET;

    static {
        // FIXME JVM and JVM version dependent code... find best solution
        // (at least) 3 case
        // 32 bits : size 8 - 12
        // 64 bits : size 16 - 20 + padding
        // 64 bits w pc : size 12 - 16
        // tested with recent 1.6 64bit with -d32.. abo = 16 but sometime cannot read (or write) length (0...)
        if (ARRAY_BASE_OFFSET == OPEN_JDK_32_ARRAY_OFFSET) {
            ARRAY_LENGTH_OFFSET = OPEN_JDK_32_ARRAY_LENGTH_OFFSET;
        } else if (ARRAY_BASE_OFFSET == OPEN_JDK_64_OOPS_ARRAY_OFFSET) {
            ARRAY_LENGTH_OFFSET = OPEN_JDK_64_OOPS_ARRAY_LENGTH_OFFSET;
            //} else if (abo == OPEN_JDK_64_ARRAY_OFFSET) {
        } else {
            ARRAY_LENGTH_OFFSET = OPEN_JDK_64_ARRAY_LENGTH_OFFSET;
        }
    }

    private UnsafeReflection() {
    }

    /**
     * Returns the value of the field represented by this {@code Field}, on
     * the specified object.
     * They are no type verification return int value of 4 next bytes after offset of
     * the field in object.
     *
     * @param field
     * @param object object from which the represented field's value is
     *               to be extracted
     * @return int value of field on object
     */

    public static int getInt(Field field, Object object) {
        long offset = getOffset(field);
        return unsafe.getInt(object, offset);
    }

    /**
     * Sets the value of a field as a {@code int} on the specified object.
     *
     * @param field
     * @param object the object whose field should be modified
     * @param value  the new value for the field of {@code obj}
     *               being modified
     */
    public static void setInt(Field field, Object object, int value) {
        long offset = getOffset(field);
        unsafe.putInt(object, offset, value);
    }

    /**
     * Returns the value of the field represented by this {@code Field}, on
     * the specified object.
     * They are no type verification return boolean value of next bytes after offset of
     * the field in object.
     *
     * @param field
     * @param object object from which the represented field's value is
     *               to be extracted
     * @return boolean value of field on object
     */
    public static boolean getBoolean(Field field, Object object) {
        long offset = getOffset(field);
        return unsafe.getBoolean(object, offset);
    }

    /**
     * Sets the value of a field as a {@code boolean} on the specified object.
     *
     * @param field
     * @param object the object whose field should be modified
     * @param value  the new value for the field of {@code obj}
     *               being modified
     */
    public static void setBoolean(Field field, Object object, boolean value) {
        long offset = getOffset(field);
        unsafe.putBoolean(object, offset, value);
    }

    /**
     * Returns the value of the field represented by this {@code Field}, on
     * the specified object.
     * They are no type verification return boolean value of next bytes after offset of
     * the field in object.
     *
     * @param field
     * @param object object from which the represented field's value is
     *               to be extracted
     * @return boolean value of field on object
     */
    public static byte getByte(Field field, Object object) {
        long offset = getOffset(field);
        return unsafe.getByte(object, offset);
    }

    /**
     * Returns the value of the field represented by this {@code Field}, on
     * the specified object.
     * They are no type verification return boolean value of 2 next bytes after offset of
     * the field in object.
     *
     * @param field
     * @param object object from which the represented field's value is
     *               to be extracted
     * @return short value of field on object
     */
    public static short getShort(Field field, Object object) {
        long offset = getOffset(field);
        return unsafe.getShort(object, offset);
    }

    /**
     * Returns the value of the field represented by this {@code Field}, on
     * the specified object.
     * They are no type verification return boolean value of 8 next bytes after offset of
     * the field in object.
     *
     * @param field
     * @param object object from which the represented field's value is
     *               to be extracted
     * @return long value of field on object
     */
    public static long getLong(Field field, Object object) {
        long offset = getOffset(field);
        return unsafe.getLong(object, offset);
    }

    /**
     * Returns the value of the field represented by this {@code Field}, on
     * the specified object.
     * They are no type verification return boolean value of 2 next bytes after offset of
     * the field in object.
     *
     * @param field
     * @param object object from which the represented field's value is
     *               to be extracted
     * @return char value of field on object
     */
    public static char getChar(Field field, Object object) {
        long offset = getOffset(field);
        return unsafe.getChar(object, offset);
    }

    /**
     * Returns the value of the field represented by this {@code Field}, on
     * the specified object.
     * They are no type verification return boolean value of 4 next bytes after offset of
     * the field in object.
     *
     * @param field
     * @param object object from which the represented field's value is
     *               to be extracted
     * @return float value of field on object
     */
    public static float getFloat(Field field, Object object) {
        long offset = getOffset(field);
        return unsafe.getFloat(object, offset);
    }

    /**
     * Returns the value of the field represented by this {@code Field}, on
     * the specified object.
     * They are no type verification return boolean value of 8 next bytes after offset of
     * the field in object.
     *
     * @param field
     * @param object object from which the represented field's value is
     *               to be extracted
     * @return double value of field on object
     */
    public static double getDouble(Field field, Object object) {
        long offset = getOffset(field);
        return unsafe.getDouble(object, offset);
    }

    /**
     * Returns the value of the field represented by this {@code Field}, on
     * the specified object.
     *
     * @param field
     * @param object object from which the represented field's value is
     *               to be extracted
     * @return double value of field on object
     */
    public static Object getObject(Field field, Object object) {
        long offset = getOffset(field);
        return unsafe.getObject(object, offset);
    }

    /**
     * Sets the value of a field as a {@code int} on the specified object.
     *
     * @param field
     * @param object the object whose field should be modified
     * @param value  the new value for the field of {@code obj}
     *               being modified
     */
    public static void setObject(Field field, Object object, Object value) {
        unsafe.putObject(object, getOffset(field), value);
    }

    /**
     * Report the location of a given field.
     * <p/>
     * COPIED FROM unsafe
     * <p>Do not expect to perform any sort of arithmetic on this offset;
     * it is just a cookie which is passed to the unsafe heap memory accessors.
     * <p/>
     * <p>Any given field will always have the same offset, and no two distinct
     * fields of the same class will ever have the same offset.
     *
     * @param field
     * @return memory offset of this field
     */
    public static long getOffset(Field field) {
        Long offset = offsetCache.get(field);
        if (offset == null) {
            offset = unsafe.objectFieldOffset(field);
            offsetCache.put(field, offset);
        }
        return offset;
    }

    /**
     * Return the length of an array
     *
     * @param array
     * @return
     */
    public static int getArrayLength(Object array) {
        // FIXME work only in array with dim = 1
        return unsafe.getInt(array, ARRAY_LENGTH_OFFSET);
    }

    /**
     * Return the memory offset where data begin in array.
     * This value depend only from JVM and pointer size
     *
     * @param array
     * @return
     */
    public static int arrayBaseOffset(Object array) {
        return ARRAY_BASE_OFFSET;
    }


    /**
     * Return the size take by array content (without header) in memory.
     * Determined by arrayLength L and array scale S
     *
     * @param array
     * @return L*S
     */
    public static int getArraySizeContentInMem(Object array) {
        return arrayIndexScale(array) * getArrayLength(array);
    }

    /**
     * Return the scale (size of each element in memory) of an array
     *
     * @param array
     * @return
     */
    public static int arrayIndexScale(Object array) {
        Class clazz = array.getClass();
        // Use cached value from unsafe is much faster than use method unsafe.arrayIndexScale
        if (clazz.equals(boolean[].class)) {
            return unsafe.ARRAY_BOOLEAN_INDEX_SCALE;
        }
        if (clazz.equals(char[].class)) {
            return unsafe.ARRAY_CHAR_INDEX_SCALE;
        }
        if (clazz.equals(byte[].class)) {
            return unsafe.ARRAY_BYTE_INDEX_SCALE;
        }
        if (clazz.equals(short[].class)) {
            return unsafe.ARRAY_SHORT_INDEX_SCALE;
        }
        if (clazz.equals(int[].class)) {
            return unsafe.ARRAY_INT_INDEX_SCALE;
        }
        if (clazz.equals(long[].class)) {
            return unsafe.ARRAY_LONG_INDEX_SCALE;
        }
        if (clazz.equals(float[].class)) {
            return unsafe.ARRAY_FLOAT_INDEX_SCALE;
        }
        if (clazz.equals(double[].class)) {
            return unsafe.ARRAY_DOUBLE_INDEX_SCALE;
        }

        return unsafe.ARRAY_OBJECT_INDEX_SCALE;

    }
}