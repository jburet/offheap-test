package jbu.serializer;

import jbu.offheap.Allocator;
import org.junit.Test;

public class TestUnsafePrimitiveBeanSerializer {

    @Test
    public void test_ser_deser_simple_int_bean() {
        Allocator a = new Allocator(1 * 1024 * 1024);
        UnsafePrimitiveBeanSerializer pbs = new UnsafePrimitiveBeanSerializer();
        long addr = a.alloc(9 * 4);
        // Serialize
        Allocator.StoreContext sc = a.getStoreContext(addr);
        LotOfInt c = new LotOfInt(10);
        pbs.serialize(c, sc);

        // Deser
        Allocator.LoadContext lc = a.getLoadContext(addr);
        LotOfInt res = new LotOfInt(0);
        pbs.deserialize(res, lc);
        System.out.println(res);
    }

    @Test
    public void test_ser_deser_simple_bean() {
        Allocator a = new Allocator(1 * 1024 * 1024);
        UnsafePrimitiveBeanSerializer pbs = new UnsafePrimitiveBeanSerializer();
        LotOfPrimitive c = new LotOfPrimitive();
        int serSize = pbs.estimateSize(c);
        long addr = a.alloc(serSize);
        // Serialize
        Allocator.StoreContext sc = a.getStoreContext(addr);

        pbs.serialize(c, sc);

        // Deser
        Allocator.LoadContext lc = a.getLoadContext(addr);
        LotOfPrimitive res = new LotOfPrimitive(true);
        pbs.deserialize(res, lc);
        System.out.println(res);
    }

    @Test
    public void test_ser_deser_simple_boolean_bean() {
        Allocator a = new Allocator(1 * 1024 * 1024);
        UnsafePrimitiveBeanSerializer pbs = new UnsafePrimitiveBeanSerializer();
        LotOfBoolean c = new LotOfBoolean(true);
        int serSize = pbs.estimateSize(c);
        long addr = a.alloc(serSize);
        // Serialize
        Allocator.StoreContext sc = a.getStoreContext(addr);

        pbs.serialize(c, sc);

        // Deser
        Allocator.LoadContext lc = a.getLoadContext(addr);
        LotOfBoolean res = new LotOfBoolean(false);
        pbs.deserialize(res, lc);
        System.out.println(res);
    }

    @Test
    public void test_ser_deser_simple_char_bean() {
        Allocator a = new Allocator(1 * 1024 * 1024);
        UnsafePrimitiveBeanSerializer pbs = new UnsafePrimitiveBeanSerializer();
        LotOfChar c = new LotOfChar('a');
        int serSize = pbs.estimateSize(c);
        long addr = a.alloc(serSize);
        // Serialize
        Allocator.StoreContext sc = a.getStoreContext(addr);

        pbs.serialize(c, sc);

        // Deser
        Allocator.LoadContext lc = a.getLoadContext(addr);
        LotOfChar res = new LotOfChar('b');
        pbs.deserialize(res, lc);
        System.out.println(res);
    }

    @Test
    public void test_ser_deser_simple_few_double_bean() {
        Allocator a = new Allocator(1 * 1024 * 1024);
        UnsafePrimitiveBeanSerializer pbs = new UnsafePrimitiveBeanSerializer();
        SomeDouble c = new SomeDouble(1);
        int serSize = pbs.estimateSize(c);
        long addr = a.alloc(serSize);
        // Serialize
        Allocator.StoreContext sc = a.getStoreContext(addr);

        pbs.serialize(c, sc);

        // Deser
        Allocator.LoadContext lc = a.getLoadContext(addr);
        SomeDouble res = new SomeDouble(45);
        pbs.deserialize(res, lc);
        System.out.println(res);
    }

    @Test
    public void test_ser_deser_simple_double_bean() {
        Allocator a = new Allocator(1 * 1024 * 1024);
        UnsafePrimitiveBeanSerializer pbs = new UnsafePrimitiveBeanSerializer();
        LotOfDouble c = new LotOfDouble(1);
        int serSize = pbs.estimateSize(c);
        long addr = a.alloc(serSize);
        // Serialize
        Allocator.StoreContext sc = a.getStoreContext(addr);

        pbs.serialize(c, sc);

        // Deser
        Allocator.LoadContext lc = a.getLoadContext(addr);
        LotOfDouble res = new LotOfDouble(45);
        pbs.deserialize(res, lc);
        System.out.println(res);
    }

    @Test
    public void test_ser_deser_simple_array_primitive_bean() {
        Allocator a = new Allocator(1 * 1024 * 1024);
        UnsafePrimitiveBeanSerializer pbs = new UnsafePrimitiveBeanSerializer();
        long addr = a.alloc(16);
        // Serialize
        Allocator.StoreContext sc = a.getStoreContext(addr);
        ArrayPrimitive c = new ArrayPrimitive(new int[]{1, 2, 3, 4, 5, 6, 7});
        pbs.serialize(c, sc);

        // Deser
        Allocator.LoadContext lc = a.getLoadContext(addr);
        ArrayPrimitive res = new ArrayPrimitive(new int[]{0, 0, 0, 0, 0, 0, 0});
        pbs.deserialize(res, lc);
        for(int i : res.a){
            System.out.println(i);
        }
        // estimated size
        int serSize = pbs.estimateSize(c);
    }

}
