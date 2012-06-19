package jbu.serializer.primitive;

import jbu.serializer.Serializer;
import jbu.serializer.SerializerSink;
import jbu.serializer.SerializerSource;

public class CharSerializer implements Serializer<Character> {

    @Override
    public void serialize(Character bool, SerializerSink serializerSink) {
        serializerSink.writeChar(bool);
    }

    @Override
    public Character deserialize(Class<Character> clazz, SerializerSource serializerSource) {
        return serializerSource.readChar();
    }
}
