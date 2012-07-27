package jbu.serializer;

import jbu.exception.CannotDeserializeException;
import jbu.offheap.Allocator;
import jbu.offheap.LoadContext;
import jbu.offheap.StoreContext;
import jbu.serializer.unsafe.UnsafePrimitiveBeanSerializer;
import jbu.testobject.LotOfInt;
import jbu.testobject.LotOfPrimitive;
import jbu.testobject.LotOfPrimitiveAndArrayAndString;
import org.junit.Test;

public class BenchUnsafePrimitiveBeanSerializer {

    @Test
    public void bench_ser_simple_int_bean() {
        Allocator a = new Allocator(50 * 1024 * 1024);
        int NB_MSG_WRITE = 10000000;
        Serializer pbs = new UnsafePrimitiveBeanSerializer();
        StoreContext sc = a.getStoreContext(a.alloc(9 * 4));
        LotOfInt c = new LotOfInt(42);
        long start = System.nanoTime();
        for (int i = 0; i < NB_MSG_WRITE; i++) {
            pbs.serialize(c, sc);
            // Free store context
            sc.reuse();
        }
        long time = System.nanoTime() - start;
        System.out.println("Write End in " + time / 1000 / 1000 + " ms");
        System.out.println("Write Throughput " + ((double) (9 * 4 * NB_MSG_WRITE / 1024 / 1024)) / ((double) time / 1000d / 1000d / 1000d) + " MB/s");
        System.out.println("Allocated memory : " + a.getAllocatedMemory() / 1024 / 1024 + " MB");
        System.out.println("Used memory : " + a.getUsedMemory() / 1024 / 1024 + " MB");
        System.out.println("Allocations : " + a.getNbAllocation());

    }

    @Test
    public void bench_deser_simple_int_bean() throws CannotDeserializeException {
        Allocator a = new Allocator(1 * 1024 * 1024);
        Serializer pbs = new UnsafePrimitiveBeanSerializer();
        long addr = a.alloc(9 * 4);
        // Serialize
        StoreContext sc = a.getStoreContext(addr);
        LotOfInt c = new LotOfInt(10);
        pbs.serialize(c, sc);

        // Deser
        LoadContext lc = a.getLoadContext(addr);
        LotOfInt res = new LotOfInt(0);

        long start = System.nanoTime();
        int NB_MSG_READ = 10000000;
        for (int i = 0; i < NB_MSG_READ; i++) {
            pbs.deserialize(lc);
            // Free load context
            lc.reset();
        }
        long time = System.nanoTime() - start;
        System.out.println("Read End in " + time / 1000 / 1000 + " ms");
        System.out.println("Read Throughput " + ((double) (9 * 4 * NB_MSG_READ / 1024 / 1024)) / ((double) time / 1000d / 1000d / 1000d) + " MB/s");
        System.out.println("Allocated memory : " + a.getAllocatedMemory() / 1024 / 1024 + " MB");
        System.out.println("Used memory : " + a.getUsedMemory() / 1024 / 1024 + " MB");
        System.out.println("Allocations : " + a.getNbAllocation());
    }


    @Test
    public void bench_ser_simple_bean() {
        Allocator a = new Allocator(50 * 1024 * 1024);
        int NB_MSG_WRITE = 1000000;
        Serializer pbs = new UnsafePrimitiveBeanSerializer();
        LotOfPrimitive c = new LotOfPrimitive();
        int serSize = pbs.calculateSerializedSize(c);
        System.out.println("Serialized size: " + serSize);
        StoreContext sc = a.getStoreContext(a.alloc(serSize));

        long start = System.nanoTime();
        for (int i = 0; i < NB_MSG_WRITE; i++) {
            pbs.serialize(c, sc);
            // Free store context
            sc.reuse();
        }
        long time = System.nanoTime() - start;
        System.out.println("Write End in " + time / 1000 / 1000 + " ms");
        System.out.println("Write Throughput " + ((double) (serSize * NB_MSG_WRITE / 1024 / 1024)) / ((double) time / 1000d / 1000d / 1000d) + " MB/s");
        System.out.println("Allocated memory : " + a.getAllocatedMemory() / 1024 / 1024 + " MB");
        System.out.println("Used memory : " + a.getUsedMemory() / 1024 / 1024 + " MB");
        System.out.println("Allocations : " + a.getNbAllocation());
    }

