package jbu.serializer;


import jbu.offheap.Allocator;
import jbu.offheap.UnsafeUtil;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.*;

public class UnsafePrimitiveBeanSerializer {

    private static final Map<Class, Long[]> registeredClass = new HashMap<Class, Long[]>();

    private static final Unsafe unsafe = UnsafeUtil.unsafe;

    public static Long[] registerClass(Class c) {
        Field[] fields = c.getDeclaredFields();
        List<Long> fieldsList = new ArrayList<Long>();
        for (Field f : fields) {
            f.setAccessible(true);
            fieldsList.add(unsafe.objectFieldOffset(f));
        }
        registeredClass.put(c, fieldsList.toArray(new Long[fieldsList.size()]));
        return registeredClass.get(c);
    }

    private Allocator.StoreContext sc;

    public UnsafePrimitiveBeanSerializer(Allocator.StoreContext sc) {
        this.sc = sc;
    }

    public void serialize(Object obj) {
        Class clazz = obj.getClass();
        Long[] fields;
        if ((fields = registeredClass.get(clazz)) == null) {
            fields = registerClass(clazz);
        }
        for (Long off : fields) {
            sc.storeInt(obj, off);
        }
    }

    public Object deserialize(Class<Object> clazz) {
        return null;
    }
}
