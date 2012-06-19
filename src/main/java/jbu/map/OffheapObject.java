package jbu.map;

import java.util.Map;

/**
 *
 */
public class OffheapObject<T> {

    private static Map<Class, Serializer> serializers;

    public static <T> OffheapObject fromObject(T object) {
        return new OffheapObject(0, 0);
    }

    public static void registerSerializer(Class clazz, Serializer serializer) {
        OffheapObject.serializers.put(clazz, serializer);
    }

    public final long addr;
    public final int serializedSize;

    private OffheapObject(long addr, int serializedSize) {
        this.addr = addr;
        this.serializedSize = serializedSize;
    }

    public T getObject() {
        return null;
    }


}
