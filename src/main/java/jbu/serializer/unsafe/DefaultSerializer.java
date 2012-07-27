package jbu.serializer.unsafe;

import jbu.offheap.LoadContext;
import jbu.offheap.StoreContext;

/**
 * Used only when don't know how to serialize... Put the reference
 */
class DefaultSerializer extends TypeSerializer {
    @Override
    void serialize(Object sourceObject, StoreContext sc, ClassDesc cd, int fieldIndex) {
        // DO nothing
    }

    @Override
    void serialize(Object objectToSerialize, Type type, StoreContext sc) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    void deserialize(LoadContext lc, ClassDesc cd, Object dest, int fieldIndex) {
        // Do nothing
    }

    @Override
    Object deserialize(Type type, LoadContext lc) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
