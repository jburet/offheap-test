package jbu.testobject;

public class LotOfChar {
    private char b1;
    private char b3;
    private char b2;

    public LotOfChar(char b) {
        this.b1 = b;
        this.b2 = b;
        this.b3 = b;
    }

    public LotOfChar() {
        this('a');
    }

    @Override
    public String toString() {
        return "LotOfChar{" +
                "b1=" + b1 +
                ", b3=" + b3 +
                ", b2=" + b2 +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LotOfChar lotOfChar = (LotOfChar) o;

        if (b1 != lotOfChar.b1) return false;
        if (b2 != lotOfChar.b2) return false;
        if (b3 != lotOfChar.b3) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) b1;
        result = 31 * result + (int) b3;
        result = 31 * result + (int) b2;
        return result;
    }
}
