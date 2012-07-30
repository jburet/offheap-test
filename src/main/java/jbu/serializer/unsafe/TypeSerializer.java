package jbu.serializer.unsafe;

import jbu.offheap.LoadContext;
import jbu.offheap.StoreContext;

/**
 * Serializer for typeId T
 * Some concrete class not implements all methods
 *
 * @param <T>
 */
abstract class TypeSerializer<T> {

    /**
     * Serialize the field {@code fieldIndex} of {@code sourceObject} in {@link StoreContext} sc
     *
     * @param sourceObject object with field to serialize
     * @param sc           {@link StoreContext}
     * @param cd           {@link ClassDesc}
     * @param fieldIndex   Index of field to serialize
     */
    abstract void serialize(Object sourceObject, StoreContext sc, ClassDesc cd, int fieldIndex);

    /**
     * Serialize {@code objectToSerialize} of {@link Type} {@code typeId} inside {@link StoreContext} {@code sc}
     *
     * @param objectToSerialize
     * @param type
     * @param sc
     */
    abstract void serialize(T objectToSerialize, Type type, StoreContext sc);

    /**
     * Deserialize the field {@code fieldIndex} from {@link LoadContext} lc inside {@code dest}
     *
     * @param lc
     * @param cd
     * @param dest
     * @param fieldIndex
     */
    abstract void deserialize(LoadContext lc, ClassDesc cd, Object dest, int fieldIndex);

    /**
     * Deserialize from {@link LoadContext} {@code lc} the object of {@link Type} {@code typeId}.
     * Instanciate and return the object
     *
     * @param type
     * @param lc
     * @return
     */
    abstract T deserialize(Type type, LoadContext lc);

}
