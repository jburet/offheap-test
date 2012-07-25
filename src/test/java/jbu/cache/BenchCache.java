package jbu.cache;

import jbu.offheap.Allocator;
import jbu.serializer.kryo.KryoSerializer;
import jbu.serializer.unsafe.UnsafePrimitiveBeanSerializer;
import jbu.testobject.LotOfPrimitiveAndArray;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BenchCache {

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

        Allocator allocator = new Allocator(1024 * 1024 * 1024);
        Cache<Integer, LotOfPrimitiveAndArray> cache = new Cache<Integer, LotOfPrimitiveAndArray>("testCache", allocator, new KryoSerializer());
        LotOfPrimitiveAndArray cachedObject = new LotOfPrimitiveAndArray();
        int estimSize = new UnsafePrimitiveBeanSerializer().estimateSerializedSize(cachedObject);
        long objectSizeInMemory = estimSize * get / 1024 / 1024;
        for (int j = 0; j < 10000; j++) {
            long start = System.nanoTime();
            for (int i = 0; i < NB_OBJ; i++) {
                cache.put(i, cachedObject);
            }
            putTime += System.nanoTime() - start;
            put += NB_OBJ;

            start = System.nanoTime();
            for (int i = 0; i < NB_OBJ; i++) {
                cache.get(i);
            }
            getTime += System.nanoTime() - start;
            get += NB_OBJ;
            cache.clean();

            System.out.println("Iteration : " + j);
            double getTimeSecond = getTime / (double) (1000 * 1000 * 1000);
            double putTimeSecond = putTime / (double) (1000 * 1000 * 1000);


            System.out.println("Real object size : " + objectSizeInMemory + " MB");
            System.out.println("Memory allocated : " + allocator.getAllocatedMemory() / 1024 / 1024 + " MB");
            System.out.println("Memory used : " + allocator.getUsedMemory() / 1024 / 1024 + " MB");
            System.out.println("Puts : " + put / putTimeSecond + " object/s");
            System.out.println("Gets : " + get / getTimeSecond + " object/s");
            System.out.println("Puts : " + (put * estimSize / 1024 / 1024) / putTimeSecond + " MB/s");
            System.out.println("Gets : " + (get * estimSize / 1024 / 1024) / getTimeSecond + " MB/s");
            System.out.println("");
            System.out.println("");

        }


    }

}
