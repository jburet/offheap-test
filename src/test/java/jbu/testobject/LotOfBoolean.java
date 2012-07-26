package jbu.testobject;

import java.io.Serializable;

public class LotOfBoolean implements Serializable {
    private boolean b1;
    private boolean b3;
    private boolean b2;


    public LotOfBoolean() {
        this(true);
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LotOfBoolean that = (LotOfBoolean) o;

        if (b1 != that.b1) return false;
        if (b2 != that.b2) return false;
        if (b3 != that.b3) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (b1 ? 1 : 0);
        result = 31 * result + (b3 ? 1 : 0);
        result = 31 * result + (b2 ? 1 : 0);
        return result;
    }
}
