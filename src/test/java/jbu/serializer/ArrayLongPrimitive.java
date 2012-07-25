package jbu.serializer;

public class ArrayLongPrimitive {

    public ArrayLongPrimitive(long[] a) {
        this.a = a;
    }

    public ArrayLongPrimitive() {
        this(new long[]{});
    }

    long[] a = new long[]{9, 9};
}
