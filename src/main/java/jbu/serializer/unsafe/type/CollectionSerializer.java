package jbu.serializer.unsafe.type;

import jbu.offheap.LoadContext;
import jbu.offheap.StoreContext;
import jbu.serializer.unsafe.ClassDesc;
import jbu.serializer.unsafe.Type;
import jbu.serializer.unsafe.UnsafeReflection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Serializer for all object implementing Collection interface. Work only for collection of primitive, primitive array, string
 * or TODO Entry (of primitive, primirive array, string....)
 */
public class CollectionSerializer extends TypeSerializer<Collection> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CollectionSerializer.class);

    @Override
    public void serialize(Object sourceObject, StoreContext sc, ClassDesc cd, int fieldIndex) {
        Collection c = (Collection) UnsafeReflection.getObject(cd.fields[fieldIndex], sourceObject);
        serialize(c, cd.types[fieldIndex], sc);
    }

    @Override
    public void serialize(Collection collection, Type type, StoreContext sc) {
        // FIXME Element cannot be null
        // FIXME If something is not serializable put the reference (long) instead
        int collectionLength = collection.size();
        // FIXME hope collection don't change during serialization
        // Store collection length on 4 bytes
        sc.storeInt(collectionLength);
        for (Object o : collection) {
            Type t = Type.resolveType(o.getClass());
            // Store type on 4 byte
            sc.storeInt(t.type);
            t.typeSerializer.serialize(o, t, sc);
        }
    }

    @Override
    public void deserialize(LoadContext lc, ClassDesc cd, Object dest, int fieldIndex) {
        Collection c = (Collection) UnsafeReflection.getObject(cd.fields[fieldIndex], dest);
        // If c don't exist... create them... By default use ArrayList
        if (c == null) {
            // find a implementation for c
            c = instanciate((Class<Collection>) cd.fields[fieldIndex].getType());
            UnsafeReflection.setObject(cd.fields[fieldIndex], dest, c);
        }

        // get collection length
        int collectionLength = lc.loadInt();
        for (int i = 0; i < collectionLength; i++) {
            // load type
            Type type = Type.resolveType(lc.loadInt());
            // deserialize
            Object element = type.typeSerializer.deserialize(type, lc);
            // put element in new collection
            c.add(element);
        }
    }

    @Override
    public Collection deserialize(Type type, LoadContext lc) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private Collection instanciate(Class<Collection> clazz) {
        // TODO.. do best I can but perhaps a better solution exists
        // If class is concrete
        if (!clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers())) {
            // try to instanciate
            try {
                return clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                // Cannot instanciate this type of collection...
                LOGGER.error("Cannot instance {}", clazz, e);
                return null;
            }
        } else {
            if (clazz.isAssignableFrom(List.class)) {
                return new ArrayList();
            }
            if (clazz.isAssignableFrom(Set.class)) {
                return new HashSet();
            }
            if (clazz.isAssignableFrom(NavigableSet.class)) {
                return new TreeSet();
            }
        }
        LOGGER.error("Cannot find implementation for {}", clazz);
        return null;
    }
}
