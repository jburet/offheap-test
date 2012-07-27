package jbu.serializer.unsafe;

import jbu.exception.NotImplementedException;
import jbu.offheap.LoadContext;
import jbu.offheap.StoreContext;

class PrimitiveSerializer extends TypeSerializer {
    @Override
    void serialize(Object sourceObject, StoreContext sc, ClassDesc cd, int fieldIndex) {
        sc.storeSomething(sourceObject, cd.offsets[fieldIndex], cd.types[fieldIndex].getLength());
    }

    @Override
    void serialize(Object objectToSerialize, Type type, StoreContext sc) {
        // FIXME not implemented for primitive
    }

    @Override
    void deserialize(LoadContext lc, ClassDesc cd, Object dest, int fieldIndex) {
        lc.loadPrimitive(dest, cd.offsets[fieldIndex], cd.types[fieldIndex].getLength());
    }

    @Override
    Object deserialize(Type type, LoadContext lc) {
        // FIXME not implemented for primitive
        throw new NotImplementedException("Cannot directly deserialize primitive");
    }
}
