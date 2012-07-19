package jbu.serializer;

public class LotOfBoolean {
    private boolean b1;
    private boolean b3;
    private boolean b2;

    public LotOfBoolean(boolean b) {
        this.b1 = b;
        this.b2 = b;
        this.b3 = b;
    }

    @Override
    public String toString() {
        return "LotOfBoolean{" +
                "b1=" + b1 +
                ", b3=" + b3 +
                ", b2=" + b2 +
                '}';
    }
}
