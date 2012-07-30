package jbu.serializer.unsafe;

import jbu.Primitive;
import jbu.UnsafeReflection;

import static jbu.Primitive.*;

import java.lang.reflect.Field;
import java.util.*;

abstract class Type<T> {

    private static final Map<Integer, Type> MAP_BY_TYPE_REF = new HashMap<>();
    private static final Map<Class, Type> MAP_BY_CLASS = new HashMap<>();

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


    // Array typeId
    static Type BOOLEAN_ARRAY(int arrayProof) {
        return new ArrayType(1001, arrayProof, (byte) 1, PRIMITIVE_ARRAY_SERIALIZER, boolean.class);
    }

    static Type CHAR_ARRAY(int arrayProof) {
        return new ArrayType(1501, arrayProof, (byte) 2, PRIMITIVE_ARRAY_SERIALIZER, char.class);
    }

    static Type BYTE_ARRAY(int arrayProof) {
        return new ArrayType(2001, arrayProof, (byte) 1, PRIMITIVE_ARRAY_SERIALIZER, byte.class);
    }

    static Type SHORT_ARRAY(int arrayProof) {
        return new ArrayType(2501, arrayProof, (byte) 2, PRIMITIVE_ARRAY_SERIALIZER, short.class);
    }

    static Type INT_ARRAY(int arrayProof) {
        return new ArrayType(3001, arrayProof, (byte) 4, PRIMITIVE_ARRAY_SERIALIZER, int.class);
    }

    static Type LONG_ARRAY(int arrayProof) {
        return new ArrayType(3501, arrayProof, (byte) 8, PRIMITIVE_ARRAY_SERIALIZER, long.class);
    }

    static Type FLOAT_ARRAY(int arrayProof) {
        return new ArrayType(4001, arrayProof, (byte) 4, PRIMITIVE_ARRAY_SERIALIZER, float.class);
    }

    static Type DOUBLE_ARRAY(int arrayProof) {
        return new ArrayType(4501, arrayProof, (byte) 8, PRIMITIVE_ARRAY_SERIALIZER, double.class);
    }

    // Wrapper typeId
    static final Type BOOLEAN_CLASS = new PrimitiveWrapperType(21, BOOLEAN_LENGTH, new PrimitiveWrapperSerializer<Boolean>(Boolean.class), Boolean.class);
    static final Type CHAR_CLASS = new PrimitiveWrapperType(22, CHAR_LENGTH, new PrimitiveWrapperSerializer<Character>(Character.class), Character.class);
    static final Type BYTE_CLASS = new PrimitiveWrapperType(23, BYTE_LENGTH, new PrimitiveWrapperSerializer<Byte>(Byte.class), Byte.class);
    static final Type SHORT_CLASS = new PrimitiveWrapperType(24, SHORT_LENGTH, new PrimitiveWrapperSerializer<Short>(Short.class), Short.class);
    static final Type INT_CLASS = new PrimitiveWrapperType(25, INT_LENGTH, new PrimitiveWrapperSerializer<Integer>(Integer.class), Integer.class);
    static final Type LONG_CLASS = new PrimitiveWrapperType(26, LONG_LENGTH, new PrimitiveWrapperSerializer<Long>(Long.class), Long.class);
    static final Type FLOAT_CLASS = new PrimitiveWrapperType(27, FLOAT_LENGTH, new PrimitiveWrapperSerializer<Float>(Float.class), Float.class);
    static final Type DOUBLE_CLASS = new PrimitiveWrapperType(28, LONG_LENGTH, new PrimitiveWrapperSerializer<Double>(Double.class), Double.class);

    // Collection
    static final Type COLLECTION = new ObjectType<AbstractCollection>(5000, new CollectionSerializer(), AbstractCollection.class) {

        @Override
        int serializedSize(AbstractCollection obj) {
            // FIXME test with a first bad implementation
            int size = 0;
            // collection typeId
            size += INT_LENGTH;
            for (Object elt : obj) {
                // element typeId
                size += INT_LENGTH;
                Type eltType = Type.resolveType(elt.getClass());
                size += eltType.serializedSize(elt);
            }
            return size;
        }
    };


