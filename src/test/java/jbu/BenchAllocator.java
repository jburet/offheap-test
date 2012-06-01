package jbu;

import org.junit.Ignore;
import org.junit.Test;

import java.util.*;

public class BenchAllocator {

    @Test
    @Ignore
    public void bench() {

        long totalTime = 0;
        long nbExecution = 0;
        byte[] data = new byte[1024];
        List<Long> chunks = new ArrayList<Long>();

        Allocator a = new Allocator(100 * 1024 * 1024);
        for (int i = 0; i < 100000; i++) {
            long start = System.nanoTime();
            long firstChunk = a.alloc(1024);
            a.store(firstChunk, data);
            totalTime += (System.nanoTime() - start);
            nbExecution++;
            chunks.add(firstChunk);
            if (i % 10000 == 0) {
                for (long firstChunkId : chunks) {
                    a.free(firstChunkId);
                }
                chunks.clear();
            }
        }

        System.out.println("Means : " + totalTime / nbExecution + " ns");
    }

    Random r = new Random();

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

        long totalWriteTime = 0;
        long nbWrite = 0;
        long totalReadTime = 0;
        long nbRead = 0;
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

        List<Long> refs = new ArrayList<Long>();

        // Use a 100 MB cache
        Allocator a = new Allocator(400 * 1024 * 1024);

        // the bench
        for (int i = 0; i < 1000000; i++) {
            // Do something
            // If less than 500 ref create
            if (refs.size() < 50) {
                int rand = r.nextInt(datas.length);
                byte[] data = datas[rand];
                long start = System.nanoTime();
                long ref = alloc(a, refs, data);
                totals[rand] += System.nanoTime() - start;
                nbAllocs[rand]++;
                a.store(ref, data);

            }
            // If more than 2000 ref free
            else if (refs.size() > 100) {
                int ref = r.nextInt(refs.size());
                long start = System.nanoTime();
                free(a, refs, refs.get(ref));
                totalFreeTime += System.nanoTime() - start;
                nbFree++;
            }
            // else create of free
            else {
                if (r.nextBoolean()) {
                    // Create
                    int rand = r.nextInt(datas.length);
                    byte[] data = datas[rand];
                    long start = System.nanoTime();
                    long ref = alloc(a, refs, data);
                    totals[rand] += System.nanoTime() - start;
                    nbAllocs[rand]++;
                    a.store(ref, data);
                } else {
                    // free
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
    }

    private void free(Allocator a, List<Long> refs, long ref) {
        a.free(ref);
        refs.remove(ref);
    }

    private long alloc(Allocator a, List<Long> refs, byte[] data) {
        long ref = a.alloc(data.length);
        refs.add(ref);
        return ref;
    }

}
