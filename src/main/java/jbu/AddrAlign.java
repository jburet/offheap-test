package jbu;

public class AddrAlign {

    public static long constructAddr(int binId, int chunkId) {
        return ((long) binId << 32L) | ((long)chunkId & 0xffffffffL);
    }

    public static int getChunkId(long chunkAdr) {
        // masking is implicit
        return (int) (chunkAdr & 0xffffffffL);
    }

    public static int getBinId(long chunkAdr) {
        return (int) (chunkAdr >> 32L);
    }
}