    @Test
    public void bench_deser_simple_bean() throws CannotDeserializeException {
        Allocator a = new Allocator(1 * 1024 * 1024);
        Serializer pbs = new UnsafePrimitiveBeanSerializer();
        LotOfPrimitive c = new LotOfPrimitive();
        int serSize = pbs.calculateSerializedSize(c);
        long addr = a.alloc(serSize);
        // Serialize
        StoreContext sc = a.getStoreContext(addr);

        pbs.serialize(c, sc);

        // Deser
        LoadContext lc = a.getLoadContext(addr);
        LotOfPrimitive res = new LotOfPrimitive();

        long start = System.nanoTime();
        int NB_MSG_READ = 1000000;
        for (int i = 0; i < NB_MSG_READ; i++) {
            pbs.deserialize(lc);
            // Free load context
            lc.reset();
        }
        long time = System.nanoTime() - start;
        System.out.println("Read End in " + time / 1000 / 1000 + " ms");
        System.out.println("Read Throughput " + ((double) (serSize * NB_MSG_READ / 1024 / 1024)) / ((double) time / 1000d / 1000d / 1000d) + " MB/s");
        System.out.println("Allocated memory : " + a.getAllocatedMemory() / 1024 / 1024 + " MB");
        System.out.println("Used memory : " + a.getUsedMemory() / 1024 / 1024 + " MB");
        System.out.println("Allocations : " + a.getNbAllocation());
    }

    @Test
    public void bench_alloc_ser_deser_simple_bean() throws CannotDeserializeException {
        Allocator a = new Allocator(500 * 1024 * 1024);
        int NB_MSG_WRITE = 1000000;
        Serializer pbs = new UnsafePrimitiveBeanSerializer();
        LotOfPrimitive c = new LotOfPrimitive();
        int serSize = pbs.calculateSerializedSize(c);
        long start = System.nanoTime();
        for (int i = 0; i < NB_MSG_WRITE; i++) {
            int intSerSize = pbs.calculateSerializedSize(c);
            long addr = a.alloc(intSerSize);
            StoreContext sc = a.getStoreContext(addr);
            pbs.serialize(c, sc);
            LotOfPrimitive res = new LotOfPrimitive();
            LoadContext lc = a.getLoadContext(addr);
            pbs.deserialize(lc);
        }
        long time = System.nanoTime() - start;
        System.out.println("Write End in " + time / 1000 / 1000 + " ms");
        System.out.println("Write Throughput " + ((double) (serSize * NB_MSG_WRITE / 1024 / 1024)) / ((double) time / 1000d / 1000d / 1000d) + " MB/s");
        System.out.println("Allocated memory : " + a.getAllocatedMemory() / 1024 / 1024 + " MB");
        System.out.println("Used memory : " + a.getUsedMemory() / 1024 / 1024 + " MB");
        System.out.println("Allocations : " + a.getNbAllocation());
    }

    @Test
    public void bench_alloc_ser_deser_simple_bean_inf() throws CannotDeserializeException {
        Allocator a = new Allocator(500 * 1024 * 1024);
        int NB_MSG_WRITE = 1000000;
        Serializer pbs = new UnsafePrimitiveBeanSerializer();
        LotOfPrimitive c = new LotOfPrimitive();
        LotOfPrimitive res = new LotOfPrimitive();
        int serSize = pbs.calculateSerializedSize(c);
        int intSerSize = pbs.calculateSerializedSize(c);
        long addr = a.alloc(intSerSize);
        StoreContext sc = a.getStoreContext(addr);
        LoadContext lc = a.getLoadContext(addr);
        while (true) {
            pbs.serialize(c, sc);
            pbs.deserialize(lc);
            sc.reuse();
            lc.reset();
        }

    }

    @Test
    public void bench_alloc_ser_deser_simple_bean_witharray() throws CannotDeserializeException {
        Allocator a = new Allocator(500 * 1024 * 1024);
        int NB_MSG_WRITE = 1000000;
        Serializer pbs = new UnsafePrimitiveBeanSerializer();
        LotOfPrimitiveAndArrayAndString c = new LotOfPrimitiveAndArrayAndString();
        LotOfPrimitiveAndArrayAndString res = new LotOfPrimitiveAndArrayAndString();
        int serSize = pbs.calculateSerializedSize(c);
        long addr = a.alloc(1024);
        StoreContext sc = a.getStoreContext(addr);
        LoadContext lc = a.getLoadContext(addr);
        long start = System.nanoTime();
        for (int i = 0; i < NB_MSG_WRITE; i++) {
            pbs.serialize(c, sc);
            pbs.deserialize(lc);
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
