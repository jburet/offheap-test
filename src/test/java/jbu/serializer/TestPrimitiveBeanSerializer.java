package jbu.serializer;

import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;

public class TestPrimitiveBeanSerializer {

    @Test
    public void test_ser_deser_simple_bean_in_bytebuffer() {
        PrimitiveBeanSerializer pbs = new PrimitiveBeanSerializer();
        ByteBuffer bb = ByteBuffer.allocateDirect(1024);
        SerializerByteBuffer sbb = new SerializerByteBuffer(bb);
        Client c = new Client("firstname", "lastName", 12, null);
        pbs.serialize(c, sbb);
        bb.flip();
        Client cres = (Client) pbs.deserialize(Client.class, sbb);
        assertEquals(c, cres);
    }


}
