package jbu;

import java.util.LinkedList;

/**
 * Reference on a serialized and stored offheap object
 * Object can be split on chunk and stored on some bins.
 * We must keep location and order off this tuple bins/chunk
 * Can store and load data on him
 */
public class OffheapReference {

    final LinkedList<ChunkReference> refs = new LinkedList<ChunkReference>();
    final int size;

    public OffheapReference(int size, ChunkReference... chunksRef) {
        this.size = size;
        for (ChunkReference cr : chunksRef) {
            refs.add(cr);
        }
    }

    public void store(byte[] data) {
        if (data.length != size) {
            // FIXME Use explicit exception
            throw new RuntimeException("Not same size." + data.length + " instead " + size);
        }
        int currentOffset = 0;
        for (ChunkReference cr : refs) {
            int length = ((data.length - currentOffset) > cr.binRef.chunkSize) ? cr.binRef.chunkSize : (data.length - currentOffset);
            cr.binRef.storeInChunk(cr.chunkRef, data, currentOffset, length);
            currentOffset += length;
        }

    }

    public byte[] load() {
        byte[] res = new byte[size];
        int currentOffset = 0;
        for (ChunkReference cr : refs) {
            byte[] tmpData = cr.binRef.loadFromChunk(cr.chunkRef);
            System.arraycopy(tmpData, 0, res, currentOffset, tmpData.length);
            currentOffset += tmpData.length;
        }
        return res;
    }
}

class ChunkReference {

    final ByteBufferBins binRef;
    final int chunkRef;

    ChunkReference(ByteBufferBins binRef, int chunkRef) {
        this.binRef = binRef;
        this.chunkRef = chunkRef;
    }
}
