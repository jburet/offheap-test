package jbu.serializer;

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
}
