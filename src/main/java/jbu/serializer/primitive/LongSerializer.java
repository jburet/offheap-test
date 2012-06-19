package jbu.serializer.primitive;

import jbu.serializer.Serializer;
import jbu.serializer.SerializerSink;
import jbu.serializer.SerializerSource;

public class LongSerializer implements Serializer<Long> {

    @Override
    public void serialize(Long bool, SerializerSink serializerSink) {
        serializerSink.writeLong(bool);
    }

    @Override
    public Long deserialize(Class<Long> clazz, SerializerSource serializerSource) {
        return serializerSource.readLong();
    }
}
