package jbu.cache;

import jbu.serializer.UnsafePrimitiveBeanSerializer;
import jbu.testobject.LotOfPrimitiveAndArray;
import org.apache.directmemory.DirectMemory;
import org.apache.directmemory.cache.CacheService;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: jburet
 * Date: 25/07/12
 * Time: 14:30
 * To change this template use File | Settings | File Templates.
 */
public class BenchDirectmemory {

    @Test
    public void bench_put_get() {
        // Put n object in map
        // Get them all
        // Remove them
        // etc...
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        int NB_OBJ = 10000;

        long putTime = 0;
        long put = 0;
        long getTime = 0;
        long get = 0;

        CacheService<Integer, LotOfPrimitiveAndArray> cacheService = new DirectMemory<Integer, LotOfPrimitiveAndArray>()
                .setNumberOfBuffers(1)
                .setSize(10000 * 1024)
                .setInitialCapacity(10000)
                .setConcurrencyLevel(4)
                .newCacheService();

        LotOfPrimitiveAndArray cachedObject = new LotOfPrimitiveAndArray();
        int estimSize = new UnsafePrimitiveBeanSerializer().estimateSerializedSize(cachedObject);
        long objectSizeInMemory = estimSize * get / 1024 / 1024;

        for (int j = 0; j < NB_OBJ; j++) {
            long start = System.nanoTime();
            for (int i = 0; i < NB_OBJ; i++) {
                cacheService.put(i, cachedObject);
            }
            putTime += System.nanoTime() - start;
            put += NB_OBJ;

            start = System.nanoTime();
            for (int i = 0; i < NB_OBJ; i++) {
                cacheService.retrieve(i);
            }
            getTime += System.nanoTime() - start;
            get += NB_OBJ;
            cacheService.clear();

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
