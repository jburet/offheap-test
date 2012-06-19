package jbu.serializer.primitive;

import jbu.serializer.Serializer;
import jbu.serializer.SerializerSink;
import jbu.serializer.SerializerSource;

public class StringSerializer implements Serializer<String> {

    @Override
    public void serialize(String bool, SerializerSink serializerSink) {
        serializerSink.writeString(bool);
    }

    @Override
    public String deserialize(Class<String> clazz, SerializerSource serializerSource) {
        return serializerSource.readString();
    }
}
