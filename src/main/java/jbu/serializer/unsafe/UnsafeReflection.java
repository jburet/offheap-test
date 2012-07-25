package jbu.serializer.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Reflection without any kind of check...
 * Using bad type or field with not same class than object class can cause JVM crash....
 */
public class UnsafeReflection {

    private static Map<Field, Long> offsetCache = new HashMap<Field, Long>();
    private static final Unsafe unsafe = getUnsafeInstance();
    private static final int arrayBaseOffset = unsafe.ARRAY_OBJECT_BASE_OFFSET;

    private static Unsafe getUnsafeInstance() {
        try {
            Field theUnsafeInstance = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafeInstance.setAccessible(true);
            return (Unsafe) theUnsafeInstance.get(Unsafe.class);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }


    public static int getInt(Field field, Object instance) {
        long offset = getOffset(field);
        return unsafe.getInt(instance, offset);
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

    public static long getOffset(Field field) {
        Long offset = null;
        if ((offset = offsetCache.get(field)) == null) {
            offset = unsafe.objectFieldOffset(field);
            offsetCache.put(field, offset);
        }
        return offset;
    }

    public static void debugArray(Object array) {
        int scale = arrayIndexScale(array);
        System.out.println("Offset : " + arrayBaseOffset(array));
        System.out.println("scale : " + unsafe.arrayIndexScale(array.getClass()));
        System.out.println("b0 : " + unsafe.getInt(array, 0l));
        System.out.println("b4 : " + unsafe.getInt(array, 4l));
        System.out.println("b8 : " + unsafe.getInt(array, 8l));
        int length = unsafe.getInt(array, 12l);
        System.out.println("length : " + length);
        for (int i = 0; i < length; i++) {
            if (scale == 4) {
                System.out.println("i : " + unsafe.getInt(array, i * 4 + 16l));
            }
            if (scale == 8) {
                System.out.println(i + ": " + unsafe.getLong(array, i * 8 + 16l));
            }
        }

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
        if (abo == 12) {
            lengthOff = 8;
        } else if (abo == 16) {
            lengthOff = 12;
        } else if (abo == 24) {
            lengthOff = 16;
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

    private static int arrayIndexScale(Object array) {
        // Use cached value from unsafe is much faster than use method unsafe.arrayIndexScale
        if (array.getClass().equals(boolean[].class)) {
            return Unsafe.ARRAY_BOOLEAN_INDEX_SCALE;
        }
        if (array.getClass().equals(char[].class)) {
            return Unsafe.ARRAY_CHAR_INDEX_SCALE;
        }
        if (array.getClass().equals(byte[].class)) {
            return Unsafe.ARRAY_BYTE_INDEX_SCALE;
        }
        if (array.getClass().equals(short[].class)) {
            return Unsafe.ARRAY_SHORT_INDEX_SCALE;
        }
        if (array.getClass().equals(int[].class)) {
            return Unsafe.ARRAY_INT_INDEX_SCALE;
        }
        if (array.getClass().equals(long[].class)) {
            return Unsafe.ARRAY_LONG_INDEX_SCALE;
        }
        if (array.getClass().equals(float[].class)) {
            return Unsafe.ARRAY_FLOAT_INDEX_SCALE;
        }
        if (array.getClass().equals(double[].class)) {
            return Unsafe.ARRAY_DOUBLE_INDEX_SCALE;
        }

        return Unsafe.ARRAY_OBJECT_INDEX_SCALE;

    }
}