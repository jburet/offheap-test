package jbu.offheap;

public interface AllocatorMBean {
    int getAllocatedMemory();

    int getUsedMemory();

    long getNbAllocation();

    long getNbFree();
}
