package jbu.serializer.unsafe.type;

import jbu.offheap.LoadContext;
import jbu.offheap.StoreContext;
import jbu.serializer.unsafe.ClassDesc;
import jbu.serializer.unsafe.Type;
import jbu.serializer.unsafe.UnsafeReflection;

import java.util.Collection;

/**
 * Serializer for all object implementing Collection interface. Work only for collection of primitive, primitive array, string
 * or TODO Entry (of primitive, primirive array, string....)
 */
public class CollectionSerializer extends TypeSerializer<Collection> {
    @Override
    public void serialize(Object sourceObject, StoreContext sc, ClassDesc cd, int fieldIndex) {
        Collection c = (Collection) UnsafeReflection.getObject(cd.fields[fieldIndex], sourceObject);
        // FIXME Element cannot be null
        // FIXME If something is not serializable put the reference (long) instead
        int collectionLength = c.size();
        // FIXME hope collection don't change during serialization
        for (Object o : c) {
            Type t = Type.resolveType(o.getClass());
            //t.typeSerializer.serialize(o, sc);
        }
    }

    @Override
    public void deserialize(LoadContext lc, ClassDesc cd, Object dest, int index) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
