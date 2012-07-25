package jbu.serializer.unsafe;

import jbu.offheap.UnsafeUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ClassDesc {

    private final static AtomicInteger classCounter = new AtomicInteger(0);
    private final static Map<Class, ClassDesc> classDescRefs = new HashMap<Class, ClassDesc>();
    private final static Map<Integer, ClassDesc> classDescRefsByInt = new HashMap<Integer, ClassDesc>();

    public final Type[] types;
    public final long[] offsets;
    public final Field[] fields;
    public final int nbFields;
    public final int classReference;
    public final Class<?> clazz;

    // FIXME must be correctly synchronized
    public static ClassDesc resolveByClass(Class<?> clazz) {
        if (!classDescRefs.containsKey(clazz)) {
            registerClass(clazz);
        }
        return classDescRefs.get(clazz);
    }

    // FIXME manage class not exists
    public static ClassDesc resolveByRef(Integer ref) {
        return classDescRefsByInt.get(ref);
    }

    private ClassDesc(Type[] types, long[] offsets, Field[] fields, Class<?> clazz) {
        // Verify same length
        if (types.length != offsets.length) {
            // FIXME caracterize exception
            throw new RuntimeException("Error in offsets or types. Length are different");
        }
        this.nbFields = types.length;
        this.types = types;
        this.offsets = offsets;
        this.fields = fields;
        this.classReference = classCounter.incrementAndGet();
        this.clazz = clazz;
    }

    private static ClassDesc registerClass(Class c) {
        Field[] fields = c.getDeclaredFields();
        long[] offsets = new long[fields.length];
        Type[] types = new Type[fields.length];
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            f.setAccessible(true);
            offsets[i] = UnsafeUtil.unsafe.objectFieldOffset(f);
            types[i] = Type.resolveType(f);
        }
        ClassDesc cd = new ClassDesc(types, offsets, fields, c);
        classDescRefs.put(c, cd);
        classDescRefsByInt.put(cd.classReference, cd);
        return classDescRefs.get(c);
    }
}
