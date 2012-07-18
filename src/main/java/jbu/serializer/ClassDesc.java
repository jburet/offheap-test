package jbu.serializer;

import java.lang.reflect.Field;

public class ClassDesc {

    public final static int BOOLEAN = 1;
    public final static int CHAR = 2;
    public final static int BYTE = 3;
    public final static int SHORT = 4;
    public final static int INT = 5;
    public final static int LONG = 6;
    public final static int FLOAT = 7;
    public final static int DOUBLE = 8;

    public final int[] types;
    public final long[] offsets;
    public final int nbFields;

    public ClassDesc(int[] types, long[] offsets) {
        // Verify same length
        if (types.length != offsets.length) {
            // FIXME caracterize exception
            throw new RuntimeException("Error in offsets or types. Length are different");
        }
        this.nbFields = types.length;
        this.types = types;
        this.offsets = offsets;
    }

    public static int resolveType(Field f) {
        if (f.getType().equals(boolean.class)) {
            return BOOLEAN;
        } else if (f.getType().equals(char.class)) {
            return CHAR;
        } else if (f.getType().equals(byte.class)) {
            return BYTE;
        } else if (f.getType().equals(short.class)) {
            return SHORT;
        } else if (f.getType().equals(int.class)) {
            return INT;
        } else if (f.getType().equals(long.class)) {
            return LONG;
        } else if (f.getType().equals(float.class)) {
            return FLOAT;
        } else if (f.getType().equals(double.class)) {
            return DOUBLE;
        }
        return 0;
    }
}
