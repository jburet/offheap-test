package jbu.testobject;

public class LotOfInt {
    private int a = 42;
    private int b = 42;
    private int c = 42;
    private int d = 42;
    private int e = 42;
    private int f = 42;
    private int g = 42;
    private int h = 42;
    private int i = 42;

    public LotOfInt() {
        this(1);
    }

    public LotOfInt(int i) {
        this.a = i;
        this.b = i;
        this.c = i;
        this.d = i;
        this.e = i;
        this.f = i;
        this.g = i;
        this.h = i;
        this.i = i;
    }

    @Override
    public String toString() {
        return "LotOfInt{" +
                "a=" + a +
                ", b=" + b +
                ", c=" + c +
                ", d=" + d +
                ", e=" + e +
                ", f=" + f +
                ", g=" + g +
                ", h=" + h +
                ", i=" + i +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LotOfInt lotOfInt = (LotOfInt) o;

        if (a != lotOfInt.a) return false;
        if (b != lotOfInt.b) return false;
        if (c != lotOfInt.c) return false;
        if (d != lotOfInt.d) return false;
        if (e != lotOfInt.e) return false;
        if (f != lotOfInt.f) return false;
        if (g != lotOfInt.g) return false;
        if (h != lotOfInt.h) return false;
        if (i != lotOfInt.i) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = a;
        result = 31 * result + b;
        result = 31 * result + c;
        result = 31 * result + d;
        result = 31 * result + e;
        result = 31 * result + f;
        result = 31 * result + g;
        result = 31 * result + h;
        result = 31 * result + i;
        return result;
    }
}
