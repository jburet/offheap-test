package jbu.serializer;

public interface SerializerSource {
    Boolean readBoolean();

    Byte readByte();

    Character readChar();

    Double readDouble();

    Float readFloat();

    Integer readInteger();

    Short readShort();

    Long readLong();

    String readString();
}
