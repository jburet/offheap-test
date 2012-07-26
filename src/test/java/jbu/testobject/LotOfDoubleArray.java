package jbu.testobject;

import java.io.Serializable;
import java.util.Arrays;

public class LotOfDoubleArray implements Serializable {
    private double[] d1 = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private double[] d2 = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private double[] d3 = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private double[] d4 = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

    @Override
    public String toString() {
        return "LotOfDoubleArray{" +
                "d1=" + Arrays.toString(d1) +
                ", d2=" + Arrays.toString(d2) +
                ", d3=" + Arrays.toString(d3) +
                ", d4=" + Arrays.toString(d4) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LotOfDoubleArray that = (LotOfDoubleArray) o;

        if (!Arrays.equals(d1, that.d1)) return false;
        if (!Arrays.equals(d2, that.d2)) return false;
        if (!Arrays.equals(d3, that.d3)) return false;
        if (!Arrays.equals(d4, that.d4)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = d1 != null ? Arrays.hashCode(d1) : 0;
        result = 31 * result + (d2 != null ? Arrays.hashCode(d2) : 0);
        result = 31 * result + (d3 != null ? Arrays.hashCode(d3) : 0);
        result = 31 * result + (d4 != null ? Arrays.hashCode(d4) : 0);
        return result;
    }
}
