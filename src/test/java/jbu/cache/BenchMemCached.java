package jbu.cache;

import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;
import jbu.serializer.unsafe.UnsafePrimitiveBeanSerializer;
import jbu.testobject.LotOfPrimitiveAndArray;
import org.bigcache.BigCache;
import org.bigcache.BigCacheManager;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;

public class BenchMemCached {

    @Test
    public void bench_put_get() throws FileNotFoundException {

        SockIOPool pool = SockIOPool.getInstance();
        pool.setServers(new String[]{"localhost:11211"});
        pool.initialize();

        // Put n object in map
        // Get them all
        // Remove them
        // etc...

        int NB_OBJ = 100000;

        long putTime = 0;
        long put = 0;
        long getTime = 0;
        long get = 0;

        MemCachedClient mClient = new MemCachedClient();

        LotOfPrimitiveAndArray cachedObject = new LotOfPrimitiveAndArray();
        int estimSize = new UnsafePrimitiveBeanSerializer().estimateSerializedSize(cachedObject);
        long objectSizeInMemory = estimSize * get / 1024 / 1024;

        for (int j = 0; j < NB_OBJ; j++) {
            long start = System.nanoTime();
            for (int i = 0; i < NB_OBJ; i++) {
                mClient.set(Integer.toString(i), cachedObject);
            }
            putTime += System.nanoTime() - start;
            put += NB_OBJ;

            start = System.nanoTime();
            for (int i = 0; i < NB_OBJ; i++) {
                mClient.get(Integer.toString(i));
            }
            getTime += System.nanoTime() - start;
            get += NB_OBJ;
            mClient.flushAll();

            System.out.println("Iteration : " + j);
            double getTimeSecond = getTime / (double) (1000 * 1000 * 1000);
            double putTimeSecond = putTime / (double) (1000 * 1000 * 1000);


            System.out.println("Real object size : " + objectSizeInMemory + " MB");
            System.out.println("Puts : " + put / putTimeSecond + " object/s");
            System.out.println("Gets : " + get / getTimeSecond + " object/s");
            System.out.println("Puts : " + (put * estimSize / 1024 / 1024) / putTimeSecond + " MB/s");
            System.out.println("Gets : " + (get * estimSize / 1024 / 1024) / getTimeSecond + " MB/s");
            System.out.println("");
            System.out.println("");

        }

    }
}
