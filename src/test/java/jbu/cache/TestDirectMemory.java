package jbu.cache;

import jbu.testobject.LotOfPrimitiveAndArray;
import org.apache.directmemory.DirectMemory;
import org.apache.directmemory.cache.CacheService;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestDirectMemory {

    @Test
    public void test_put_get_object() {
        CacheService<Integer, LotOfPrimitiveAndArray> cacheService = new DirectMemory<Integer, LotOfPrimitiveAndArray>()
                .setNumberOfBuffers(1)
                .setSize(100 * 1024 * 1024)
                .newCacheService();
        LotOfPrimitiveAndArray cachedObject = new LotOfPrimitiveAndArray();
        cacheService.put(1, cachedObject);
        LotOfPrimitiveAndArray res = cacheService.retrieve(1);
        assertEquals(cachedObject, res);
    }


}
