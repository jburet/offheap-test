package jbu.serializer.unsafe.type;

import jbu.offheap.LoadContext;
import jbu.offheap.StoreContext;
import jbu.serializer.unsafe.ClassDesc;

public class PrimitiveSerializer extends TypeSerializer {
    @Override
    public void serialize(Object sourceObject, StoreContext sc, ClassDesc cd, int fieldIndex) {
        sc.storeSomething(sourceObject, cd.offsets[fieldIndex], cd.types[fieldIndex].getLength());
    }

    @Override
    public void deserialize(LoadContext lc, ClassDesc cd, Object dest, int index) {
        lc.loadPrimitive(dest, cd.offsets[index], cd.types[index].getLength());
    }
}
