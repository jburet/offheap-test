package jbu.offheap;

import jbu.exception.BufferOverflowException;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static org.junit.Assert.*;

public class TestUnsafeBins {

    @Test(expected = Exception.class)
    public void test_new_with_0_chunk_should_throws_exception() {
        UnsafeBins ub = new UnsafeBins(0, 1, 0);
    }

    @Test(expected = Exception.class)
    public void test_new_with_size_0_should_throws_exception() {
        UnsafeBins ub = new UnsafeBins(1, 0, 0);
    }

    @Test(expected = Exception.class)
    public void test_new_with_too_big_memory_should_throws_exception() {
        UnsafeBins ub = new UnsafeBins(Integer.MAX_VALUE, 2, 0);
    }

    @Test
    public void test_allocate_0_chunk_should_return_null() {
        UnsafeBins ub = new UnsafeBins(1, 1, 0);
        assertNull(ub.allocateNChunk(0));
    }

    @Test
    public void test_allocate_1_chunk_should_return_array() {
        UnsafeBins ub = new UnsafeBins(1, 1, 0);
        long[] chunks = ub.allocateNChunk(1);
        assertNotNull(chunks);
        assertEquals(1, chunks.length);
    }

    @Test
    public void test_allocate_1_chunk_two_time_should_return_array() {
        UnsafeBins ub = new UnsafeBins(2, 1, 0);
        long[] chunks = ub.allocateNChunk(1);
        assertNotNull(chunks);
        assertEquals(1, chunks.length);

        chunks = ub.allocateNChunk(1);
        assertNotNull(chunks);
        assertEquals(1, chunks.length);
    }

    @Test
    public void test_allocate_more_chunk_than_size_should_return_null() {
        UnsafeBins ub = new UnsafeBins(2, 1, 0);
        long[] chunks = ub.allocateNChunk(3);
        assertNull(chunks);
    }

    @Test(expected = BufferOverflowException.class)
    public void stored_too_big_data_should_fail() {
        byte[] data = new byte[10];
        UnsafeBins ub = new UnsafeBins(1, 5, 0);
        long[] chunks = ub.allocateNChunk(1);
        ub.storeInChunk(AddrAlign.getChunkId(chunks[0]), data, 0, data.length);
    }

    @Test
    public void stored_data_should_be_loaded() {
        byte[] data = new byte[10];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) i;
        }
        UnsafeBins ub = new UnsafeBins(2, 15, 0);
        long[] chunks = ub.allocateNChunk(1);
        ub.storeInChunk(AddrAlign.getChunkId(chunks[0]), data, 0, data.length);
        byte[] dataRes = ub.loadFromChunk(AddrAlign.getChunkId(chunks[0]));
        assertTrue(Arrays.equals(data, dataRes));
    }

    @Test
    public void store_chunk_from_direct_byte_buffer_should_be_loaded() {
        byte[] data = new byte[10];
        ByteBuffer bb = ByteBuffer.allocateDirect(10);
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) i;
        }
        bb.put(data);
        bb.flip();

        UnsafeBins ub = new UnsafeBins(2, 15, 0);
        long[] chunks = ub.allocateNChunk(1);
        ub.storeInChunk(AddrAlign.getChunkId(chunks[0]), bb);
        byte[] dataRes = ub.loadFromChunk(AddrAlign.getChunkId(chunks[0]));
        assertTrue(Arrays.equals(data, dataRes));
    }

}
