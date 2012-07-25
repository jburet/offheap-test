package jbu.testobject;

public class ArrayBooleanPrimitive {

    public ArrayBooleanPrimitive(boolean[] a) {
        this.a = a;
    }

    public ArrayBooleanPrimitive() {
        this(new boolean[]{});
    }

    public boolean[] a = new boolean[]{false, false};
}
