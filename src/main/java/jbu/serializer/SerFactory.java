package jbu.serializer;

import jbu.serializer.primitive.*;

import java.util.HashMap;
import java.util.Map;

public class SerFactory {

    static {
        sers = new HashMap<Class, Serializer>();
        // Std serializer

        registerSerializer(Boolean.class, new BooleanSerializer());
        registerSerializer(boolean.class, new BooleanSerializer());
        registerSerializer(Byte.class, new ByteSerializer());
        registerSerializer(byte.class, new ByteSerializer());
        registerSerializer(Character.class, new CharSerializer());
        registerSerializer(char.class, new CharSerializer());
        registerSerializer(Short.class, new ShortSerializer());
        registerSerializer(short.class, new ShortSerializer());
        registerSerializer(Integer.class, new IntegerSerializer());
        registerSerializer(int.class, new IntegerSerializer());
        registerSerializer(Long.class, new LongSerializer());
        registerSerializer(long.class, new LongSerializer());
        registerSerializer(Float.class, new FloatSerializer());
        registerSerializer(float.class, new FloatSerializer());
        registerSerializer(Double.class, new DoubleSerializer());
        registerSerializer(double.class, new DoubleSerializer());
        registerSerializer(String.class, new StringSerializer());

    }

    private static Map<Class, Serializer> sers;

    public static void registerSerializer(Class clazz, Serializer ser) {
        sers.put(clazz, ser);
    }

    public static Serializer getSerializer(Class<?> type) {
        return sers.get(type);
    }
}
