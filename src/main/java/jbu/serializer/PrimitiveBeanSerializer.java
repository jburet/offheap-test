package jbu.serializer;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Serialize only primitive (boolean, short, int, ... , String) from the bean
 * This serializer must be used for serialize row loaded from database
 * CANNOT BE USED FOR PERSISTENT STORAGE
 * CANNOT SHARED SERIALIZED FORM BETWEEN TWO JVM
 */
public class PrimitiveBeanSerializer implements Serializer<Object> {

    private static final Map<Class, Map<Field, Serializer>> registeredClass = new HashMap<Class, Map<Field, Serializer>>();

    public static void registerClass(Class c) {
        Field[] fields = c.getDeclaredFields();
        Map<Field, Serializer> sers = new TreeMap<Field, Serializer>(new FieldComparator());
        for (Field f : fields) {
            f.setAccessible(true);
            Serializer s = SerFactory.getSerializer(f.getType());
            if (s != null) {
                sers.put(f, s);
            }
        }
        registeredClass.put(c, sers);
    }

    @Override
    public void serialize(Object obj, SerializerSink serializerSink) {
        Map<Field, Serializer> fields;
        if ((fields = registeredClass.get(obj.getClass())) == null) {
            PrimitiveBeanSerializer.registerClass(obj.getClass());
            fields = registeredClass.get(obj.getClass());
        }
        for (Map.Entry<Field, Serializer> ent : fields.entrySet()) {
            if (ent.getKey().getType().equals(int.class)) {
                ent.getValue().serialize(UnsafeReflection.getInt(ent.getKey(), obj), serializerSink);
            } else {
                ent.getValue().serialize(UnsafeReflection.getObject(ent.getKey(), obj), serializerSink);
            }
            /*
            try {
                ent.getValue().serialize(ent.getKey().get(obj), serializerSink);
            } catch (IllegalAccessException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }*/
        }
    }

    @Override
    public Object deserialize(Class clazz, SerializerSource serializerSource) {
        Map<Field, Serializer> fields;
        if ((fields = registeredClass.get(clazz)) == null) {
            PrimitiveBeanSerializer.registerClass(clazz);
            fields = registeredClass.get(clazz);
        }
        Object res = null;
        try {
            res = clazz.newInstance();
            for (Map.Entry<Field, Serializer> ent : fields.entrySet()) {
                ent.getKey().set(res, ent.getValue().deserialize(ent.getKey().getClass(), serializerSource));
            }
        } catch (InstantiationException e) {
            // Never append... in ideal world
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            // Never append... in ideal world
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return res;
    }
}

class FieldComparator implements Comparator<Field> {

    @Override
    public int compare(Field f1, Field f2) {
        return f1.getName().compareTo(f2.getName());
    }
}