package jbu.serializer.unsafe.type;

import jbu.offheap.LoadContext;
import jbu.offheap.StoreContext;

public abstract class TypeSerializer<T> {

    public abstract void serialize(Object sourceObject, StoreContext sc, ClassDesc cd, int fieldIndex);

    public abstract void serialize(T objectToSerialize, Type type, StoreContext sc);

    public abstract void deserialize(LoadContext lc, ClassDesc cd, Object dest, int fieldIndex);

    public abstract T deserialize(Type type, LoadContext lc);
}
