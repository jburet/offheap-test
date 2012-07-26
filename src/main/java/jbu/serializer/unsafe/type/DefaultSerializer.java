package jbu.serializer.unsafe.type;

import jbu.offheap.LoadContext;
import jbu.offheap.StoreContext;
import jbu.serializer.unsafe.ClassDesc;
import jbu.serializer.unsafe.UnsafeReflection;

/**
 * Used only when don't know how to serialize... Put the reference
 */
public class DefaultSerializer extends TypeSerializer {
    @Override
    public void serialize(Object sourceObject, StoreContext sc, ClassDesc cd, int fieldIndex) {
        // DO nothing
    }

    @Override
    public void deserialize(LoadContext lc, ClassDesc cd, Object dest, int index) {
        // Do nothing
    }
}
