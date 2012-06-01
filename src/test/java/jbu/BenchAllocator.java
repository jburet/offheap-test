package jbu;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BenchAllocator {

    @Test
    public void bench() {

        long totalTime = 0;
        long nbExecution = 0;
        byte[] data = new byte[1024 * 1024];

        List<OffheapReference> ors = new ArrayList<OffheapReference>();

        Allocator a = new Allocator();
        for (int i = 0; i < 100000; i++) {
            long start = System.nanoTime();
            OffheapReference oh = a.alloc(1024);
            ors.add(oh);
            totalTime += (System.nanoTime() - start);
            nbExecution++;
            if (i % 1000 == 0) {
                for (OffheapReference or : ors) {
                    a.free(or);
                }
                ors.clear();
            }
        }

        System.out.println("Means : " + totalTime / nbExecution + " ns");
    }
}
