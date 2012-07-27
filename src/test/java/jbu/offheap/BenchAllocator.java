package jbu.offheap;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.nio.ByteBuffer;
import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;
import java.util.*;
import java.util.concurrent.*;

public class BenchAllocator {

    private static MBeanServer mbs;

    @BeforeClass
    public static void setup() {
        mbs = ManagementFactory.getPlatformMBeanServer();
    }

    @Test
    @Ignore
    public void bench() {

        long totalTime = 0;
        long nbExecution = 0;
        byte[] data = new byte[100];
        List<Long> chunks = new ArrayList<Long>();

        Allocator a = new Allocator(10 * 1024 * 1024);
        OffheapMemoryAccess oma = new OffheapMemoryAccess(a);
        for (int i = 0; i < 100000; i++) {
            long start = System.nanoTime();
            long firstChunk = a.alloc(100);
            oma.store(firstChunk, data);
            totalTime += (System.nanoTime() - start);
            nbExecution++;
            chunks.add(firstChunk);
            if (i % 10000 == 0) {
                System.out.println("Nb allocation : " + a.getNbAllocation());
                System.out.println("Nb free : " + a.getNbFree());
                System.out.println("Used memory : " + a.getUsedMemory());
                for (long firstChunkId : chunks) {
                    a.free(firstChunkId);
                }
                chunks.clear();
            }
        }


        System.out.println("Nb allocation : " + a.getNbAllocation());
        System.out.println("Nb free : " + a.getNbFree());
        System.out.println("Used memory : " + a.getUsedMemory());

        System.out.println("Means : " + totalTime / nbExecution + " ns");
    }

    final Random r = new Random();

    @Test
    public void better_bench() {
        long totalAllocationTime100 = 0;
        long nbAllocation100 = 0;
        long totalAllocationTime300 = 0;
        long nbAllocation300 = 0;
        long totalAllocationTime4 = 0;
        long nbAllocation4 = 0;
        long totalAllocationTime10000 = 0;
        long nbAllocation10000 = 0;
        long totalAllocationTime8200 = 0;
        long nbAllocation8200 = 0;
        long totalAllocationTime123456 = 0;
        long nbAllocation123456 = 0;
        long totalAllocationTime1 = 0;
        long nbAllocation1 = 0;
        long[] totals = new long[]{totalAllocationTime100, totalAllocationTime300, totalAllocationTime4,
                totalAllocationTime10000, totalAllocationTime8200, totalAllocationTime123456, totalAllocationTime1};
        long[] nbAllocs = new long[]{nbAllocation100, nbAllocation300, nbAllocation4,
                nbAllocation10000, nbAllocation8200, nbAllocation123456, nbAllocation1};

        long totalFreeTime = 0;
        long nbFree = 0;

        long nbWrite = 0;
        long totalWriteTime = 0;

        /*
        byte[] data100 = new byte[100];
        byte[] data300 = new byte[300];
        byte[] data4 = new byte[4];
        byte[] data10000 = new byte[10000];
        byte[] data8200 = new byte[8200];
        byte[] data123456 = new byte[123456];
        byte[] data1 = new byte[1];
        byte[][] datas = new byte[][]{data100, data300, data4, data10000, data8200, data123456, data1};
        */
        ByteBuffer[] bbs = new ByteBuffer[]{ByteBuffer.allocateDirect(100), ByteBuffer.allocateDirect(300),
                ByteBuffer.allocateDirect(4), ByteBuffer.allocateDirect(10000), ByteBuffer.allocateDirect(8200),
                ByteBuffer.allocateDirect(123456), ByteBuffer.allocateDirect(1)};

        List<Long> refs = new ArrayList<Long>();

        // Use a 100 MB cache
        Allocator a = new Allocator(400 * 1024 * 1024);
        OffheapMemoryAccess oma = new OffheapMemoryAccess(a);
        a.registerInMBeanServer(mbs);

        // the bench
        for (int i = 0; i < 10000000; i++) {
            // Do something
            // If less than 500 ref create
            if (refs.size() < 500) {
                int rand = r.nextInt(bbs.length);
                ByteBuffer data = bbs[rand];
                long start = System.nanoTime();
                long ref = alloc(a, refs, data);
                totals[rand] += System.nanoTime() - start;
                nbAllocs[rand]++;
                start = System.nanoTime();
                oma.store(ref, data);
                totalWriteTime = +System.nanoTime() - start;
                nbWrite++;

            }
            // If more than 2000 ref free
            else if (refs.size() > 1000) {
                int ref = r.nextInt(refs.size());
                long start = System.nanoTime();
                free(a, refs, refs.get(ref));
                totalFreeTime += System.nanoTime() - start;
                nbFree++;
            }
            // else create or free
            else {

                if (r.nextBoolean()) {
                    // Create
                    int rand = r.nextInt(bbs.length);
                    ByteBuffer data = bbs[rand];
                    long start = System.nanoTime();
                    long ref = alloc(a, refs, data);
                    totals[rand] += System.nanoTime() - start;
                    nbAllocs[rand]++;
                    start = System.nanoTime();
                    oma.store(ref, data);
                    totalWriteTime = +System.nanoTime() - start;
                    nbWrite++;
                } else {
                    int ref = r.nextInt(refs.size());
                    long start = System.nanoTime();
                    free(a, refs, refs.get(ref));
                    totalFreeTime += System.nanoTime() - start;
                    nbFree++;
                }
            }
        }

        for (int i = 0; i < totals.length; i++) {
            System.out.println("Means allocation : " + totals[i] / nbAllocs[i] + " ns");
        }
        System.out.println("Means free : " + totalFreeTime / nbFree + " ns");

        System.out.println("Total Write : " + totalWriteTime + " ns");
        System.out.println("Means Write : " + totalWriteTime / nbWrite + " ns");
        a.unRegisterInMBeanServer(mbs);
    }

