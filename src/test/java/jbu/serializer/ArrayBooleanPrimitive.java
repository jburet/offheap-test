package jbu.serializer;

public class ArrayBooleanPrimitive {

    public ArrayBooleanPrimitive(boolean[] a) {
        this.a = a;
    }

    public ArrayBooleanPrimitive() {
        this(new boolean[]{});
    }

    boolean[] a = new boolean[]{false, false};
}
