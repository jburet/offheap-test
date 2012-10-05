package jbu.serializer.unsafe;

import jbu.offheap.LoadContext;
import jbu.offheap.StoreContext;
import jbu.UnsafeReflection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Serializer for all object implementing Collection interface. Work only for collection of primitive, primitive array, string
 * or TODO Entry (of primitive, primitive array, string....)
 */
class CollectionSerializer extends TypeSerializer<Collection> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CollectionSerializer.class);

    @Override
    void serialize(Object sourceObject, StoreContext sc, ClassDesc cd, int fieldIndex) {
        Collection c = (Collection) UnsafeReflection.getObject(cd.fields[fieldIndex], sourceObject);
        serialize(c, cd.types[fieldIndex], sc);
    }

    @Override
    void serialize(Collection collection, Type type, StoreContext sc) {
        // FIXME Element cannot be null
        // FIXME If something is not serializable put the reference (long) instead
        // register collection implementation
        ClassDesc cd = ClassDesc.resolveByClass(collection.getClass());
        // Store class reference
        sc.storeInt(cd.classReference);
        int collectionLength = collection.size();
        // FIXME hope collection don't change during serialization
        // Store collection length on 4 bytes
        sc.storeInt(collectionLength);
        for (Object o : collection) {
            Type t = Type.resolveType(o.getClass());
            // Store typeId on 4 byte
            sc.storeInt(t.typeId);
            t.typeSerializer.serialize(o, t, sc);
        }
    }

    @Override
    void deserialize(LoadContext lc, ClassDesc cd, Object dest, int fieldIndex) {
        // Get concrete class of collection
        ClassDesc cs = ClassDesc.resolveByRef(lc.loadInt());
        Collection c = (Collection) UnsafeReflection.getObject(cd.fields[fieldIndex], dest);
        // If c don't exist... create them... By default use ArrayList
        if (c == null) {
            // instanciate it
            c = instanciate((Class<Collection>) cs.clazz);
            UnsafeReflection.setObject(cd.fields[fieldIndex], dest, c);
        }

        // get collection length
        deserialize(lc, c);
    }

    @Override
    Collection deserialize(Type type, LoadContext lc) {
        ClassDesc cs = ClassDesc.resolveByRef(lc.loadInt());
        Collection c = instanciate((Class<Collection>) cs.clazz);
        deserialize(lc, c);
        return c;
    }

    private void deserialize(LoadContext lc, Collection c) {
        int collectionLength = lc.loadInt();
        for (int i = 0; i < collectionLength; i++) {
            // load typeId
            Type type = Type.resolveType(lc.loadInt());
            // deserialize
            Object element = type.typeSerializer.deserialize(type, lc);
            // put element in new collection
            c.add(element);
        }
    }

    private Collection instanciate(Class<Collection> clazz) {
        // TODO.. Perhaps a best solution exists
        // If class is concrete
        if (!clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers())) {
            // try to instanciate
            try {
                return clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                // Cannot instanciate this typeId of collection...
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
