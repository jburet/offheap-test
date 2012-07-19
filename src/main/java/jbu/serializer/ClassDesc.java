package jbu.serializer;

import java.lang.reflect.Field;

public class ClassDesc {

    public final Type[] types;
    public final long[] offsets;
    public final int nbFields;

    public ClassDesc(Type[] types, long[] offsets) {
        // Verify same length
        if (types.length != offsets.length) {
            // FIXME caracterize exception
            throw new RuntimeException("Error in offsets or types. Length are different");
        }
        this.nbFields = types.length;
        this.types = types;
        this.offsets = offsets;
    }
}
