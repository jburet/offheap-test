package jbu.serializer.unsafe.type;

import jbu.offheap.LoadContext;
import jbu.offheap.StoreContext;
import jbu.offheap.UnsafeUtil;
import jbu.serializer.unsafe.ClassDesc;
import jbu.serializer.unsafe.Type;
import sun.misc.Unsafe;

public abstract class TypeSerializer<T> {

    protected static final Unsafe unsafe = UnsafeUtil.unsafe;

    public abstract void serialize(Object sourceObject, StoreContext sc, ClassDesc cd, int fieldIndex);

    public abstract void serialize(T objectToSerialize, Type type, StoreContext sc);

    public abstract void deserialize(LoadContext lc, ClassDesc cd, Object dest, int fieldIndex);

    public abstract T deserialize(Type type, LoadContext lc);
}
