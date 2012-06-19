package jbu.serializer.primitive;

import jbu.serializer.Serializer;
import jbu.serializer.SerializerSink;
import jbu.serializer.SerializerSource;

public class ByteSerializer implements Serializer<Byte> {

    @Override
    public void serialize(Byte b, SerializerSink serializerSink) {
        serializerSink.writeByte(b);
    }

    @Override
    public Byte deserialize(Class<Byte> clazz, SerializerSource serializerSource) {
        return serializerSource.readByte();
    }
}
