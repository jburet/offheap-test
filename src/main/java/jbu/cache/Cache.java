package jbu.cache;

public class Cache<K> {

    private String name;

    public Cache(String name) {
        this.name = name;
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
    public long put(K key, Object value) throws NullPointerException {
        return 0;
    }

    public Object get(K key) {
        return null;
    }

    public boolean remove(K key) {
        return false;
    }

    public Object getAndRemove(K key) {
        return null;
    }

}
