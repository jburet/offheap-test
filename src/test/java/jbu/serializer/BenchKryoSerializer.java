package jbu.serializer;

import jbu.exception.CannotDeserializeException;
import jbu.offheap.Allocator;
import jbu.offheap.LoadContext;
import jbu.offheap.StoreContext;
import jbu.serializer.kryo.KryoSerializer;
import jbu.testobject.LotOfPrimitiveAndArrayAndString;
import org.junit.Test;

public class BenchKryoSerializer {

    @Test
    public void bench_alloc_ser_deser_simple_bean_witharray() {
        Allocator a = new Allocator(500 * 1024 * 1024);
        int NB_MSG_WRITE = 10000000;
        Serializer pbs = new KryoSerializer();
        LotOfPrimitiveAndArrayAndString c = new LotOfPrimitiveAndArrayAndString();
        LotOfPrimitiveAndArrayAndString res;
        int serSize = pbs.calculateSerializedSize(c);
        long addr = a.alloc(1024);
        StoreContext sc = a.getStoreContext(addr);
        LoadContext lc = a.getLoadContext(addr);
        long start = System.nanoTime();
        for (int i = 0; i < NB_MSG_WRITE; i++) {
            pbs.serialize(c, sc);
            try {
                res = (LotOfPrimitiveAndArrayAndString) pbs.deserialize(lc);
            } catch (CannotDeserializeException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            sc.reuse();
            lc.reset();
        }
        long time = System.nanoTime() - start;
        System.out.println("Write End in " + time / 1000 / 1000 + " ms");
        System.out.println("Write Throughput " + ((double) (serSize * NB_MSG_WRITE / 1024 / 1024)) / ((double) time / 1000d / 1000d / 1000d) + " MB/s");
        System.out.println("Allocated memory : " + a.getAllocatedMemory() / 1024 / 1024 + " MB");
        System.out.println("Used memory : " + a.getUsedMemory() / 1024 / 1024 + " MB");
        System.out.println("Allocations : " + a.getNbAllocation());
    }

}