    // Standard java typeId
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
        MAP_BY_TYPE_REF.put(OBJECT.typeId, OBJECT);
        MAP_BY_TYPE_REF.put(BOOLEAN.typeId, BOOLEAN);
        MAP_BY_TYPE_REF.put(CHAR.typeId, CHAR);
        MAP_BY_TYPE_REF.put(BYTE.typeId, BYTE);
        MAP_BY_TYPE_REF.put(SHORT.typeId, SHORT);
        MAP_BY_TYPE_REF.put(INT.typeId, INT);
        MAP_BY_TYPE_REF.put(LONG.typeId, LONG);
        MAP_BY_TYPE_REF.put(FLOAT.typeId, FLOAT);
        MAP_BY_TYPE_REF.put(DOUBLE.typeId, DOUBLE);
        MAP_BY_TYPE_REF.put(BOOLEAN_CLASS.typeId, BOOLEAN_CLASS);
        MAP_BY_TYPE_REF.put(CHAR_CLASS.typeId, CHAR_CLASS);
        MAP_BY_TYPE_REF.put(BYTE_CLASS.typeId, BYTE_CLASS);
        MAP_BY_TYPE_REF.put(SHORT_CLASS.typeId, SHORT_CLASS);
        MAP_BY_TYPE_REF.put(INT_CLASS.typeId, INT_CLASS);
        MAP_BY_TYPE_REF.put(LONG_CLASS.typeId, LONG_CLASS);
        MAP_BY_TYPE_REF.put(FLOAT_CLASS.typeId, FLOAT_CLASS);
        MAP_BY_TYPE_REF.put(DOUBLE_CLASS.typeId, DOUBLE_CLASS);
        for (int i = 1; i <= 255; i++) {
            MAP_BY_TYPE_REF.put(1000 + i, BOOLEAN_ARRAY(i));
            MAP_BY_TYPE_REF.put(1500 + i, CHAR_ARRAY(i));
            MAP_BY_TYPE_REF.put(2000 + i, BYTE_ARRAY(i));
            MAP_BY_TYPE_REF.put(2500 + i, SHORT_ARRAY(i));
            MAP_BY_TYPE_REF.put(3000 + i, INT_ARRAY(i));
            MAP_BY_TYPE_REF.put(3500 + i, LONG_ARRAY(i));
            MAP_BY_TYPE_REF.put(4000 + i, FLOAT_ARRAY(i));
            MAP_BY_TYPE_REF.put(4500 + i, DOUBLE_ARRAY(i));
        }
        MAP_BY_TYPE_REF.put(COLLECTION.typeId, COLLECTION);
        MAP_BY_TYPE_REF.put(STRING.typeId, STRING);

