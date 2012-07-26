package jbu.serializer.unsafe.type;

import jbu.offheap.LoadContext;
import jbu.offheap.StoreContext;
import jbu.serializer.unsafe.ClassDesc;
import jbu.serializer.unsafe.Type;
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
    public void serialize(Object objectToSerialize, Type type, StoreContext sc) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void deserialize(LoadContext lc, ClassDesc cd, Object dest, int fieldIndex) {
        // Do nothing
    }

    @Override
    public Object deserialize(Type type, LoadContext lc) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
