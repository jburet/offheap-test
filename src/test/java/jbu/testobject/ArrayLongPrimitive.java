package jbu.testobject;

public class ArrayLongPrimitive {

    public ArrayLongPrimitive(long[] a) {
        this.a = a;
    }

    public ArrayLongPrimitive() {
        this(new long[]{});
    }

    public long[] a = new long[]{9, 9};
}
