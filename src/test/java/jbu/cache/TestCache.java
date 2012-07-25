package jbu.cache;

import jbu.testobject.LotOfPrimitiveAndArray;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestCache {

    @Test
    public void test_put_get_object() {
        Cache<Long, LotOfPrimitiveAndArray> cache = new Cache<Long, LotOfPrimitiveAndArray>("testCache", 50 * 1024 * 1024);
        LotOfPrimitiveAndArray cachedObject = new LotOfPrimitiveAndArray();
        cache.put(1l, cachedObject);
        LotOfPrimitiveAndArray res = cache.get(1l);
        assertEquals(res, cachedObject);
    }


}
