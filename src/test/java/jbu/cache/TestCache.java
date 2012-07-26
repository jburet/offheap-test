package jbu.cache;

import jbu.testobject.LotOfPrimitiveAndArrayAndString;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestCache {

    @Test
    public void test_put_get_object() {
        Cache<Long, LotOfPrimitiveAndArrayAndString> cache = new Cache<Long, LotOfPrimitiveAndArrayAndString>("testCache", 50 * 1024 * 1024);
        LotOfPrimitiveAndArrayAndString cachedObject = new LotOfPrimitiveAndArrayAndString();
        cache.put(1l, cachedObject);
        LotOfPrimitiveAndArrayAndString res = cache.get(1l);
        assertEquals(res, cachedObject);
    }


}
