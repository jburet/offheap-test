package jbu.serializer.unsafe;

import jbu.UnsafeReflection;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

final class ClassDesc {

    private static final AtomicInteger classCounter = new AtomicInteger(0);
    private static final Map<Class, ClassDesc> classDescRefs = new HashMap<Class, ClassDesc>();
    private static final Map<Integer, ClassDesc> classDescRefsByInt = new HashMap<Integer, ClassDesc>();

    final Type[] types;
    final long[] offsets;
    final Field[] fields;
    final int nbFields;
    final int classReference;
    final Class<?> clazz;

    // FIXME must be correctly synchronized
    static ClassDesc resolveByClass(Class<?> clazz) {
        if (!classDescRefs.containsKey(clazz)) {
            registerClass(clazz);
        }
        return classDescRefs.get(clazz);
    }

    // FIXME manage class not exists
    static ClassDesc resolveByRef(Integer ref) {
        return classDescRefsByInt.get(ref);
    }

    private ClassDesc(final Type[] types, final long[] offsets, final Field[] fields, final Class<?> clazz) {
        // Verify same length
        if (types.length != offsets.length) {
            // FIXME caracterize exception
            throw new IllegalArgumentException("Error in offsets or types. Length are different");
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
            offsets[i] = UnsafeReflection.getOffset(f);
            types[i] = Type.resolveType(f);
        }
        ClassDesc cd = new ClassDesc(types, offsets, fields, c);
        classDescRefs.put(c, cd);
        classDescRefsByInt.put(cd.classReference, cd);
        return classDescRefs.get(c);
    }
}
