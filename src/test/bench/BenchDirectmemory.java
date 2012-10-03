import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import jbu.serializer.unsafe.UnsafePrimitiveBeanSerializer;
import jbu.testobject.LotOfPrimitive;
import jbu.testobject.LotOfPrimitiveAndArrayAndString;
import org.apache.directmemory.DirectMemory;
import org.apache.directmemory.cache.CacheService;
import org.apache.directmemory.memory.MemoryManagerServiceImpl;
import org.apache.directmemory.memory.UnsafeMemoryManagerServiceImpl;
import org.apache.directmemory.memory.unsafe.NewUnsafeMemoryManagerServiceImpl;
import org.apache.directmemory.serialization.kryo.KryoSerializer;
import org.junit.Test;


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
        int NB_OBJ = 1000000;
        int NB_ITER = 4;

        long putTime = 0;
        long put = 0;
        long getTime = 0;
        long get = 0;

        CacheService<Integer, LotOfPrimitiveAndArrayAndString> cacheService = new DirectMemory<Integer, LotOfPrimitiveAndArrayAndString>()
                .setNumberOfBuffers(1)
                .setSize(200 * 1024 * 1024)
                .setInitialCapacity(NB_OBJ)
                .setConcurrencyLevel(2)
                .newCacheService();

        LotOfPrimitiveAndArrayAndString cachedObject = new LotOfPrimitiveAndArrayAndString();
        int estimSize = new UnsafePrimitiveBeanSerializer().calculateSerializedSize(cachedObject);
        long objectSizeInMemory = estimSize * get / 1024 / 1024;

        for (int j = 0; j < NB_ITER; j++) {
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


    @Test
    public void bench_put_get_withkryo() {
        // Put n object in map
        // Get them all
        // Remove them
        // etc...
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        int NB_OBJ = 100000;
        int NB_ITER = 100;

        long putTime = 0;
        long put = 0;
        long getTime = 0;
        long get = 0;

        CacheService<Integer, LotOfPrimitiveAndArrayAndString> cacheService = new DirectMemory<Integer, LotOfPrimitiveAndArrayAndString>()
                .setNumberOfBuffers(1)
                .setSize(500 * 1024 * 1024)
                .setInitialCapacity(NB_OBJ)
                .setConcurrencyLevel(1)
                .setDisposalTime(10 * 60 * 1000)
                //.setMemoryManager(new NewUnsafeMemoryManagerServiceImpl<LotOfPrimitiveAndArrayAndString>())
                .setMemoryManager(new MemoryManagerServiceImpl<LotOfPrimitiveAndArrayAndString>())
                .setSerializer(new KryoSerializer())
                .newCacheService();

        LotOfPrimitiveAndArrayAndString cachedObject = new LotOfPrimitiveAndArrayAndString();
        int estimSize = new UnsafePrimitiveBeanSerializer().calculateSerializedSize(cachedObject);
        Kryo k = new Kryo();
        Output bout = new Output(1024);
        k.writeObject(bout, cachedObject);
        bout.close();

        int realSize = bout.getBuffer().length;
        System.out.println("Estimate Object size in memory  : " + estimSize * NB_OBJ / 1024 / 1024);
        System.out.println("Real Object size in memory  : " + realSize * NB_OBJ / 1024 / 1024 + " MB");
        System.out.println("Store : " + NB_OBJ);

        for (int j = 0; j < NB_ITER; j++) {
            long start = System.nanoTime();
            for (int i = 0; i < NB_OBJ; i++) {
                //bout = new Output(1024);
                //k.writeObject(bout, cachedObject);
                //bout.close();
                cacheService.put(i,cachedObject);
            }
            putTime += System.nanoTime() - start;
            put += NB_OBJ;

            start = System.nanoTime();
            for (int i = 0; i < NB_OBJ; i++) {
               cacheService.retrieve(i);
                // deser
                //if (out != null) {
                //    Input in = new Input(out);
                //    k.readObject(in, LotOfPrimitiveAndArrayAndString.class);
                //}else{
                //    System.out.println("object not find : "+i);
                //}
            }
            getTime += System.nanoTime() - start;
            get += NB_OBJ;
            cacheService.clear();

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
