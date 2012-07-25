package jbu.cache;

import jbu.offheap.Allocator;
import jbu.serializer.UnsafePrimitiveBeanSerializer;

import java.util.HashMap;
import java.util.Map;

public class Cache<K, V> {

    private final String name;
    private final Allocator allocator;
    private final UnsafePrimitiveBeanSerializer pbs;
    private final Map<K, Long> keys;

    public Cache(String name, int approxSize) {
        this.name = name;
        this.allocator = new Allocator(approxSize);
        this.pbs = new UnsafePrimitiveBeanSerializer();
        this.keys = new HashMap<K, Long>();
    }

    public Cache(String name, Allocator allocator) {
        this.name = name;
        this.allocator = allocator;
        this.pbs = new UnsafePrimitiveBeanSerializer();
        this.keys = new HashMap<K, Long>();
    }

    /**
     * Store the Object value with the key K.
     * This method return addr where K is serialized.
     * If key already exist the object is replaced by the new value
     *
     * @param key
     * @param value
     * @return adresse where value is stored
     * @throws NullPointerException If value is null
     */
    public long put(K key, V value) throws NullPointerException {
        long addr = allocator.alloc(pbs.estimateSerializedSize(value));
        pbs.serialize(value, allocator.getStoreContext(addr));
        keys.put(key, addr);
        return addr;
    }

    public V get(K key) {
        return (V) pbs.deserialize(allocator.getLoadContext(keys.get(key)));
    }

    public boolean remove(K key) {
        return false;
    }

    public V getAndRemove(K key) {
        return null;
    }

    /**
     * Remove all cached object
     */
    public void clean() {
        for (Long add : keys.values()) {
            allocator.free(add);
        }
    }
}
