package jbu.cache;

import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;
import jbu.testobject.LotOfPrimitiveAndArrayAndString;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestMemCached {

    @Test
    public void test_put_get_object() {
        SockIOPool pool = SockIOPool.getInstance();
        pool.setServers(new String[]{"localhost:11211"});
        pool.initialize();
        MemCachedClient mClient = new MemCachedClient();
        LotOfPrimitiveAndArrayAndString cachedObject = new LotOfPrimitiveAndArrayAndString();
        mClient.set("1", cachedObject);
        LotOfPrimitiveAndArrayAndString res = (LotOfPrimitiveAndArrayAndString) mClient.get("1");
        assertEquals(cachedObject, res);
    }


}
