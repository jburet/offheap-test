package jbu.serializer;

public class ArrayIntPrimitive {

    public ArrayIntPrimitive(int[] a) {
        this.a = a;
    }

    public ArrayIntPrimitive() {
        this(new int[]{});
    }

    int[] a = new int[]{2, 4};
}
