package jbu.serializer;

public interface SerializerSink {
    void writeBoolean(Boolean bool);

    void writeByte(Byte b);

    void writeChar(Character c);

    void writeDouble(Double d);

    void writeFloat(Float f);

    void writeInteger(Integer i);

    void writeShort(Short s);

    void writeLong(Long l);

    void writeString(String s);

}
