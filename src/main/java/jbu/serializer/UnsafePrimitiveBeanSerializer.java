package jbu.serializer;


import jbu.offheap.Allocator;
import jbu.offheap.Primitive;
import jbu.offheap.UnsafeUtil;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.*;

public class UnsafePrimitiveBeanSerializer {

    private static final Map<Class, ClassDesc> registeredClassOffset = new HashMap<Class, ClassDesc>();

    private static final Unsafe unsafe = UnsafeUtil.unsafe;

    public static ClassDesc registerClass(Class c) {
        Field[] fields = c.getDeclaredFields();
        long[] offsets = new long[fields.length];
        int[] types = new int[fields.length];
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            f.setAccessible(true);
            offsets[i] = unsafe.objectFieldOffset(f);
            types[i] = ClassDesc.resolveType(f);
        }
        registeredClassOffset.put(c, new ClassDesc(types, offsets));
        return registeredClassOffset.get(c);
    }

    public void serialize(Object obj, Allocator.StoreContext sc) {
        Class clazz = obj.getClass();
        ClassDesc cd = null;
        if ((cd = registeredClassOffset.get(clazz)) == null) {
            cd = registerClass(clazz);
        }
        for (int i = 0; i < cd.nbFields; i++) {
            if (cd.types[i] == ClassDesc.BOOLEAN) {
                sc.storeBoolean(obj, cd.offsets[i]);
            } else if (cd.types[i] == ClassDesc.CHAR) {
                sc.storeChar(obj, cd.offsets[i]);
            } else if (cd.types[i] == ClassDesc.BYTE) {
                sc.storeByte(obj, cd.offsets[i]);
            } else if (cd.types[i] == ClassDesc.SHORT) {
                sc.storeShort(obj, cd.offsets[i]);
            } else if (cd.types[i] == ClassDesc.INT) {
                sc.storeInt(obj, cd.offsets[i]);
            } else if (cd.types[i] == ClassDesc.LONG) {
                sc.storeLong(obj, cd.offsets[i]);
            } else if (cd.types[i] == ClassDesc.FLOAT) {
                sc.storeFloat(obj, cd.offsets[i]);
            } else if (cd.types[i] == ClassDesc.DOUBLE) {
                sc.storeDouble(obj, cd.offsets[i]);
            }
        }
    }

    public void deserialize(Object obj, Allocator.LoadContext lc) {
        Class clazz = obj.getClass();
        ClassDesc cd = null;
        if ((cd = registeredClassOffset.get(clazz)) == null) {
            cd = registerClass(clazz);
        }
        for (int i = 0; i < cd.nbFields; i++) {
            if (cd.types[i] == ClassDesc.BOOLEAN) {
                //lc.loadInt(obj, cd.offsets[i]);
            } else if (cd.types[i] == ClassDesc.CHAR) {
                //lc.loadChar(obj, cd.offsets[i]);
            } else if (cd.types[i] == ClassDesc.BYTE) {
                //lc.loadByte(obj, cd.offsets[i]);
            } else if (cd.types[i] == ClassDesc.SHORT) {
                //lc.loadShort(obj, cd.offsets[i]);
            } else if (cd.types[i] == ClassDesc.INT) {
                lc.loadInt(obj, cd.offsets[i]);
            } else if (cd.types[i] == ClassDesc.LONG) {
                //lc.loadLong(obj, cd.offsets[i]);
            } else if (cd.types[i] == ClassDesc.FLOAT) {
                //lc.loadFloat(obj, cd.offsets[i]);
            } else if (cd.types[i] == ClassDesc.DOUBLE) {
                //lc.loadDouble(obj, cd.offsets[i]);
            }
        }
    }

    public int estimateSize(Object obj) {
        Class clazz = obj.getClass();
        ClassDesc cd = null;
        if ((cd = registeredClassOffset.get(clazz)) == null) {
            cd = registerClass(clazz);
        }
        int size = 0;
        for (int i = 0; i < cd.nbFields; i++) {
            size += getFieldSize(cd.types[i], obj);
        }
        return size;
    }

    private int getFieldSize(int type, Object obj) {
        if (type == ClassDesc.BOOLEAN) {
            return Primitive.BOOLEAN_SIZE;
        } else if (type == ClassDesc.CHAR) {
            return Primitive.CHAR_SIZE;
        } else if (type == ClassDesc.BYTE) {
            return Primitive.BYTE_SIZE;
        } else if (type == ClassDesc.SHORT) {
            return Primitive.SHORT_SIZE;
        } else if (type == ClassDesc.INT) {
            return Primitive.INT_SIZE;
        } else if (type == ClassDesc.LONG) {
            return Primitive.LONG_SIZE;
        } else if (type == ClassDesc.FLOAT) {
            return Primitive.FLOAT_SIZE;
        } else if (type == ClassDesc.LONG) {
            return Primitive.LONG_SIZE;
        }
        return 0;
    }
}
