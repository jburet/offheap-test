package jbu.serializer.unsafe;

import jbu.Primitive;
import jbu.UnsafeReflection;

import static jbu.Primitive.*;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

abstract class Type<T> {

    private static final Map<Integer, Type> MAP_BY_TYPE_REF = new HashMap<Integer, Type>();

    // Primitive Type
    private static final PrimitiveSerializer PRIMITIVE_SERIALIZER = new PrimitiveSerializer();
    private static final PrimitiveArraySerializer PRIMITIVE_ARRAY_SERIALIZER = new PrimitiveArraySerializer();

    static final Type BOOLEAN = new PrimitiveType(1, BOOLEAN_LENGTH, PRIMITIVE_SERIALIZER, boolean.class);
    static final Type CHAR = new PrimitiveType(2, CHAR_LENGTH, PRIMITIVE_SERIALIZER, char.class);
    static final Type BYTE = new PrimitiveType(3, BYTE_LENGTH, PRIMITIVE_SERIALIZER, byte.class);
    static final Type SHORT = new PrimitiveType(4, SHORT_LENGTH, PRIMITIVE_SERIALIZER, short.class);
    static final Type INT = new PrimitiveType(5, INT_LENGTH, PRIMITIVE_SERIALIZER, int.class);
    static final Type LONG = new PrimitiveType(6, LONG_LENGTH, PRIMITIVE_SERIALIZER, long.class);
    static final Type FLOAT = new PrimitiveType(7, FLOAT_LENGTH, PRIMITIVE_SERIALIZER, float.class);
    static final Type DOUBLE = new PrimitiveType(8, LONG_LENGTH, PRIMITIVE_SERIALIZER, double.class);


    final int type;
    final int arrayProof;
    final int typeSize;
    final boolean isArray;
    final boolean isPrimitive;
    final TypeSerializer typeSerializer;
    final Class<?> clazz;


    // Array type
    static Type BOOLEAN_ARRAY(int arrayProof) {
        return new ArrayType(11, arrayProof, (byte) 1, PRIMITIVE_ARRAY_SERIALIZER, boolean.class);
    }

    static Type CHAR_ARRAY(int arrayProof) {
        return new ArrayType(12, arrayProof, (byte) 2, PRIMITIVE_ARRAY_SERIALIZER, char.class);
    }

    static Type BYTE_ARRAY(int arrayProof) {
        return new ArrayType(13, arrayProof, (byte) 1, PRIMITIVE_ARRAY_SERIALIZER, byte.class);
    }

    static Type SHORT_ARRAY(int arrayProof) {
        return new ArrayType(14, arrayProof, (byte) 2, PRIMITIVE_ARRAY_SERIALIZER, short.class);
    }

    static Type INT_ARRAY(int arrayProof) {
        return new ArrayType(15, arrayProof, (byte) 4, PRIMITIVE_ARRAY_SERIALIZER, int.class);
    }

    static Type LONG_ARRAY(int arrayProof) {
        return new ArrayType(16, arrayProof, (byte) 8, PRIMITIVE_ARRAY_SERIALIZER, long.class);
    }

    static Type FLOAT_ARRAY(int arrayProof) {
        return new ArrayType(17, arrayProof, (byte) 4, PRIMITIVE_ARRAY_SERIALIZER, float.class);
    }

    static Type DOUBLE_ARRAY(int arrayProof) {
        return new ArrayType(18, arrayProof, (byte) 8, PRIMITIVE_ARRAY_SERIALIZER, double.class);
    }

    // Collection
    static final Type COLLECTION = new ObjectType<Collection>(5000, new CollectionSerializer(), Collection.class) {

        @Override
        int serializedSize(Collection obj) {
            // FIXME test with a first bad implementation
            int size = 0;
            // collection type
            size += INT_LENGTH;

            for (Object elt : obj) {
                // element type
                size += INT_LENGTH;
                if (elt instanceof String) {
                    // String size
                    size += INT_LENGTH;
                    size += ((String) elt).length() * CHAR_LENGTH;
                }
            }
            return size;
        }
    };


    // Standard java type
    static final Type STRING = new ObjectType<String>(10000, new StringSerializer(), String.class) {
        @Override
        int serializedSize(String obj) {
            return ((StringSerializer) typeSerializer).serializedSize(obj);
        }
    };
    static final Type OBJECT = new ObjectType<Object>(0, new DefaultSerializer(), Object.class) {

        @Override
        int serializedSize(Object obj) {
            // No serialization
            return 0;
        }
    };

