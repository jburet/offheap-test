package jbu.serializer;

import jbu.offheap.LoadContext;
import jbu.offheap.StoreContext;

public interface Serializer {
    void serialize(Object obj, StoreContext sc);

    Object deserialize(LoadContext lc);

    int estimateSerializedSize(Object obj);
}
