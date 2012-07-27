package jbu;


import jbu.exception.InvalidJvmException;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

public final class UnsafeUtil {

    private UnsafeUtil() {
    }

    private static Unsafe getUnsafeInstance() {
        try {
            Field theUnsafeInstance = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafeInstance.setAccessible(true);
            return (Unsafe) theUnsafeInstance.get(Unsafe.class);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new InvalidJvmException("Cannot get UNSAFE reference", e);
        }
    }

    public static final Unsafe unsafe = getUnsafeInstance();
}
