import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import jbu.serializer.unsafe.UnsafePrimitiveBeanSerializer;
import jbu.testobject.LotOfPrimitiveAndArrayAndString;
import org.bigcache.BigCache;
import org.bigcache.BigCacheManager;
import org.bigcache.core.serialization.impl.kryo.KryoFactory;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class BenchHugeCollection {

    @Test
    public void bench_put_get() throws FileNotFoundException {
        // Put n object in map
        // Get them all
        // Remove them
        // etc...
        int NB_OBJ = 100000;
        int NB_ITER = 100;
        long putTime = 0;
        long put = 0;
        long getTime = 0;
        long get = 0;

        BigCacheManager bigCacheManager = BigCacheManager.configure(new FileInputStream("src/test/resources/bigcache-config.xml"));
        BigCache<Integer, LotOfPrimitiveAndArrayAndString> cache = bigCacheManager.getCache("users");
        KryoFactory.getKryo().register(LotOfPrimitiveAndArrayAndString.class, 10000);
        Output out = new Output(1024);

        LotOfPrimitiveAndArrayAndString cachedObject = new LotOfPrimitiveAndArrayAndString();

        new Kryo().writeObject(out, cachedObject);
        int realSize = out.getBuffer().length;

        int estimSize = new UnsafePrimitiveBeanSerializer().calculateSerializedSize(cachedObject);
        System.out.println("Object size in memory  : " + realSize * NB_OBJ / 1024 / 1024);

        for (int j = 0; j < NB_ITER; j++) {
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
            System.out.println("Puts : " + (put * realSize / 1024 / 1024) / putTimeSecond + " MB/s");
            System.out.println("Gets : " + (get * realSize / 1024 / 1024) / getTimeSecond + " MB/s");
            System.out.println("");
            System.out.println("");

        }

    }
}
