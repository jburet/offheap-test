package jbu.serializer;

import jbu.offheap.Allocator;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        Object res = pbs.deserialize(lc);
        assertEquals(c, res);
    }

    @Test
    public void test_ser_deser_simple_bean() {
        Allocator a = new Allocator(1 * 1024 * 1024);
        UnsafePrimitiveBeanSerializer pbs = new UnsafePrimitiveBeanSerializer();
        LotOfPrimitive c = new LotOfPrimitive();
        int serSize = pbs.estimateSerializedSize(c);
        long addr = a.alloc(serSize);
        // Serialize
        Allocator.StoreContext sc = a.getStoreContext(addr);

        pbs.serialize(c, sc);

        // Deser
        Allocator.LoadContext lc = a.getLoadContext(addr);
        Object res = pbs.deserialize(lc);
        assertEquals(c, res);
    }

    @Test
    public void test_ser_deser_simple_boolean_bean() {
        Allocator a = new Allocator(1 * 1024 * 1024);
        UnsafePrimitiveBeanSerializer pbs = new UnsafePrimitiveBeanSerializer();
        LotOfBoolean c = new LotOfBoolean(true);
        int serSize = pbs.estimateSerializedSize(c);
        long addr = a.alloc(serSize);
        // Serialize
        Allocator.StoreContext sc = a.getStoreContext(addr);

        pbs.serialize(c, sc);

        // Deser
        Allocator.LoadContext lc = a.getLoadContext(addr);
        Object res = pbs.deserialize(lc);
        assertEquals(c, res);
    }

    @Test
    public void test_ser_deser_simple_char_bean() {
        Allocator a = new Allocator(1 * 1024 * 1024);
        UnsafePrimitiveBeanSerializer pbs = new UnsafePrimitiveBeanSerializer();
        LotOfChar c = new LotOfChar('a');
        int serSize = pbs.estimateSerializedSize(c);
        long addr = a.alloc(serSize);
        // Serialize
        Allocator.StoreContext sc = a.getStoreContext(addr);

        pbs.serialize(c, sc);

        // Deser
        Allocator.LoadContext lc = a.getLoadContext(addr);
        Object res = pbs.deserialize(lc);
        assertEquals(c, res);
    }

    @Test
    public void test_ser_deser_simple_few_double_bean() {
        Allocator a = new Allocator(1 * 1024 * 1024);
        UnsafePrimitiveBeanSerializer pbs = new UnsafePrimitiveBeanSerializer();
        SomeDouble c = new SomeDouble(1);
        int serSize = pbs.estimateSerializedSize(c);
        long addr = a.alloc(serSize);
        // Serialize
        Allocator.StoreContext sc = a.getStoreContext(addr);

        pbs.serialize(c, sc);

        // Deser
        Allocator.LoadContext lc = a.getLoadContext(addr);
        Object res = pbs.deserialize(lc);
        assertEquals(c, res);
    }

    @Test
    public void test_ser_deser_simple_double_bean() {
        Allocator a = new Allocator(1 * 1024 * 1024);
        UnsafePrimitiveBeanSerializer pbs = new UnsafePrimitiveBeanSerializer();
        LotOfDouble c = new LotOfDouble(1);
        int serSize = pbs.estimateSerializedSize(c);
        long addr = a.alloc(serSize);
        // Serialize
        Allocator.StoreContext sc = a.getStoreContext(addr);

        pbs.serialize(c, sc);

        // Deser
        Allocator.LoadContext lc = a.getLoadContext(addr);
        LotOfDouble res = (LotOfDouble) pbs.deserialize(lc);
        assertEquals(c, res);
    }

    @Test
    public void test_ser_deser_simple_array_int_primitive_bean() {
        Allocator a = new Allocator(1 * 1024 * 1024);
        UnsafePrimitiveBeanSerializer pbs = new UnsafePrimitiveBeanSerializer();
        long addr = a.alloc(64);
        // Serialize
        Allocator.StoreContext sc = a.getStoreContext(addr);
        ArrayIntPrimitive c = new ArrayIntPrimitive(new int[]{2, 2, 2, 2, 2, 2});
        pbs.serialize(c, sc);

        // Deser
        Allocator.LoadContext lc = a.getLoadContext(addr);
        ArrayIntPrimitive res = (ArrayIntPrimitive) pbs.deserialize(lc);
        //for (int i : res.a) {
        //    System.out.println(i);
        //}

        assertTrue(Arrays.equals(c.a, res.a));
        // estimated size
        int serSize = pbs.estimateSerializedSize(c);
    }

    @Test
    public void test_ser_deser_simple_array_boolean_primitive_bean() {
        Allocator a = new Allocator(1 * 1024 * 1024);
        UnsafePrimitiveBeanSerializer pbs = new UnsafePrimitiveBeanSerializer();
        long addr = a.alloc(64);
        // Serialize
        Allocator.StoreContext sc = a.getStoreContext(addr);
        ArrayBooleanPrimitive c = new ArrayBooleanPrimitive(new boolean[]{true, true, true, true, true});
        pbs.serialize(c, sc);

        // Deser
        Allocator.LoadContext lc = a.getLoadContext(addr);
        ArrayBooleanPrimitive res = (ArrayBooleanPrimitive) pbs.deserialize(lc);
        //for (boolean i : res.a) {
        //    System.out.println(i);
        //}
        assertTrue(Arrays.equals(c.a, res.a));
        // estimated size
        int serSize = pbs.estimateSerializedSize(c);
    }


    @Test
    public void test_ser_deser_simple_array_long_primitive_bean() {
        Allocator a = new Allocator(1 * 1024 * 1024);
        UnsafePrimitiveBeanSerializer pbs = new UnsafePrimitiveBeanSerializer();
        ArrayLongPrimitive c = new ArrayLongPrimitive(new long[]{1, 2, 5, 10, 20});
        System.out.println(pbs.estimateSerializedSize(c));
        long addr = a.alloc(2048);
        // Serialize
        Allocator.StoreContext sc = a.getStoreContext(addr);

        //UnsafeReflection.debugArray(c.a);
        pbs.serialize(c, sc);

        // Deser
        Allocator.LoadContext lc = a.getLoadContext(addr);
        ArrayLongPrimitive res = (ArrayLongPrimitive) pbs.deserialize(lc);

        assertTrue(Arrays.equals(c.a, res.a));
        //for(long i : res.a){
        //    System.out.println(i);
        //}
        // estimated size

    }

    @Test
    public void test_ser_deser_simple_with_all_type_primitive_bean() {
        Allocator a = new Allocator(1 * 1024 * 1024);
        UnsafePrimitiveBeanSerializer pbs = new UnsafePrimitiveBeanSerializer();
        LotOfPrimitiveAndArray c = new LotOfPrimitiveAndArray();
        System.out.println(pbs.estimateSerializedSize(c));
        long addr = a.alloc(1024);
        // Serialize
        Allocator.StoreContext sc = a.getStoreContext(addr);

        //UnsafeReflection.debugArray(c.a);
        pbs.serialize(c, sc);

        // Deser
        Allocator.LoadContext lc = a.getLoadContext(addr);
        Object res = pbs.deserialize(lc);

        assertEquals(c, res);
        //for(long i : res.a){
        //    System.out.println(i);
        //}
        // estimated size
        int serSize = pbs.estimateSerializedSize(c);
    }

    @Test
    public void test_ser_deser_simple_double_array_bean() {
        Allocator a = new Allocator(1 * 1024 * 1024);
        UnsafePrimitiveBeanSerializer pbs = new UnsafePrimitiveBeanSerializer();
        LotOfDoubleArray c = new LotOfDoubleArray();
        System.out.println(pbs.estimateSerializedSize(c));
        long addr = a.alloc(1024);
        // Serialize
        Allocator.StoreContext sc = a.getStoreContext(addr);

        //UnsafeReflection.debugArray(c.a);
        pbs.serialize(c, sc);

        // Deser
        Allocator.LoadContext lc = a.getLoadContext(addr);
        Object res = pbs.deserialize(lc);

        assertEquals(c, res);
        //for(long i : res.a){
        //    System.out.println(i);
        //}
        // estimated size
        int serSize = pbs.estimateSerializedSize(c);
    }

}
