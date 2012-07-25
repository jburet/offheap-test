package jbu.testobject;

public class ArrayIntPrimitive {

    public ArrayIntPrimitive(int[] a) {
        this.a = a;
    }

    public ArrayIntPrimitive() {
        this(new int[]{});
    }

    public int[] a = new int[]{2, 4};
}
