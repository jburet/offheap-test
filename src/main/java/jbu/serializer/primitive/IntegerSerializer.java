package jbu.serializer.primitive;

import jbu.serializer.Serializer;
import jbu.serializer.SerializerSink;
import jbu.serializer.SerializerSource;

public class IntegerSerializer implements Serializer<Integer> {

    @Override
    public void serialize(Integer bool, SerializerSink serializerSink) {
        serializerSink.writeInteger(bool);
    }

    @Override
    public Integer deserialize(Class<Integer> clazz, SerializerSource serializerSource) {
        return serializerSource.readInteger();
    }
}
