package jbu.testobject;

public class LotOfDouble {
    private double b1;
    private double b3;
    private double b2;
    private double b4=45;
    private double b5=45;
    private double b6=45;
    private double b7=45;
    private double b8=45;
    private double b9=45;
    private double b10=45;
    private double b11=45;
    private double b12=45;
    private double b14=45;
    private double b13=45;
    private double b15=45;
    private double b16=45;
    private double b17=45;
    private double b18=45;

    public LotOfDouble(double b) {
        this.b1 = b;
        this.b2 = b;
        this.b3 = b;
    }

    public LotOfDouble() {
        this(23);
    }

    @Override
    public String toString() {
        return "LotOfDouble{" +
                "b1=" + b1 +
                ", b3=" + b3 +
                ", b2=" + b2 +
                ", b4=" + b4 +
                ", b5=" + b5 +
                ", b6=" + b6 +
                ", b7=" + b7 +
                ", b8=" + b8 +
                ", b9=" + b9 +
                ", b10=" + b10 +
                ", b11=" + b11 +
                ", b12=" + b12 +
                ", b14=" + b14 +
                ", b13=" + b13 +
                ", b15=" + b15 +
                ", b16=" + b16 +
                ", b17=" + b17 +
                ", b18=" + b18 +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LotOfDouble that = (LotOfDouble) o;

        if (Double.compare(that.b1, b1) != 0) return false;
        if (Double.compare(that.b10, b10) != 0) return false;
        if (Double.compare(that.b11, b11) != 0) return false;
        if (Double.compare(that.b12, b12) != 0) return false;
        if (Double.compare(that.b13, b13) != 0) return false;
        if (Double.compare(that.b14, b14) != 0) return false;
        if (Double.compare(that.b15, b15) != 0) return false;
        if (Double.compare(that.b16, b16) != 0) return false;
        if (Double.compare(that.b17, b17) != 0) return false;
        if (Double.compare(that.b18, b18) != 0) return false;
        if (Double.compare(that.b2, b2) != 0) return false;
        if (Double.compare(that.b3, b3) != 0) return false;
        if (Double.compare(that.b4, b4) != 0) return false;
        if (Double.compare(that.b5, b5) != 0) return false;
        if (Double.compare(that.b6, b6) != 0) return false;
        if (Double.compare(that.b7, b7) != 0) return false;
        if (Double.compare(that.b8, b8) != 0) return false;
        if (Double.compare(that.b9, b9) != 0) return false;

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
        temp = b4 != +0.0d ? Double.doubleToLongBits(b4) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = b5 != +0.0d ? Double.doubleToLongBits(b5) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = b6 != +0.0d ? Double.doubleToLongBits(b6) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = b7 != +0.0d ? Double.doubleToLongBits(b7) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = b8 != +0.0d ? Double.doubleToLongBits(b8) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = b9 != +0.0d ? Double.doubleToLongBits(b9) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = b10 != +0.0d ? Double.doubleToLongBits(b10) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = b11 != +0.0d ? Double.doubleToLongBits(b11) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = b12 != +0.0d ? Double.doubleToLongBits(b12) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = b14 != +0.0d ? Double.doubleToLongBits(b14) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = b13 != +0.0d ? Double.doubleToLongBits(b13) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = b15 != +0.0d ? Double.doubleToLongBits(b15) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = b16 != +0.0d ? Double.doubleToLongBits(b16) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = b17 != +0.0d ? Double.doubleToLongBits(b17) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = b18 != +0.0d ? Double.doubleToLongBits(b18) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
