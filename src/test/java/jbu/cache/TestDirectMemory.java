package jbu.cache;

import jbu.testobject.LotOfPrimitiveAndArrayAndString;
import org.apache.directmemory.DirectMemory;
import org.apache.directmemory.cache.CacheService;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestDirectMemory {

    @Test
    public void test_put_get_object() {
        CacheService<Integer, LotOfPrimitiveAndArrayAndString> cacheService = new DirectMemory<Integer, LotOfPrimitiveAndArrayAndString>()
                .setNumberOfBuffers(1)
                .setSize(100 * 1024 * 1024)
                .newCacheService();
        LotOfPrimitiveAndArrayAndString cachedObject = new LotOfPrimitiveAndArrayAndString();
        cacheService.put(1, cachedObject);
        LotOfPrimitiveAndArrayAndString res = cacheService.retrieve(1);
        assertEquals(cachedObject, res);
    }


}
