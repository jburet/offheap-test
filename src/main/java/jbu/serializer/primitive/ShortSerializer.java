package jbu.serializer.primitive;

import jbu.serializer.Serializer;
import jbu.serializer.SerializerSink;
import jbu.serializer.SerializerSource;

public class ShortSerializer implements Serializer<Short> {

    @Override
    public void serialize(Short bool, SerializerSink serializerSink) {
        serializerSink.writeShort(bool);
    }

    @Override
    public Short deserialize(Class<Short> clazz, SerializerSource serializerSource) {
        return serializerSource.readShort();
    }
}
