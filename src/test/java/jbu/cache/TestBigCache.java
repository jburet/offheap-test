package jbu.cache;

import jbu.testobject.LotOfPrimitiveAndArrayAndString;
import org.bigcache.BigCache;
import org.bigcache.BigCacheManager;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;

public class TestBigCache {

    @Test
    public void test_put_get_object() throws FileNotFoundException {
        BigCacheManager bigCacheManager = BigCacheManager.configure(new FileInputStream("src/test/resources/bigcache-config.xml"));
        BigCache cache = bigCacheManager.getCache("users");

        LotOfPrimitiveAndArrayAndString cachedObject = new LotOfPrimitiveAndArrayAndString();
        cache.put(1, cachedObject);
        LotOfPrimitiveAndArrayAndString res = (LotOfPrimitiveAndArrayAndString) cache.get(1);
        assertEquals(cachedObject, res);
    }


}
