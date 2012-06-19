package jbu.serializer;

import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;

public class BenchPrimitiveBeanSerializer {

    @Test
    public void bench_ser_deser_simple_bean_in_bytebuffer() {
        PrimitiveBeanSerializer pbs = new PrimitiveBeanSerializer();
        ByteBuffer bb = ByteBuffer.allocateDirect(1024);
        Client c = new Client("firstname", "lastName", 12, null);

        SerializerByteBuffer sbb = new SerializerByteBuffer(bb);
        long start = System.nanoTime();
        int NB_MSG_WRITE = 10000000;
        for (int i = 0; i < NB_MSG_WRITE; i++) {
            pbs.serialize(c, sbb);
            bb.flip();
        }
        System.out.println("Write End in " + (System.nanoTime() - start) / 1000 / 1000 + " ms");
        System.out.println("Write Throughput " + ((double) (21 * NB_MSG_WRITE)) / ((double) ((System.nanoTime() - start) / 1000d / 1000d / 1000d)) / 1024d / 1024d + " MB/s");


        int NB_MSG_READ = 1000000000;
        start = System.nanoTime();
        for (int i = 0; i < NB_MSG_READ; i++) {
            Client cres = (Client) pbs.deserialize(Client.class, sbb);
            bb.flip();
        }
        System.out.println("Read End in " + (System.nanoTime() - start) / 1000 / 1000 + " ms");
        System.out.println("Read Throughput " + ((double) (21 * NB_MSG_WRITE)) / ((double) ((System.nanoTime() - start) / 1000d / 1000d / 1000d)) / 1024d / 1024d + " MB/s");


    }


}
