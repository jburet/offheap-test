import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;
import jbu.serializer.unsafe.UnsafePrimitiveBeanSerializer;
import jbu.testobject.LotOfPrimitiveAndArrayAndString;
import org.bigcache.core.serialization.impl.kryo.KryoFactory;
import org.junit.Test;

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

        int NB_OBJ = 1000000;

        long putTime = 0;
        long put = 0;
        long getTime = 0;
        long get = 0;

        MemCachedClient mClient = new MemCachedClient();

        LotOfPrimitiveAndArrayAndString cachedObject = new LotOfPrimitiveAndArrayAndString();
        int estimSize = new UnsafePrimitiveBeanSerializer().calculateSerializedSize(cachedObject);
        Kryo k = new Kryo();
        Output bout = new Output(1024);
        k.writeObject(bout, cachedObject);
        bout.close();

        int realSize = bout.getBuffer().length;
        System.out.println("Estimate Object size in memory  : " + estimSize * NB_OBJ / 1024 / 1024);
        System.out.println("Real Object size in memory  : " + realSize * NB_OBJ / 1024 / 1024);
        System.out.println("Store : " + NB_OBJ);

        for (int j = 0; j < NB_OBJ; j++) {
            long start = System.nanoTime();
            for (int i = 0; i < NB_OBJ; i++) {
                bout = new Output(1024);
                k.writeObject(bout, cachedObject);
                bout.close();
                mClient.set(Integer.toString(i), bout.getBuffer());
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

            System.out.println("Puts : " + put / putTimeSecond + " object/s");
            System.out.println("Gets : " + get / getTimeSecond + " object/s");
            System.out.println("Puts : " + (put * estimSize / 1024 / 1024) / putTimeSecond + " MB/s");
            System.out.println("Gets : " + (get * estimSize / 1024 / 1024) / getTimeSecond + " MB/s");
            System.out.println("");
            System.out.println("");

        }

    }
}