    @Test
    public void multi_thread_bench() {
        // Use a 100 MB cache
        Allocator a = new Allocator(800 * 1024 * 1024);
        a.registerInMBeanServer(mbs);

        AllocRunnable ar1 = new AllocRunnable(a);
        AllocRunnable ar2 = new AllocRunnable(a);
        AllocRunnable ar3 = new AllocRunnable(a);
        AllocRunnable ar4 = new AllocRunnable(a);

        ExecutorService es = Executors.newCachedThreadPool();
        Future<Void> res1 = es.submit(ar1);
        Future<Void> res2 = es.submit(ar2);
        Future<Void> res3 = es.submit(ar3);
        Future<Void> res4 = es.submit(ar4);

        try {
            res1.get();
            res2.get();
            res3.get();
            res4.get();
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ExecutionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        for (int i = 0; i < ar1.totals.length; i++) {
            System.out.println("Means allocation : " + ar1.totals[i] / ar1.nbAllocs[i] + " ns");
        }
        System.out.println("Means free : " + ar1.totalFreeTime / ar1.nbFree + " ns");

        for (int i = 0; i < ar2.totals.length; i++) {
            System.out.println("Means allocation : " + ar2.totals[i] / ar2.nbAllocs[i] + " ns");
        }
        System.out.println("Means free : " + ar2.totalFreeTime / ar2.nbFree + " ns");

        for (int i = 0; i < ar3.totals.length; i++) {
            System.out.println("Means allocation : " + ar3.totals[i] / ar3.nbAllocs[i] + " ns");
        }
        System.out.println("Means free : " + ar3.totalFreeTime / ar3.nbFree + " ns");


        for (int i = 0; i < ar4.totals.length; i++) {
            System.out.println("Means allocation : " + ar4.totals[i] / ar4.nbAllocs[i] + " ns");
        }
        System.out.println("Means free : " + ar4.totalFreeTime / ar4.nbFree + " ns");


        a.unRegisterInMBeanServer(mbs);
    }

    private void free(Allocator a, List<Long> refs, long ref) {
        a.free(ref);
        refs.remove(ref);
    }

    private long alloc(Allocator a, List<Long> refs, ByteBuffer data) {
        long ref = a.alloc(data.capacity());
        refs.add(ref);
        return ref;
    }

    class AllocRunnable implements Callable<Void> {

        long totalFreeTime = 0;
        long nbFree = 0;

        byte[] data100 = new byte[100];
        byte[] data300 = new byte[300];
        byte[] data4 = new byte[4];
        byte[] data10000 = new byte[10000];
        byte[] data8200 = new byte[8200];
        byte[] data123456 = new byte[123456];
        byte[] data1 = new byte[1];
        byte[][] datas = new byte[][]{data100, data300, data4, data10000, data8200, data123456, data1};

        long[] totals = new long[]{0, 0, 0,
                0, 0, 0, 0};
        long[] nbAllocs = new long[]{0, 0, 0,
                0, 0, 0, 0};


        List<Long> refs = new ArrayList<Long>();


        // Use a 100 MB cache
        Allocator a;
        OffheapMemoryAccess oma = new OffheapMemoryAccess(a);

        AllocRunnable(Allocator a) {
            this.a = a;
        }

        @Override
        public Void call() {

            // the bench
            for (int i = 0; i < 10000000; i++) {
                // Do something
                // If less than 500 ref create
                if (refs.size() < 500) {
                    int rand = r.nextInt(datas.length);
                    byte[] buffer = datas[rand];
                    ByteBuffer data = ByteBuffer.allocateDirect(buffer.length);
                    long start = System.nanoTime();
                    long ref = alloc(a, refs, data);
                    totals[rand] += System.nanoTime() - start;
                    nbAllocs[rand]++;
                    oma.store(ref, data);

                }
                // If more than 2000 ref free
                else if (refs.size() > 1000) {
                    int ref = r.nextInt(refs.size());
                    long start = System.nanoTime();
                    free(a, refs, refs.get(ref));
                    totalFreeTime += System.nanoTime() - start;
                    nbFree++;
                }
                // else create or free
                else {

                    if (r.nextBoolean()) {
                        // Create
                        int rand = r.nextInt(datas.length);
                        byte[] buffer = datas[rand];
                        ByteBuffer data = ByteBuffer.allocateDirect(buffer.length);
                        long start = System.nanoTime();
                        long ref = alloc(a, refs, data);
                        totals[rand] += System.nanoTime() - start;
                        nbAllocs[rand]++;
                        oma.store(ref, data);
                    } else {
                        int ref = r.nextInt(refs.size());
                        long start = System.nanoTime();
                        free(a, refs, refs.get(ref));
                        totalFreeTime += System.nanoTime() - start;
                        nbFree++;
                    }
                }
            }


            return null;
        }

        public long getTotalFreeTime() {
            return totalFreeTime;
        }

        public long getNbFree() {
            return nbFree;
        }

        public long[] getTotals() {
            return totals;
        }

        public long[] getNbAllocs() {
            return nbAllocs;
        }
    }

}



