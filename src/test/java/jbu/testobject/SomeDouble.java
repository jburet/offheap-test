package jbu.testobject;

public class SomeDouble {
    private double b1;
    private double b3;
    private double b2;

    public SomeDouble(double b) {
        this.b1 = b;
        this.b2 = b;
        this.b3 = b;
    }

    public SomeDouble() {
        this(1);
    }

    @Override
    public String toString() {
        return "SomeDouble{" +
                "b1=" + b1 +
                ", b3=" + b3 +
                ", b2=" + b2 +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SomeDouble that = (SomeDouble) o;

        if (Double.compare(that.b1, b1) != 0) return false;
        if (Double.compare(that.b2, b2) != 0) return false;
        if (Double.compare(that.b3, b3) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = b1 != +0.0d ? Double.doubleToLongBits(b1) : 0L;
        result = (int) (temp ^ (temp >>> 32));
        temp = b3 != +0.0d ? Double.doubleToLongBits(b3) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = b2 != +0.0d ? Double.doubleToLongBits(b2) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
