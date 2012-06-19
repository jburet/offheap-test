package jbu.serializer.primitive;

import jbu.serializer.Serializer;
import jbu.serializer.SerializerSink;
import jbu.serializer.SerializerSource;

public class BooleanSerializer implements Serializer<Boolean> {

    @Override
    public void serialize(Boolean bool, SerializerSink serializerSink) {
        serializerSink.writeBoolean(bool);
    }

    @Override
    public Boolean deserialize(Class<Boolean> clazz, SerializerSource serializerSource) {
        return serializerSource.readBoolean();
    }
}
