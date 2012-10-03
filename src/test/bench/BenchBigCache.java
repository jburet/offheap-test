import com.esotericsoftware.kryo.Registration;
import jbu.serializer.unsafe.UnsafePrimitiveBeanSerializer;
import jbu.testobject.LotOfPrimitive;
import jbu.testobject.LotOfPrimitiveAndArrayAndString;
import org.bigcache.BigCache;
import org.bigcache.BigCacheManager;
import org.bigcache.core.serialization.impl.kryo.KryoFactory;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class BenchBigCache {

    @Test
    public void bench_put_get() throws FileNotFoundException {
        // Put n object in map
        // Get them all
        // Remove them
        // etc...
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        int NB_OBJ = 400000;
        int NB_ITER = 40;
        long putTime = 0;
        long put = 0;
        long getTime = 0;
        long get = 0;

        BigCacheManager bigCacheManager = BigCacheManager.configure(new FileInputStream("src/test/resources/bigcache-config.xml"));
        BigCache<Integer, LotOfPrimitiveAndArrayAndString> cache = bigCacheManager.getCache("users");
        KryoFactory.getKryo().register(LotOfPrimitiveAndArrayAndString.class, 10000);

        LotOfPrimitiveAndArrayAndString cachedObject = new LotOfPrimitiveAndArrayAndString();
        int estimSize = new UnsafePrimitiveBeanSerializer().calculateSerializedSize(cachedObject);
        System.out.println("Object size in memory  : " + estimSize * NB_OBJ / 1024 / 1024);

        for (int j = 0; j < NB_OBJ; j++) {
            long start = System.nanoTime();
            for (int i = 0; i < NB_OBJ; i++) {
                cache.put(i, cachedObject);
            }
            putTime += System.nanoTime() - start;
            put += NB_OBJ;

            start = System.nanoTime();
            for (int i = 0; i < NB_OBJ; i++) {
                LotOfPrimitiveAndArrayAndString r = cache.get(i);
            }
            getTime += System.nanoTime() - start;
            get += NB_OBJ;
            cache.evictAll();

            System.out.println("Iteration : " + j);
            double getTimeSecond = getTime / (double) (1000 * 1000 * 1000);
            double putTimeSecond = putTime / (double) (1000 * 1000 * 1000);

            System.out.println("Puts : " + put / putTimeSecond + " object/s");
            System.out.println("Gets : " + get / getTimeSecond + " object/s");
            System.out.println("Puts : " + (put * estimSize / 1024 / 1024) / putTimeSecond + " MB/s");
            System.out.println("Gets : " + (get * estimSize / 1024 / 1024) / getTimeSecond + " MB/s");
            System.out.println("");
            System.out.println("");

        }

    }
}
