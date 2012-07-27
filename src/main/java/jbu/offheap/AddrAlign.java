package jbu.offheap;

final class AddrAlign {

    private AddrAlign() {
    }

    static long constructAddr(int binId, int chunkId) {
        return ((long) binId << 32L) | ((long) chunkId & 0xffffffffL);
    }

    static int getChunkId(long chunkAdr) {
        // masking is implicit
        return (int) (chunkAdr & 0xffffffffL);
    }

    static int getBinId(long chunkAdr) {
        return (int) (chunkAdr >> 32L);
    }
}
