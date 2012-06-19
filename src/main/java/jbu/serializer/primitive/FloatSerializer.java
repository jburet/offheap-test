package jbu.serializer.primitive;

import jbu.serializer.Serializer;
import jbu.serializer.SerializerSink;
import jbu.serializer.SerializerSource;

public class FloatSerializer implements Serializer<Float> {

    @Override
    public void serialize(Float bool, SerializerSink serializerSink) {
        serializerSink.writeFloat(bool);
    }

    @Override
    public Float deserialize(Class<Float> clazz, SerializerSource serializerSource) {
        return serializerSource.readFloat();
    }
}
