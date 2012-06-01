package jbu;

import java.util.HashMap;
import java.util.Map;

/**
 * Frontend of off-heap store
 */
public class Allocator {

    private final Map<Integer, ByteBufferBins> bins = new HashMap<Integer, ByteBufferBins>();

    public Allocator() {
        // Create a 100KB storage
        bins.put(1024, new ByteBufferBins(1000000, 1024));
    }

    public OffheapReference alloc(int memorySize) {

        // allocate a 1024 chunk
        ByteBufferBins bin = bins.get(1024);
        int nbChunck = memorySize / 1024;
        if (memorySize % 1024 > 0) {
            nbChunck++;
        }
        int[] chunksId = bin.allocateNChunk(nbChunck);
        ChunkReference[] crs = new ChunkReference[chunksId.length];
        for (int i = 0; i < crs.length; i++) {
            crs[i] = new ChunkReference(bin, chunksId[i]);
        }
        return new OffheapReference(memorySize, crs);
    }

    public void free(OffheapReference or) {
        for (ChunkReference cr : or.refs) {
            cr.binRef.freeChunk(cr.chunkRef);
        }
    }
}
