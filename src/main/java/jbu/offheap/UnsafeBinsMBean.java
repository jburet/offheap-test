package jbu.offheap;

/**
 * Created with IntelliJ IDEA.
 * User: julienburet
 * Date: 15/06/12
 * Time: 23:13
 * To change this template use File | Settings | File Templates.
 */
public interface UnsafeBinsMBean {
    int getAllocatedChunks();

    int getUsedSize();
}
