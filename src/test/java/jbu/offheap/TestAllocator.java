package jbu.offheap;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;

public class TestAllocator {

    @Test
    public void test_alloc_some_byte_and_store_data_should_be_reloaded() {
        byte[] data = new byte[10];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) i;
        }
        Allocator allocator = new Allocator(10 * 1024 * 1024, false);
        long firstChunk = allocator.alloc(10);
        allocator.store(firstChunk, data);
        byte[] dataRes = allocator.load(firstChunk);
        assertTrue(Arrays.equals(data, dataRes));
    }

    @Test
    public void test_alloc_more_byte_than_chunk_size_and_store_data_should_be_reloaded() {
        byte[] data = new byte[1500];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) i;
        }
        Allocator allocator = new Allocator(10 * 1024 * 1024, false);
        long firstChunk = allocator.alloc(1500);
        allocator.store(firstChunk, data);
        byte[] dataRes = allocator.load(firstChunk);
        assertTrue(Arrays.equals(data, dataRes));
    }

    @Test
    public void test_alloc_more_byte_than_biggest_bin_and_store_data_should_be_reloaded() {
        byte[] data = new byte[123456];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) i;
        }
        Allocator allocator = new Allocator(10 * 1024 * 1024, false);
        long firstChunk = allocator.alloc(123456);
        allocator.store(firstChunk, data);
        byte[] dataRes = allocator.load(firstChunk);
        assertTrue(Arrays.equals(data, dataRes));
    }

    @Test
    public void test_alloc_more_bytebuffer_than_biggest_bin_and_store_data_should_be_reloaded() {
        byte[] data = new byte[123456];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) i;
        }
        ByteBuffer bb = ByteBuffer.allocateDirect(data.length);
        bb.put(data);
        bb.flip();
        Allocator allocator = new Allocator(10 * 1024 * 1024, true);
        long firstChunk = allocator.alloc(123456);
        allocator.store(firstChunk, bb);
        byte[] dataRes = allocator.load(firstChunk);
        assertTrue(Arrays.equals(data, dataRes));
    }


}
