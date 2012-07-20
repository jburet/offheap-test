package jbu.serializer;

public class SomeDouble {
    private double b1;
    private double b3;
    private double b2;

    public SomeDouble(double b) {
        this.b1 = b;
        this.b2 = b;
        this.b3 = b;
    }

    @Override
    public String toString() {
        return "SomeDouble{" +
                "b1=" + b1 +
                ", b3=" + b3 +
                ", b2=" + b2 +
                '}';
    }
}