    static {
        MAP_BY_TYPE_REF.put(0, OBJECT);
        MAP_BY_TYPE_REF.put(1, BOOLEAN);
        MAP_BY_TYPE_REF.put(2, CHAR);
        MAP_BY_TYPE_REF.put(3, BYTE);
        MAP_BY_TYPE_REF.put(4, SHORT);
        MAP_BY_TYPE_REF.put(5, INT);
        MAP_BY_TYPE_REF.put(6, LONG);
        MAP_BY_TYPE_REF.put(7, FLOAT);
        MAP_BY_TYPE_REF.put(8, DOUBLE);
        MAP_BY_TYPE_REF.put(1001, BOOLEAN_ARRAY(1));
        MAP_BY_TYPE_REF.put(1501, CHAR_ARRAY(1));
        MAP_BY_TYPE_REF.put(2001, BYTE_ARRAY(1));
        MAP_BY_TYPE_REF.put(2501, SHORT_ARRAY(1));
        MAP_BY_TYPE_REF.put(3001, INT_ARRAY(1));
        MAP_BY_TYPE_REF.put(3501, LONG_ARRAY(1));
        MAP_BY_TYPE_REF.put(4001, FLOAT_ARRAY(1));
        MAP_BY_TYPE_REF.put(4501, DOUBLE_ARRAY(1));
        MAP_BY_TYPE_REF.put(5000, COLLECTION);
        MAP_BY_TYPE_REF.put(10000, STRING);

    }

    static Type resolveType(Field f) {
        return resolveType(f.getType());
    }

    static Type resolveType(Class<?> clazz) {
        if (clazz.isPrimitive()) {
            String tname = clazz.getName();
            if (tname.equals("boolean")) {
                return BOOLEAN;
            }
            if (tname.equals("char")) {
                return CHAR;
            }
            if (tname.equals("byte")) {
                return BYTE;
            }
            if (tname.equals("short")) {
                return SHORT;
            }
            if (tname.equals("int")) {
                return INT;
            }
            if (tname.equals("long")) {
                return LONG;
            }
            if (tname.equals("float")) {
                return FLOAT;
            }
            if (tname.equals("double")) {
                return DOUBLE;
            }
        } else if (clazz.isArray()) {
            String tname = clazz.getName();
            String code = getTypeCode(tname);
            short arrayProof = getProof(tname);
            if (code.equals("Z")) {
                return BOOLEAN_ARRAY(arrayProof);
            }
            if (code.equals("C")) {
                return CHAR_ARRAY(arrayProof);
            }
            if (code.equals("B")) {
                return BYTE_ARRAY(arrayProof);
            }
            if (code.equals("S")) {
                return SHORT_ARRAY(arrayProof);
            }
            if (code.equals("I")) {
                return INT_ARRAY(arrayProof);
            }
            if (code.equals("J")) {
                return LONG_ARRAY(arrayProof);
            }
            if (code.equals("F")) {
                return FLOAT_ARRAY(arrayProof);
            }
            if (code.equals("D")) {
                return DOUBLE_ARRAY(arrayProof);
            }
            // COLLECTION
        } else if (Collection.class.isAssignableFrom(clazz)) {
            return COLLECTION;
        } else {
            String tname = clazz.getName();
            if (tname.equals("java.lang.String")) {
                return STRING;
            }
        }
        return OBJECT;
    }

    static Type resolveType(int i) {
        return MAP_BY_TYPE_REF.get(i);
    }

    private static short getProof(String tname) {
        return (short) (tname.length() - 1);
    }

    private static String getTypeCode(String tname) {
        return tname.substring(tname.length() - 1);
    }


    // Construct primitive (array or not) type
    protected Type(int type, int arrayProof, int typeSize, boolean isArray, TypeSerializer typeSerializer, Class<?> clazz) {
        this.type = type;
        this.arrayProof = arrayProof;
        this.typeSize = typeSize;
        this.isArray = isArray;
        this.isPrimitive = true;
        this.typeSerializer = typeSerializer;
        this.clazz = clazz;
    }

    // Construct object type
    protected Type(int type, TypeSerializer typeSerializer, Class<?> clazz) {
        this.type = type;
        this.arrayProof = -1;
        this.typeSize = -1;
        this.isArray = false;
        this.isPrimitive = false;
        this.typeSerializer = typeSerializer;
        this.clazz = clazz;
    }


    int getLength(int... arrayLength) {
        // FIXME Validate coherence (arraylength only when array and size correspond to declaration ? )
        // or not...

        // If l empty Is primitive return standard length
        if (arrayLength.length == 0) {
            return typeSize;
        }

        // If l has length > 0 is an array...
        int totalLength = 0;
        for (int l : arrayLength) {
            totalLength += l;
        }
        return totalLength * typeSize;
    }


    abstract int serializedSize(T obj);
}

class PrimitiveType extends Type<Object> {

    PrimitiveType(int type, int typeSize, TypeSerializer typeSerializer, Class<?> clazz) {
        super(type, 0, typeSize, false, typeSerializer, clazz);
    }

    @Override
    int serializedSize(Object obj) {
        return typeSize;
    }
}

class ArrayType extends Type<Object> {

    ArrayType(int type, int arrayProof, int typeSize, TypeSerializer typeSerializer, Class<?> clazz) {
        super(type, arrayProof, typeSize, true, typeSerializer, clazz);
    }

    @Override
    int serializedSize(Object obj) {
        return UnsafeReflection.getArraySizeContentInMem(obj) + Primitive.INT_LENGTH;
    }
}

abstract class ObjectType<T> extends Type<T> {

    ObjectType(int type, TypeSerializer typeSerializer, Class<?> clazz) {
        super(type, typeSerializer, clazz);
    }
}

