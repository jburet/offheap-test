package jbu.serializer;

import jbu.offheap.Allocator;
import org.junit.Test;

import java.nio.ByteBuffer;

public class BenchUnsafePrimitiveBeanSerializer {

    @Test
    public void bench_ser_deser_simple_bean() {
        Allocator a = new Allocator(50 * 1024 * 1024, true);


        int NB_MSG_WRITE = 10000000;
        UnsafePrimitiveBeanSerializer pbs = new UnsafePrimitiveBeanSerializer(a.getStoreContext(a.alloc(9 * 4), 9 * 4));
        LotOfInt c = new LotOfInt();
        long start = System.nanoTime();
        for (int i = 0; i < NB_MSG_WRITE; i++) {
            pbs.serialize(c);
        }
        long time = System.nanoTime() - start;
        System.out.println("Write End in " + time / 1000 / 1000 + " ms");
        System.out.println("Write Throughput " + ((double) (9 * 4 * NB_MSG_WRITE / 1024 / 1024)) / ((double) time / 1000d / 1000d / 1000d) + " MB/s");
        System.out.println("Allocated memory : " + a.getAllocatedMemory() / 1024 / 1024 + " MB");
        System.out.println("Used memory : " + a.getUsedMemory() / 1024 / 1024 + " MB");
        System.out.println("Allocations : " + a.getNbAllocation());

    }


}
