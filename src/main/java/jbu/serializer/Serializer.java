package jbu.serializer;

import jbu.exception.CannotDeserializeException;
import jbu.offheap.LoadContext;
import jbu.offheap.StoreContext;

public interface Serializer {
    void serialize(Object obj, StoreContext sc);

    Object deserialize(LoadContext lc) throws CannotDeserializeException;

    int estimateSerializedSize(Object obj);
}
