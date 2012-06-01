package jbu;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertTrue;

public class TestAllocator {

    @Test
    public void test_alloc_some_byte_and_store_data_should_be_reloaded() {
        byte[] data = new byte[10];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) i;
        }
        Allocator allocator = new Allocator();
        OffheapReference or = allocator.alloc(10);
        or.store(data);
        byte[] dataRes = or.load();
        assertTrue(Arrays.equals(data, dataRes));
    }

    @Test
    public void test_alloc_more_byte_than_chunk_size_and_store_data_should_be_reloaded() {
        byte[] data = new byte[1500];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) i;
        }
        Allocator allocator = new Allocator();
        OffheapReference or = allocator.alloc(1500);
        or.store(data);
        byte[] dataRes = or.load();
        assertTrue(Arrays.equals(data, dataRes));
    }

}
