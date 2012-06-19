package jbu.map;

import jbu.offheap.Allocator;

import java.io.*;
import java.util.concurrent.ConcurrentSkipListMap;

public class MapCache<K, V extends Serializable> {

    private Allocator allocator = new Allocator(100 * 1024 * 1024, true);

    // Internally use ConcurrentSkipListMap
    private ConcurrentSkipListMap<Object, OffheapObject> cache = new ConcurrentSkipListMap<Object, OffheapObject>();

    public void put(K key, V value) {
        ByteArrayOutputStream baot = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baot);
            oos.writeObject(value);
            byte[] serObj = baot.toByteArray();
            int serSize = serObj.length;
            long addr = allocator.alloc2(serSize);
            allocator.store(addr, serObj);
        //    cache.put(key, new OffheapObject(value.getClass(), addr, serSize));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public V get(K key) {
        OffheapObject oho = cache.get(key);
        if (oho != null) {
            ByteArrayInputStream bais = new ByteArrayInputStream(allocator.load(oho.addr));
            try {
                ObjectInputStream ois = new ObjectInputStream(bais);
                return (V) ois.readObject();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (ClassNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return null;
    }
}
