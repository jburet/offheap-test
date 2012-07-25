package jbu.serializer.unsafe;

import java.lang.reflect.Field;

public class Type {

    public static final Type BOOLEAN = new Type((short) 1, (short) 0, (byte) 1, false);

    public static Type BOOLEAN_ARRAY(short arrayProof) {
        return new Type((short) 1, arrayProof, (byte) 1, true);
    }

    public static final Type CHAR = new Type((short) 2, (short) 0, (byte) 2, false);

    public static Type CHAR_ARRAY(short arrayProof) {
        return new Type((short) 2, arrayProof, (byte) 2, true);
    }

    public static final Type BYTE = new Type((short) 3, (short) 0, (byte) 1, false);

    public static Type BYTE_ARRAY(short arrayProof) {
        return new Type((short) 3, arrayProof, (byte) 1, true);
    }

    public static final Type SHORT = new Type((short) 4, (short) 0, (byte) 2, false);

    public static Type SHORT_ARRAY(short arrayProof) {
        return new Type((short) 4, arrayProof, (byte) 2, true);
    }

    public static final Type INT = new Type((short) 5, (short) 0, (byte) 4, false);

    public static Type INT_ARRAY(short arrayProof) {
        return new Type((short) 5, arrayProof, (byte) 4, true);
    }

    public static final Type LONG = new Type((short) 6, (short) 0, (byte) 8, false);

    public static Type LONG_ARRAY(short arrayProof) {
        return new Type((short) 6, arrayProof, (byte) 8, true);
    }

    public static final Type FLOAT = new Type((short) 7, (short) 0, (byte) 4, false);

    public static Type FLOAT_ARRAY(short arrayProof) {
        return new Type((short) 7, arrayProof, (byte) 4, true);
    }

    public static final Type DOUBLE = new Type((short) 8, (short) 0, (byte) 8, false);

    public static Type DOUBLE_ARRAY(short arrayProof) {
        return new Type((short) 8, arrayProof, (byte) 8, true);
    }

    public final short type;
    public final short arrayProof;
    public final byte typeSize;
    public final boolean isArray;


    private Type(short type, short arrayProof, byte typeSize, boolean isArray) {
        this.type = type;
        this.arrayProof = arrayProof;
        this.typeSize = typeSize;
        this.isArray = isArray;
    }

    public static Type resolveType(Field f) {
        if (f.getType().isPrimitive()) {
            String tname = f.getType().getName();
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
        } else if (f.getType().isArray()) {
            String tname = f.getType().getName();
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
        }
        return null;
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
