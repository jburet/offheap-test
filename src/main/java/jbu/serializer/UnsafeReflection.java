package jbu.serializer;

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

    private static final Unsafe unsafe = getUnsafeInstance();

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


    public static int getArrayLength(Object obj, long offset) {
        // FIXME work only in array with dim = 1
        // FIXME JVM and JVM version dependent code... find best solution
        // 3 case
        // 32 bits : size 8 - 12
        // 64 bits : size 16 - 20 + padding
        // 64 bits w pc : size 12 - 16
        Object array = unsafe.getObject(obj, offset);
        int abo = unsafe.arrayBaseOffset(array.getClass());
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


    public static int getArraySizeInMem(Object obj, long offset) {
        Object array = unsafe.getObject(obj, offset);
        int abo = unsafe.arrayBaseOffset(array.getClass());
        int scale = unsafe.arrayIndexScale(array.getClass());
        int al = getArrayLength(obj, offset);
        return abo + scale * al;
    }
}
