package jbu.serializer.primitive;

import jbu.serializer.Serializer;
import jbu.serializer.SerializerSink;
import jbu.serializer.SerializerSource;

public class DoubleSerializer implements Serializer<Double> {

    @Override
    public void serialize(Double bool, SerializerSink serializerSink) {
        serializerSink.writeDouble(bool);
    }

    @Override
    public Double deserialize(Class<Double> clazz, SerializerSource serializerSource) {
        return serializerSource.readDouble();
    }

}
