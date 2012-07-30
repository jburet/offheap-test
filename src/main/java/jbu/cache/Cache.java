package jbu.cache;

import jbu.exception.CannotDeserializeException;
import jbu.offheap.Allocator;
import jbu.serializer.Serializer;
import jbu.serializer.unsafe.UnsafePrimitiveBeanSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public final class Cache<K, V> implements CacheMBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(Cache.class);

    private final String name;
    private final Allocator allocator;
    private final Serializer pbs;
    private final Map<K, Long> keys;

    public Cache(String name, int approxSize) {
        this(name, new Allocator(approxSize));
    }

    public Cache(String name, Allocator allocator) {
        this(name, allocator, new UnsafePrimitiveBeanSerializer());
    }

    public Cache(String name, Allocator allocator, Serializer serializer) {
        this.name = name;
        this.allocator = allocator;
        this.pbs = serializer;
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
     * @throws IllegalArgumentException If value is null
     */
    public long put(K key, V value) {
        if (value == null) {
            throw new IllegalArgumentException("Value should not be null");
        }
        long addr = allocator.alloc(pbs.calculateSerializedSize(value));
        pbs.serialize(value, allocator.getStoreContext(addr));
        keys.put(key, addr);
        return addr;
    }

    public V get(K key) {
        try {
            return (V) pbs.deserialize(allocator.getLoadContext(keys.get(key)));
        } catch (CannotDeserializeException e) {
            LOGGER.error("Cannot deserialize value", e);
            return null;
        }
    }

    public boolean remove(K key) {
        Long addr = keys.remove(key);
        if (addr != null) {
            allocator.free(addr);
            return true;
        }
        return false;
    }

    public V getAndRemove(K key) {
        Long addr = keys.remove(key);
        if (addr != null) {
            V res = null;
            try {
                res = (V) pbs.deserialize(allocator.getLoadContext(addr));
            } catch (CannotDeserializeException e) {
                LOGGER.error("Cannot deserialize value", e);
            }
            allocator.free(addr);
            return res;
        }
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


    // JMX Accessor
    @Override
    public String getName() {
        return name;
    }
}
