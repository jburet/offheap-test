package jbu.serializer;

import jbu.exception.CannotDeserializeException;
import jbu.offheap.Allocator;
import jbu.offheap.LoadContext;
import jbu.offheap.StoreContext;
import jbu.serializer.unsafe.UnsafePrimitiveBeanSerializer;
import jbu.testobject.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestUnsafePrimitiveBeanSerializer {

    @Test
    public void test_ser_deser_simple_int_bean() throws CannotDeserializeException {
        Allocator a = new Allocator(1 * 1024 * 1024);
        Serializer pbs = new UnsafePrimitiveBeanSerializer();
        long addr = a.alloc(9 * 4);
        // Serialize
        StoreContext sc = a.getStoreContext(addr);
        LotOfInt c = new LotOfInt(10);
        pbs.serialize(c, sc);

        // Deser
        LoadContext lc = a.getLoadContext(addr);
        Object res = pbs.deserialize(lc);
        assertEquals(c, res);
    }

    @Test
    public void test_ser_deser_simple_bean() throws CannotDeserializeException {
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
        Object res = pbs.deserialize(lc);
        assertEquals(c, res);
    }

    @Test
    public void test_ser_deser_simple_boolean_bean() throws CannotDeserializeException {
        Allocator a = new Allocator(1 * 1024 * 1024);
        Serializer pbs = new UnsafePrimitiveBeanSerializer();
        LotOfBoolean c = new LotOfBoolean(true);
        int serSize = pbs.calculateSerializedSize(c);
        long addr = a.alloc(serSize);
        // Serialize
        StoreContext sc = a.getStoreContext(addr);

        pbs.serialize(c, sc);

        // Deser
        LoadContext lc = a.getLoadContext(addr);
        Object res = pbs.deserialize(lc);
        assertEquals(c, res);
    }

    @Test
    public void test_ser_deser_simple_char_bean() throws CannotDeserializeException {
        Allocator a = new Allocator(1 * 1024 * 1024);
        Serializer pbs = new UnsafePrimitiveBeanSerializer();
        LotOfChar c = new LotOfChar('a');
        int serSize = pbs.calculateSerializedSize(c);
        long addr = a.alloc(serSize);
        // Serialize
        StoreContext sc = a.getStoreContext(addr);

        pbs.serialize(c, sc);

        // Deser
        LoadContext lc = a.getLoadContext(addr);
        Object res = pbs.deserialize(lc);
        assertEquals(c, res);
    }

    @Test
    public void test_ser_deser_simple_few_double_bean() throws CannotDeserializeException {
        Allocator a = new Allocator(1 * 1024 * 1024);
        Serializer pbs = new UnsafePrimitiveBeanSerializer();
        SomeDouble c = new SomeDouble(1);
        int serSize = pbs.calculateSerializedSize(c);
        long addr = a.alloc(serSize);
        // Serialize
        StoreContext sc = a.getStoreContext(addr);

        pbs.serialize(c, sc);

        // Deser
        LoadContext lc = a.getLoadContext(addr);
        Object res = pbs.deserialize(lc);
        assertEquals(c, res);
    }

    @Test
    public void test_ser_deser_simple_double_bean() throws CannotDeserializeException {
        Allocator a = new Allocator(1 * 1024 * 1024);
        Serializer pbs = new UnsafePrimitiveBeanSerializer();
        LotOfDouble c = new LotOfDouble(1);
        int serSize = pbs.calculateSerializedSize(c);
        long addr = a.alloc(serSize);
        // Serialize
        StoreContext sc = a.getStoreContext(addr);

        pbs.serialize(c, sc);

        // Deser
        LoadContext lc = a.getLoadContext(addr);
        LotOfDouble res = (LotOfDouble) pbs.deserialize(lc);
        assertEquals(c, res);
    }

    @Test
    public void test_ser_deser_simple_array_int_primitive_bean() throws CannotDeserializeException {
        Allocator a = new Allocator(1 * 1024 * 1024);
        Serializer pbs = new UnsafePrimitiveBeanSerializer();
        long addr = a.alloc(64);
        // Serialize
        StoreContext sc = a.getStoreContext(addr);
        ArrayIntPrimitive c = new ArrayIntPrimitive(new int[]{2, 2, 2, 2, 2, 2});
        pbs.serialize(c, sc);

        // Deser
        LoadContext lc = a.getLoadContext(addr);
        ArrayIntPrimitive res = (ArrayIntPrimitive) pbs.deserialize(lc);
        for (int i : res.a) {
            System.out.println(i);
        }

        assertTrue(Arrays.equals(c.a, res.a));
        // estimated size
        int serSize = pbs.calculateSerializedSize(c);
    }

    @Test
    public void test_ser_deser_simple_array_boolean_primitive_bean() throws CannotDeserializeException {
        Allocator a = new Allocator(1 * 1024 * 1024);
        Serializer pbs = new UnsafePrimitiveBeanSerializer();
        long addr = a.alloc(64);
        // Serialize
        StoreContext sc = a.getStoreContext(addr);
        ArrayBooleanPrimitive c = new ArrayBooleanPrimitive(new boolean[]{true, true, true, true, true});
        pbs.serialize(c, sc);

        // Deser
        LoadContext lc = a.getLoadContext(addr);
        ArrayBooleanPrimitive res = (ArrayBooleanPrimitive) pbs.deserialize(lc);
        //for (boolean i : res.a) {
        //    System.out.println(i);
        //}
        assertTrue(Arrays.equals(c.a, res.a));
        // estimated size
        int serSize = pbs.calculateSerializedSize(c);
    }


    @Test
    public void test_ser_deser_simple_array_long_primitive_bean() throws CannotDeserializeException {
        Allocator a = new Allocator(1 * 1024 * 1024);
        Serializer pbs = new UnsafePrimitiveBeanSerializer();
        ArrayLongPrimitive c = new ArrayLongPrimitive(new long[]{1, 2, 5, 10, 20});
        System.out.println(pbs.calculateSerializedSize(c));
        long addr = a.alloc(2048);
        // Serialize
        StoreContext sc = a.getStoreContext(addr);

        //UnsafeReflection.debugArray(c.a);
        pbs.serialize(c, sc);

        // Deser
        LoadContext lc = a.getLoadContext(addr);
        ArrayLongPrimitive res = (ArrayLongPrimitive) pbs.deserialize(lc);

        assertTrue(Arrays.equals(c.a, res.a));
        //for(long i : res.a){
        //    System.out.println(i);
        //}
        // estimated size

    }

    @Test
    public void test_ser_deser_simple_with_all_type_primitive_bean() throws CannotDeserializeException {
        Allocator a = new Allocator(1 * 1024 * 1024);
        Serializer pbs = new UnsafePrimitiveBeanSerializer();
        LotOfPrimitiveAndArrayAndString c = new LotOfPrimitiveAndArrayAndString();
        System.out.println(pbs.calculateSerializedSize(c));
        long addr = a.alloc(2048);
        // Serialize
        StoreContext sc = a.getStoreContext(addr);

        //UnsafeReflection.debugArray(c.a);
        pbs.serialize(c, sc);

        // Deser
        LoadContext lc = a.getLoadContext(addr);
        Object res = pbs.deserialize(lc);

        assertEquals(c, res);
        //for(long i : res.a){
        //    System.out.println(i);
        //}
        // estimated size
        int serSize = pbs.calculateSerializedSize(c);
    }

    @Test
    public void test_ser_deser_simple_double_array_bean() throws CannotDeserializeException {
        Allocator a = new Allocator(1 * 1024 * 1024);
        Serializer pbs = new UnsafePrimitiveBeanSerializer();
        LotOfDoubleArray c = new LotOfDoubleArray();
        System.out.println(pbs.calculateSerializedSize(c));
        long addr = a.alloc(1024);
        // Serialize
        StoreContext sc = a.getStoreContext(addr);

        //UnsafeReflection.debugArray(c.a);
        pbs.serialize(c, sc);

        // Deser
        LoadContext lc = a.getLoadContext(addr);
        Object res = pbs.deserialize(lc);

        assertEquals(c, res);
        //for(long i : res.a){
        //    System.out.println(i);
        //}
        // estimated size
        int serSize = pbs.calculateSerializedSize(c);
    }

    @Test
    public void test_ser_deser_simple_string_bean() throws CannotDeserializeException {
        Allocator a = new Allocator(1 * 1024 * 1024);
        Serializer pbs = new UnsafePrimitiveBeanSerializer();
        LotOfString c = new LotOfString();
        c.a = "test";
        System.out.println(pbs.calculateSerializedSize(c));
        long addr = a.alloc(4000);
        // Serialize
        StoreContext sc = a.getStoreContext(addr);

        //UnsafeReflection.debugArray(c.a);
        pbs.serialize(c, sc);

        // Deser
        LoadContext lc = a.getLoadContext(addr);
        Object res = pbs.deserialize(lc);

        assertEquals(c, res);
        //for(long i : res.a){
        //    System.out.println(i);
        //}
        // estimated size
        int serSize = pbs.calculateSerializedSize(c);
    }

    @Test
    public void test_ser_deser_simple_arraylist_bean() throws CannotDeserializeException {
        Allocator a = new Allocator(1 * 1024 * 1024);
        Serializer pbs = new UnsafePrimitiveBeanSerializer();
        ObjectWithArrayList c = new ObjectWithArrayList();
        c.collection = new ArrayList<String>();
        c.collection.add("a");
        c.collection.add("ab");
        c.collection.add("abc");
        c.collection.add("abcd");
        System.out.println(pbs.calculateSerializedSize(c));
        long addr = a.alloc(4000);
        // Serialize
        StoreContext sc = a.getStoreContext(addr);

        //UnsafeReflection.debugArray(c.a);
        pbs.serialize(c, sc);

        // Deser
        LoadContext lc = a.getLoadContext(addr);
        Object res = pbs.deserialize(lc);

        assertEquals(c, res);
        //for(long i : res.a){
        //    System.out.println(i);
        //}
        // estimated size
        int serSize = pbs.calculateSerializedSize(c);
    }

    @Test
    public void test_ser_deser_simple_wrapper_bean() throws CannotDeserializeException {
        Allocator a = new Allocator(1 * 1024 * 1024);
        Serializer pbs = new UnsafePrimitiveBeanSerializer();
        LotOfWrapper c = new LotOfWrapper();
        c.b1 = false;
        c.b2 = 'g';
        c.b3 = 10;
        c.b4 = 11;
        c.b5 = 12;
        c.b6 = 13l;
        c.b7 = 14f;
        c.b8 = 15d;
        long addr = a.alloc(pbs.calculateSerializedSize(c));
        // Serialize
        StoreContext sc = a.getStoreContext(addr);

        //UnsafeReflection.debugArray(c.a);
        pbs.serialize(c, sc);

        // Deser
        LoadContext lc = a.getLoadContext(addr);
        Object res = pbs.deserialize(lc);

        assertEquals(c, res);
        //for(long i : res.a){
        //    System.out.println(i);
        //}
    }

}
