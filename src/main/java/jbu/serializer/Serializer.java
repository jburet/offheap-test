package jbu.serializer;

import jbu.exception.CannotDeserializeException;
import jbu.offheap.LoadContext;
import jbu.offheap.StoreContext;

/**
 * High level Serializer.
 * Serialize each field of object
 * Can serialize an object directly in Offheap memory.
 */
public interface Serializer {
    /**
     * Serialize the object {@code obj} in memory allocated by {@link StoreContext} sc
     *
     * @param obj
     * @param sc
     */
    void serialize(Object obj, StoreContext sc);

    /**
     * Deserialize and instanciate in Heap the next object allocated in OffHeap in the {@link LoadContext} lc
     *
     * @param lc
     * @return A new deserialized Object
     */
    Object deserialize(LoadContext lc) throws CannotDeserializeException;

    /**
     * Return the size of {@obj} after serialization
     *
     * @param obj
     * @return
     */
    int calculateSerializedSize(Object obj);
}
