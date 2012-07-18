package jbu.serializer;

import jbu.offheap.Allocator;
import org.junit.Test;

public class TestUnsafePrimitiveBeanSerializer {

    @Test
    public void test_ser_deser_simple_int_bean() {
        Allocator a = new Allocator(1 * 1024 * 1024, true);
        UnsafePrimitiveBeanSerializer pbs = new UnsafePrimitiveBeanSerializer();
        long addr = a.alloc(9 * 4);
        // Serialize
        Allocator.StoreContext sc = a.getStoreContext(addr, 9 * 4);
        LotOfInt c = new LotOfInt(10);
        pbs.serialize(c, sc);

        // Deser
        Allocator.LoadContext lc = a.getLoadContext(addr);
        LotOfInt res = new LotOfInt(0);
        pbs.deserialize(res, lc);
        System.out.println(res);
    }

    @Test
    public void test_ser_deser_simple_array_primitive_bean() {
        Allocator a = new Allocator(1 * 1024 * 1024, true);
        UnsafePrimitiveBeanSerializer pbs = new UnsafePrimitiveBeanSerializer();
        long addr = a.alloc(16);
        // Serialize
        Allocator.StoreContext sc = a.getStoreContext(addr, 16);
        ArrayPrimitive c = new ArrayPrimitive();
        pbs.serialize(c, sc);

        // Deser
        Allocator.LoadContext lc = a.getLoadContext(addr);
        ArrayPrimitive res = new ArrayPrimitive();
        pbs.deserialize(res, lc);
        System.out.println(res);
    }

}
