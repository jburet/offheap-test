package jbu.serializer.unsafe;

import jbu.serializer.unsafe.type.*;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Type {

    private final static Map<Integer, Type> mapByTypeRef = new HashMap<Integer, Type>();

    // Primitive Type
    private final static PrimitiveSerializer primitiveSerializer = new PrimitiveSerializer();
    private final static PrimitiveArraySerializer primitiveArraySerializer = new PrimitiveArraySerializer();
    public static final Type BOOLEAN = new Type(1, (short) 0, (byte) 1, false, primitiveSerializer, boolean.class);
    public static final Type CHAR = new Type(2, (short) 0, (byte) 2, false, primitiveSerializer, char.class);
    public static final Type BYTE = new Type(3, (short) 0, (byte) 1, false, primitiveSerializer, byte.class);
    public static final Type SHORT = new Type(4, (short) 0, (byte) 2, false, primitiveSerializer, short.class);
    public static final Type INT = new Type(5, (short) 0, (byte) 4, false, primitiveSerializer, int.class);
    public static final Type LONG = new Type(6, (short) 0, (byte) 8, false, primitiveSerializer, long.class);
    public static final Type FLOAT = new Type(7, (short) 0, (byte) 4, false, primitiveSerializer, float.class);
    public static final Type DOUBLE = new Type(8, (short) 0, (byte) 8, false, primitiveSerializer, double.class);


    // Array type
    public static Type BOOLEAN_ARRAY(int arrayProof) {
        return new Type(11, arrayProof, (byte) 1, true, primitiveArraySerializer, boolean.class);
    }

    public static Type CHAR_ARRAY(int arrayProof) {
        return new Type(12, arrayProof, (byte) 2, true, primitiveArraySerializer, char.class);
    }

    public static Type BYTE_ARRAY(int arrayProof) {
        return new Type(13, arrayProof, (byte) 1, true, primitiveArraySerializer, byte.class);
    }

    public static Type SHORT_ARRAY(int arrayProof) {
        return new Type(14, arrayProof, (byte) 2, true, primitiveArraySerializer, short.class);
    }

    public static Type INT_ARRAY(int arrayProof) {
        return new Type(15, arrayProof, (byte) 4, true, primitiveArraySerializer, int.class);
    }

    public static Type LONG_ARRAY(int arrayProof) {
        return new Type(16, arrayProof, (byte) 8, true, primitiveArraySerializer, long.class);
    }

    public static Type FLOAT_ARRAY(int arrayProof) {
        return new Type(17, arrayProof, (byte) 4, true, primitiveArraySerializer, float.class);
    }

    public static Type DOUBLE_ARRAY(int arrayProof) {
        return new Type(18, arrayProof, (byte) 8, true, primitiveArraySerializer, double.class);
    }

    // Collection
    public static final Type COLLECTION = new Type(5000, new CollectionSerializer(), Collection.class);

    // Standard java type
    public static final Type STRING = new Type(10000, new StringSerializer(), String.class);
    public static final Type OBJECT = new Type(0, new DefaultSerializer(), Object.class);

    static {
        mapByTypeRef.put(0, OBJECT);
        mapByTypeRef.put(1, BOOLEAN);
        mapByTypeRef.put(2, CHAR);
        mapByTypeRef.put(3, BYTE);
        mapByTypeRef.put(4, SHORT);
        mapByTypeRef.put(5, INT);
        mapByTypeRef.put(6, LONG);
        mapByTypeRef.put(7, FLOAT);
        mapByTypeRef.put(8, DOUBLE);
        mapByTypeRef.put(1001, BOOLEAN_ARRAY(1));
        mapByTypeRef.put(1501, CHAR_ARRAY(1));
        mapByTypeRef.put(2001, BYTE_ARRAY(1));
        mapByTypeRef.put(2501, SHORT_ARRAY(1));
        mapByTypeRef.put(3001, INT_ARRAY(1));
        mapByTypeRef.put(3501, LONG_ARRAY(1));
        mapByTypeRef.put(4001, FLOAT_ARRAY(1));
        mapByTypeRef.put(4501, DOUBLE_ARRAY(1));
        mapByTypeRef.put(5000, COLLECTION);
        mapByTypeRef.put(10000, STRING);

    }

    public final int type;
    public final int arrayProof;
    public final byte typeSize;
    public final boolean isArray;
    public final boolean isPrimitive;
    public final TypeSerializer typeSerializer;
    public final Class<?> clazz;


    // Construct primitive (array or not) type
    private Type(int type, int arrayProof, byte typeSize, boolean isArray, TypeSerializer typeSerializer, Class<?> clazz) {
        this.type = type;
        this.arrayProof = arrayProof;
        this.typeSize = typeSize;
        this.isArray = isArray;
        this.isPrimitive = true;
        this.typeSerializer = typeSerializer;
        this.clazz = clazz;
    }

    // Construct object type
    private Type(int type, TypeSerializer typeSerializer, Class<?> clazz) {
        this.type = type;
        this.arrayProof = -1;
        this.typeSize = -1;
        this.isArray = false;
        this.isPrimitive = false;
        this.typeSerializer = typeSerializer;
        this.clazz = clazz;
    }

    public static Type resolveType(Field f) {
        return resolveType(f.getType());
    }

    public static Type resolveType(Class<?> clazz) {
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

    public static Type resolveType(int i) {
        return mapByTypeRef.get(i);
    }

    private static short getProof(String tname) {
        return (short) (tname.length() - 1);
    }

    private static String getTypeCode(String tname) {
        return tname.substring(tname.length() - 1);
    }

    public int getLength(int... arrayLength) {
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


}
