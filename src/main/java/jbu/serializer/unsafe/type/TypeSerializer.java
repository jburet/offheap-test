package jbu.serializer.unsafe.type;

import jbu.offheap.LoadContext;
import jbu.offheap.StoreContext;
import jbu.offheap.UnsafeUtil;
import jbu.serializer.unsafe.ClassDesc;
import sun.misc.Unsafe;

public abstract class TypeSerializer<T> {

    protected static final Unsafe unsafe = UnsafeUtil.unsafe;

    public abstract void serialize(Object sourceObject, StoreContext sc, ClassDesc cd, int fieldIndex);

    //public abstract void serialize(Object objectToSerialize, StoreContext sc);

    public abstract void deserialize(LoadContext lc, ClassDesc cd, Object dest, int index);

}
