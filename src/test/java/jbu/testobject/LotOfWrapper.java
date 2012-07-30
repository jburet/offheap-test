package jbu.testobject;

public class LotOfWrapper {
    public Boolean b1 = true;
    public Character b2 = 'a';
    public Byte b3 = 1;
    public Short b4 = 2;
    public Integer b5 = 3;
    public Long b6 = 4l;
    public Float b7 = 5f;
    public Double b8 = 6d;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LotOfWrapper that = (LotOfWrapper) o;

        if (b1 != null ? !b1.equals(that.b1) : that.b1 != null) return false;
        if (b2 != null ? !b2.equals(that.b2) : that.b2 != null) return false;
        if (b3 != null ? !b3.equals(that.b3) : that.b3 != null) return false;
        if (b4 != null ? !b4.equals(that.b4) : that.b4 != null) return false;
        if (b5 != null ? !b5.equals(that.b5) : that.b5 != null) return false;
        if (b6 != null ? !b6.equals(that.b6) : that.b6 != null) return false;
        if (b7 != null ? !b7.equals(that.b7) : that.b7 != null) return false;
        if (b8 != null ? !b8.equals(that.b8) : that.b8 != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = b1 != null ? b1.hashCode() : 0;
        result = 31 * result + (b2 != null ? b2.hashCode() : 0);
        result = 31 * result + (b3 != null ? b3.hashCode() : 0);
        result = 31 * result + (b4 != null ? b4.hashCode() : 0);
        result = 31 * result + (b5 != null ? b5.hashCode() : 0);
        result = 31 * result + (b6 != null ? b6.hashCode() : 0);
        result = 31 * result + (b7 != null ? b7.hashCode() : 0);
        result = 31 * result + (b8 != null ? b8.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LotOfWrapper{" +
                "b1=" + b1 +
                ", b2=" + b2 +
                ", b3=" + b3 +
                ", b4=" + b4 +
                ", b5=" + b5 +
                ", b6=" + b6 +
                ", b7=" + b7 +
                ", b8=" + b8 +
                '}';
    }
}
