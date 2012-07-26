package jbu.cache;

import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;
import jbu.testobject.LotOfPrimitiveAndArray;
import org.apache.directmemory.DirectMemory;
import org.apache.directmemory.cache.CacheService;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestMemCached {

    @Test
    public void test_put_get_object() {
        SockIOPool pool = SockIOPool.getInstance();
        pool.setServers(new String[]{"localhost:11211"});
        pool.initialize();
        MemCachedClient mClient = new MemCachedClient();
        LotOfPrimitiveAndArray cachedObject = new LotOfPrimitiveAndArray();
        mClient.set("1", cachedObject);
        LotOfPrimitiveAndArray res = (LotOfPrimitiveAndArray) mClient.get("1");
        assertEquals(cachedObject, res);
    }


}
