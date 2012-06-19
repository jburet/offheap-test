package jbu.serializer;

public interface Serializer<T> {

    void serialize(T obj, SerializerSink serializerSink);

    T deserialize(Class<T> clazz, SerializerSource serializerSource);

}
