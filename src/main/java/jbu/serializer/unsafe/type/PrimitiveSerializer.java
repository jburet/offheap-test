package jbu.serializer.unsafe.type;

import jbu.offheap.LoadContext;
import jbu.offheap.StoreContext;
import jbu.serializer.unsafe.ClassDesc;
import jbu.serializer.unsafe.Type;

public class PrimitiveSerializer extends TypeSerializer {
    @Override
    public void serialize(Object sourceObject, StoreContext sc, ClassDesc cd, int fieldIndex) {
        sc.storeSomething(sourceObject, cd.offsets[fieldIndex], cd.types[fieldIndex].getLength());
    }

    @Override
    public void serialize(Object objectToSerialize, Type type, StoreContext sc) {
        // FIXME not implemented for primitive
    }

    @Override
    public void deserialize(LoadContext lc, ClassDesc cd, Object dest, int fieldIndex) {
        lc.loadPrimitive(dest, cd.offsets[fieldIndex], cd.types[fieldIndex].getLength());
    }

    @Override
    public Object deserialize(Type type, LoadContext lc) {
        // FIXME not implemented for primitive
        throw new RuntimeException("Cannot directly deserialize primitive");
    }
}