        MAP_BY_CLASS.put(Object.class, OBJECT);
        MAP_BY_CLASS.put(boolean.class, BOOLEAN);
        MAP_BY_CLASS.put(char.class, CHAR);
        MAP_BY_CLASS.put(byte.class, BYTE);
        MAP_BY_CLASS.put(short.class, SHORT);
        MAP_BY_CLASS.put(int.class, INT);
        MAP_BY_CLASS.put(long.class, LONG);
        MAP_BY_CLASS.put(float.class, FLOAT);
        MAP_BY_CLASS.put(double.class, DOUBLE);
        MAP_BY_CLASS.put(Boolean.class, BOOLEAN_CLASS);
        MAP_BY_CLASS.put(Character.class, CHAR_CLASS);
        MAP_BY_CLASS.put(Byte.class, BYTE_CLASS);
        MAP_BY_CLASS.put(Short.class, SHORT_CLASS);
        MAP_BY_CLASS.put(Integer.class, INT_CLASS);
        MAP_BY_CLASS.put(Long.class, LONG_CLASS);
        MAP_BY_CLASS.put(Float.class, FLOAT_CLASS);
        MAP_BY_CLASS.put(Double.class, DOUBLE_CLASS);
        MAP_BY_CLASS.put(Collection.class, COLLECTION);
        MAP_BY_CLASS.put(String.class, STRING);
    }

    static Type resolveType(Field f) {
        return resolveType(f.getType());
    }

    static Type resolveType(Class<?> clazz) {
        // Search if class is mapped..
        // if not search for his superclass
        if (clazz.isArray()) {
            return resolveArrayType(clazz);
        }
        //
        if (MAP_BY_CLASS.containsKey(clazz)) {
            return MAP_BY_CLASS.get(clazz);
        }
        // Not in map try to find from hierarchie
        List<Class<?>> classList = getClassHierarchie(clazz);
        for (Class<?> c : classList) {
            if (MAP_BY_CLASS.containsKey(c)) {
                MAP_BY_CLASS.put(clazz, MAP_BY_CLASS.get(c));
                return MAP_BY_CLASS.get(c);
            }
        }
        return OBJECT;
    }

    private static Type resolveArrayType(Class<?> arrayClass) {
        // TODO REFACTOR THIS...
        // FIXME not + 1 but + array dimension
        if (arrayClass.getComponentType().equals(boolean.class)) {
            return MAP_BY_TYPE_REF.get(1000 + 1);
        }
        if (arrayClass.getComponentType().equals(char.class)) {
            return MAP_BY_TYPE_REF.get(1500 + 1);
        }
        if (arrayClass.getComponentType().equals(byte.class)) {
            return MAP_BY_TYPE_REF.get(2000 + 1);
        }
        if (arrayClass.getComponentType().equals(short.class)) {
            return MAP_BY_TYPE_REF.get(2500 + 1);
        }
        if (arrayClass.getComponentType().equals(int.class)) {
            return MAP_BY_TYPE_REF.get(3000 + 1);
        }
        if (arrayClass.getComponentType().equals(long.class)) {
            return MAP_BY_TYPE_REF.get(3500 + 1);
        }
        if (arrayClass.getComponentType().equals(float.class)) {
            return MAP_BY_TYPE_REF.get(4000 + 1);
        }
        if (arrayClass.getComponentType().equals(double.class)) {
            return MAP_BY_TYPE_REF.get(4500 + 1);
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


    final int typeId;
    final int arrayProof;
    final int typeSize;
    final boolean isArray;
    final boolean isPrimitive;
    final TypeSerializer typeSerializer;
    final Class<?> clazz;

    // Construct primitive (array or not) typeId
    protected Type(int typeId, int arrayProof, int typeSize, boolean isArray, TypeSerializer typeSerializer, Class<?> clazz) {
        this.typeId = typeId;
        this.arrayProof = arrayProof;
        this.typeSize = typeSize;
        this.isArray = isArray;
        this.isPrimitive = true;
        this.typeSerializer = typeSerializer;
        this.clazz = clazz;
    }

    // Construct object typeId
    protected Type(int typeId, TypeSerializer typeSerializer, Class<?> clazz) {
        this.typeId = typeId;
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

    static List getClassHierarchie(Class classObject) {
        List<Class<?>> generalizations = new ArrayList<>();

        generalizations.add(classObject);

        Class superClass = classObject.getSuperclass();
        if (superClass != null) {
            generalizations.addAll(getClassHierarchie(superClass));
        }

        Class[] superInterfaces = classObject.getInterfaces();
        for (int i = 0; i < superInterfaces.length; i++) {
            Class superInterface = superInterfaces[i];
            generalizations.addAll(getClassHierarchie(superInterface));
        }

        return generalizations;
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

class PrimitiveWrapperType extends Type<Object> {

    PrimitiveWrapperType(int type, int typeSize, TypeSerializer typeSerializer, Class<?> clazz) {
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

