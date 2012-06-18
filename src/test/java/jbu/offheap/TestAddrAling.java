package jbu.offheap;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestAddrAling {

    @Test
    public void concat_separe_1(){
        long concat = AddrAlign.constructAddr(0, 0);
        assertEquals(0, AddrAlign.getBinId(concat));
        assertEquals(0, AddrAlign.getChunkId(concat));
    }

    @Test
    public void concat_separe_2(){
        long concat = AddrAlign.constructAddr(0, 1);
        assertEquals(0, AddrAlign.getBinId(concat));
        assertEquals(1, AddrAlign.getChunkId(concat));
    }

    @Test
    public void concat_separe_3(){
        long concat = AddrAlign.constructAddr(1, 1);
        assertEquals(1, AddrAlign.getBinId(concat));
        assertEquals(1, AddrAlign.getChunkId(concat));
    }

    @Test
    public void concat_separe_4(){
        long concat = AddrAlign.constructAddr(1, 0);
        assertEquals(1, AddrAlign.getBinId(concat));
        assertEquals(0, AddrAlign.getChunkId(concat));
    }



}
