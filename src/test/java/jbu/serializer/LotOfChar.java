package jbu.serializer;

public class LotOfChar {
    private char b1;
    private char b3;
    private char b2;

    public LotOfChar(char b) {
        this.b1 = b;
        this.b2 = b;
        this.b3 = b;
    }

    @Override
    public String toString() {
        return "LotOfChar{" +
                "b1=" + b1 +
                ", b3=" + b3 +
                ", b2=" + b2 +
                '}';
    }
}
