package jbu.testobject;

import java.io.Serializable;
import java.util.Arrays;

// SIZE = 8 + 16 +8 +16+32+64+32+64
public class LotOfPrimitiveAndArrayAndString implements Serializable {
    private boolean b1 = true;
    private boolean b2 = true;
    private boolean b3 = true;
    private boolean b4 = true;
    private boolean b5 = true;
    private boolean b6 = true;
    private boolean b7 = true;
    private boolean b8 = true;

    private char c1 = 'a';
    private char c2 = 'a';
    private char c3 = 'a';
    private char c4 = 'a';
    private char c5 = 'a';
    private char c6 = 'a';
    private char c7 = 'a';
    private char c8 = 'a';

    private byte by1 = 1;
    private byte by2 = 1;
    private byte by3 = 1;
    private byte by4 = 1;
    private byte by5 = 1;
    private byte by6 = 1;
    private byte by7 = 1;
    private byte by8 = 1;

    private short s1 = 42;
    private short s2 = 42;
    private short s3 = 42;
    private short s4 = 42;
    private short s5 = 42;
    private short s6 = 42;
    private short s7 = 42;
    private short s8 = 42;

    private int i1 = 42;
    private int i2 = 42;
    private int i3 = 42;
    private int i4 = 42;
    private int i5 = 42;
    private int i6 = 42;
    private int i7 = 42;
    private int i8 = 42;

    private long l1 = 42;
    private long l2 = 42;
    private long l3 = 42;
    private long l4 = 42;
    private long l5 = 42;
    private long l6 = 42;
    private long l7 = 42;
    private long l8 = 42;

    private float f1 = 42.0f;
    private float f2 = 42.0f;
    private float f3 = 42.0f;
    private float f4 = 42.0f;
    private float f5 = 42.0f;
    private float f6 = 42.0f;
    private float f7 = 42.0f;
    private float f8 = 42.0f;

    private double d1 = 42.0;
    private double d2 = 42.0;
    private double d3 = 42.0;
    private double d4 = 42.0;
    private double d5 = 42.0;
    private double d6 = 42.0;
    private double d7 = 42.0;
    private double d8 = 42.0;

    private double[] da1 = new double[]{42.0, 43.0, 44.0, 45.0, 46.0, 47.0, 48.0, 49.0};
    private double[] da2 = new double[]{42.0, 43.0, 44.0, 45.0, 46.0, 47.0, 48.0, 49.0};
    private double[] da3 = new double[]{42.0, 43.0, 44.0, 45.0, 46.0, 47.0, 48.0, 49.0};
    private double[] da4 = new double[]{42.0, 43.0, 44.0, 45.0, 46.0, 47.0, 48.0, 49.0};
    private double[] da5 = new double[]{42.0, 43.0, 44.0, 45.0, 46.0, 47.0, 48.0, 49.0};
    private double[] da6 = new double[]{42.0, 43.0, 44.0, 45.0, 46.0, 47.0, 48.0, 49.0};
    private double[] da7 = new double[]{42.0, 43.0, 44.0, 45.0, 46.0, 47.0, 48.0, 49.0};
    private double[] da8 = new double[]{42.0, 43.0, 44.0, 45.0, 46.0, 47.0, 48.0, 49.0};

    private String a = "aazemlkfsdfùklgjsmkgdsfgertrieogjfdmknsdf;g,fsmdglkjerfesmfdkgndfsg;n:,dfgnds;:fvcxv dfkglmjerfuizoehfsdlfhskdljhfskdlfh";
    private String b = "aazemlkfsdfùklgjsmkgdsfgertrieogjfdmknsdf;g,fsmdglkjerfesmfdkgndfsg;n:,dfgnds;:fvcxv dfkglmjerfuizoehfsdlfhskdljhfskdlfh";
    private String c = "aazemlkfsdfùklgjsmkgdsfgertrieogjfdmknsdf;g,fsmdglkjerfesmfdkgndfsg;n:,dfgnds;:fvcxv dfkglmjerfuizoehfsdlfhskdljhfskdlfh";
    private String d = "aazemlkfsdfùklgjsmkgdsfgertrieogjfdmknsdf;g,fsmdglkjerfesmfdkgndfsg;n:,dfgnds;:fvcxv dfkglmjerfuizoehfsdlfhskdljhfskdlfh";
    private String e = "aazemlkfsdfùklgjsmkgdsfgertrieogjfdmknsdf;g,fsmdglkjerfesmfdkgndfsg;n:,dfgnds;:fvcxv dfkglmjerfuizoehfsdlfhskdljhfskdlfh";
    private String f = "aazemlkfsdfùklgjsmkgdsfgertrieogjfdmknsdf;g,fsmdglkjerfesmfdkgndfsg;n:,dfgnds;:fvcxv dfkglmjerfuizoehfsdlfhskdljhfskdlfh";
    private String h = "aazemlkfsdfùklgjsmkgdsfgertrieogjfdmknsdf;g,fsmdglkjerfesmfdkgndfsg;n:,dfgnds;:fvcxv dfkglmjerfuizoehfsdlfhskdljhfskdlfh";
    private String i = "aazemlkfsdfùklgjsmkgdsfgertrieogjfdmknsdf;g,fsmdglkjerfesmfdkgndfsg;n:,dfgnds;:fvcxv dfkglmjerfuizoehfsdlfhskdljhfskdlfh";
    private String j = "aazemlkfsdfùklgjsmkgdsfgertrieogjfdmknsdf;g,fsmdglkjerfesmfdkgndfsg;n:,dfgnds;:fvcxv dfkglmjerfuizoehfsdlfhskdljhfskdlfh";
    private String k = "aazemlkfsdfùklgjsmkgdsfgertrieogjfdmknsdf;g,fsmdglkjerfesmfdkgndfsg;n:,dfgnds;:fvcxv dfkglmjerfuizoehfsdlfhskdljhfskdlfh";
    private String l = "aazemlkfsdfùklgjsmkgdsfgertrieogjfdmknsdf;g,fsmdglkjerfesmfdkgndfsg;n:,dfgnds;:fvcxv dfkglmjerfuizoehfsdlfhskdljhfskdlfh";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LotOfPrimitiveAndArrayAndString that = (LotOfPrimitiveAndArrayAndString) o;

        if (b1 != that.b1) return false;
        if (b2 != that.b2) return false;
        if (b3 != that.b3) return false;
        if (b4 != that.b4) return false;
        if (b5 != that.b5) return false;
        if (b6 != that.b6) return false;
        if (b7 != that.b7) return false;
        if (b8 != that.b8) return false;
        if (by1 != that.by1) return false;
        if (by2 != that.by2) return false;
        if (by3 != that.by3) return false;
        if (by4 != that.by4) return false;
        if (by5 != that.by5) return false;
        if (by6 != that.by6) return false;
        if (by7 != that.by7) return false;
        if (by8 != that.by8) return false;
        if (c1 != that.c1) return false;
        if (c2 != that.c2) return false;
        if (c3 != that.c3) return false;
        if (c4 != that.c4) return false;
        if (c5 != that.c5) return false;
        if (c6 != that.c6) return false;
        if (c7 != that.c7) return false;
        if (c8 != that.c8) return false;
        if (Double.compare(that.d1, d1) != 0) return false;
        if (Double.compare(that.d2, d2) != 0) return false;
        if (Double.compare(that.d3, d3) != 0) return false;
        if (Double.compare(that.d4, d4) != 0) return false;
        if (Double.compare(that.d5, d5) != 0) return false;
        if (Double.compare(that.d6, d6) != 0) return false;
        if (Double.compare(that.d7, d7) != 0) return false;
        if (Double.compare(that.d8, d8) != 0) return false;
        if (Float.compare(that.f1, f1) != 0) return false;
        if (Float.compare(that.f2, f2) != 0) return false;
        if (Float.compare(that.f3, f3) != 0) return false;
        if (Float.compare(that.f4, f4) != 0) return false;
        if (Float.compare(that.f5, f5) != 0) return false;
        if (Float.compare(that.f6, f6) != 0) return false;
        if (Float.compare(that.f7, f7) != 0) return false;
        if (Float.compare(that.f8, f8) != 0) return false;
        if (i1 != that.i1) return false;
        if (i2 != that.i2) return false;
        if (i3 != that.i3) return false;
        if (i4 != that.i4) return false;
        if (i5 != that.i5) return false;
        if (i6 != that.i6) return false;
        if (i7 != that.i7) return false;
        if (i8 != that.i8) return false;
        if (l1 != that.l1) return false;
        if (l2 != that.l2) return false;
        if (l3 != that.l3) return false;
        if (l4 != that.l4) return false;
        if (l5 != that.l5) return false;
        if (l6 != that.l6) return false;
        if (l7 != that.l7) return false;
        if (l8 != that.l8) return false;
        if (s1 != that.s1) return false;
        if (s2 != that.s2) return false;
        if (s3 != that.s3) return false;
        if (s4 != that.s4) return false;
        if (s5 != that.s5) return false;
        if (s6 != that.s6) return false;
        if (s7 != that.s7) return false;
        if (s8 != that.s8) return false;
        if (a != null ? !a.equals(that.a) : that.a != null) return false;
        if (b != null ? !b.equals(that.b) : that.b != null) return false;
        if (c != null ? !c.equals(that.c) : that.c != null) return false;
        if (d != null ? !d.equals(that.d) : that.d != null) return false;
        if (!Arrays.equals(da1, that.da1)) return false;
        if (!Arrays.equals(da2, that.da2)) return false;
        if (!Arrays.equals(da3, that.da3)) return false;
        if (!Arrays.equals(da4, that.da4)) return false;
        if (!Arrays.equals(da5, that.da5)) return false;
        if (!Arrays.equals(da6, that.da6)) return false;
        if (!Arrays.equals(da7, that.da7)) return false;
        if (!Arrays.equals(da8, that.da8)) return false;
        if (e != null ? !e.equals(that.e) : that.e != null) return false;
        if (f != null ? !f.equals(that.f) : that.f != null) return false;
        if (h != null ? !h.equals(that.h) : that.h != null) return false;
        if (i != null ? !i.equals(that.i) : that.i != null) return false;
        if (j != null ? !j.equals(that.j) : that.j != null) return false;
        if (k != null ? !k.equals(that.k) : that.k != null) return false;
        if (l != null ? !l.equals(that.l) : that.l != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (b1 ? 1 : 0);
        result = 31 * result + (b2 ? 1 : 0);
        result = 31 * result + (b3 ? 1 : 0);
        result = 31 * result + (b4 ? 1 : 0);
        result = 31 * result + (b5 ? 1 : 0);
        result = 31 * result + (b6 ? 1 : 0);
        result = 31 * result + (b7 ? 1 : 0);
        result = 31 * result + (b8 ? 1 : 0);
        result = 31 * result + (int) c1;
        result = 31 * result + (int) c2;
        result = 31 * result + (int) c3;
        result = 31 * result + (int) c4;
        result = 31 * result + (int) c5;
        result = 31 * result + (int) c6;
        result = 31 * result + (int) c7;
        result = 31 * result + (int) c8;
        result = 31 * result + (int) by1;
        result = 31 * result + (int) by2;
        result = 31 * result + (int) by3;
        result = 31 * result + (int) by4;
        result = 31 * result + (int) by5;
        result = 31 * result + (int) by6;
        result = 31 * result + (int) by7;
        result = 31 * result + (int) by8;
        result = 31 * result + (int) s1;
        result = 31 * result + (int) s2;
        result = 31 * result + (int) s3;
        result = 31 * result + (int) s4;
        result = 31 * result + (int) s5;
        result = 31 * result + (int) s6;
        result = 31 * result + (int) s7;
        result = 31 * result + (int) s8;
        result = 31 * result + i1;
        result = 31 * result + i2;
        result = 31 * result + i3;
        result = 31 * result + i4;
        result = 31 * result + i5;
        result = 31 * result + i6;
        result = 31 * result + i7;
        result = 31 * result + i8;
        result = 31 * result + (int) (l1 ^ (l1 >>> 32));
        result = 31 * result + (int) (l2 ^ (l2 >>> 32));
        result = 31 * result + (int) (l3 ^ (l3 >>> 32));
        result = 31 * result + (int) (l4 ^ (l4 >>> 32));
        result = 31 * result + (int) (l5 ^ (l5 >>> 32));
        result = 31 * result + (int) (l6 ^ (l6 >>> 32));
        result = 31 * result + (int) (l7 ^ (l7 >>> 32));
        result = 31 * result + (int) (l8 ^ (l8 >>> 32));
        result = 31 * result + (f1 != +0.0f ? Float.floatToIntBits(f1) : 0);
        result = 31 * result + (f2 != +0.0f ? Float.floatToIntBits(f2) : 0);
        result = 31 * result + (f3 != +0.0f ? Float.floatToIntBits(f3) : 0);
        result = 31 * result + (f4 != +0.0f ? Float.floatToIntBits(f4) : 0);
        result = 31 * result + (f5 != +0.0f ? Float.floatToIntBits(f5) : 0);
        result = 31 * result + (f6 != +0.0f ? Float.floatToIntBits(f6) : 0);
        result = 31 * result + (f7 != +0.0f ? Float.floatToIntBits(f7) : 0);
        result = 31 * result + (f8 != +0.0f ? Float.floatToIntBits(f8) : 0);
        temp = d1 != +0.0d ? Double.doubleToLongBits(d1) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = d2 != +0.0d ? Double.doubleToLongBits(d2) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = d3 != +0.0d ? Double.doubleToLongBits(d3) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = d4 != +0.0d ? Double.doubleToLongBits(d4) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = d5 != +0.0d ? Double.doubleToLongBits(d5) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = d6 != +0.0d ? Double.doubleToLongBits(d6) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = d7 != +0.0d ? Double.doubleToLongBits(d7) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = d8 != +0.0d ? Double.doubleToLongBits(d8) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (da1 != null ? Arrays.hashCode(da1) : 0);
        result = 31 * result + (da2 != null ? Arrays.hashCode(da2) : 0);
        result = 31 * result + (da3 != null ? Arrays.hashCode(da3) : 0);
        result = 31 * result + (da4 != null ? Arrays.hashCode(da4) : 0);
        result = 31 * result + (da5 != null ? Arrays.hashCode(da5) : 0);
        result = 31 * result + (da6 != null ? Arrays.hashCode(da6) : 0);
        result = 31 * result + (da7 != null ? Arrays.hashCode(da7) : 0);
        result = 31 * result + (da8 != null ? Arrays.hashCode(da8) : 0);
        result = 31 * result + (a != null ? a.hashCode() : 0);
        result = 31 * result + (b != null ? b.hashCode() : 0);
        result = 31 * result + (c != null ? c.hashCode() : 0);
        result = 31 * result + (d != null ? d.hashCode() : 0);
        result = 31 * result + (e != null ? e.hashCode() : 0);
        result = 31 * result + (f != null ? f.hashCode() : 0);
        result = 31 * result + (h != null ? h.hashCode() : 0);
        result = 31 * result + (i != null ? i.hashCode() : 0);
        result = 31 * result + (j != null ? j.hashCode() : 0);
        result = 31 * result + (k != null ? k.hashCode() : 0);
        result = 31 * result + (l != null ? l.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LotOfPrimitiveAndArrayAndString{" +
                "b1=" + b1 +
                ", b2=" + b2 +
                ", b3=" + b3 +
                ", b4=" + b4 +
                ", b5=" + b5 +
                ", b6=" + b6 +
                ", b7=" + b7 +
                ", b8=" + b8 +
                ", c1=" + c1 +
                ", c2=" + c2 +
                ", c3=" + c3 +
                ", c4=" + c4 +
                ", c5=" + c5 +
                ", c6=" + c6 +
                ", c7=" + c7 +
                ", c8=" + c8 +
                ", by1=" + by1 +
                ", by2=" + by2 +
                ", by3=" + by3 +
                ", by4=" + by4 +
                ", by5=" + by5 +
                ", by6=" + by6 +
                ", by7=" + by7 +
                ", by8=" + by8 +
                ", s1=" + s1 +
                ", s2=" + s2 +
                ", s3=" + s3 +
                ", s4=" + s4 +
                ", s5=" + s5 +
                ", s6=" + s6 +
                ", s7=" + s7 +
                ", s8=" + s8 +
                ", i1=" + i1 +
                ", i2=" + i2 +
                ", i3=" + i3 +
                ", i4=" + i4 +
                ", i5=" + i5 +
                ", i6=" + i6 +
                ", i7=" + i7 +
                ", i8=" + i8 +
                ", l1=" + l1 +
                ", l2=" + l2 +
                ", l3=" + l3 +
                ", l4=" + l4 +
                ", l5=" + l5 +
                ", l6=" + l6 +
                ", l7=" + l7 +
                ", l8=" + l8 +
                ", f1=" + f1 +
                ", f2=" + f2 +
                ", f3=" + f3 +
                ", f4=" + f4 +
                ", f5=" + f5 +
                ", f6=" + f6 +
                ", f7=" + f7 +
                ", f8=" + f8 +
                ", d1=" + d1 +
                ", d2=" + d2 +
                ", d3=" + d3 +
                ", d4=" + d4 +
                ", d5=" + d5 +
                ", d6=" + d6 +
                ", d7=" + d7 +
                ", d8=" + d8 +
                ", da1=" + da1 +
                ", da2=" + da2 +
                ", da3=" + da3 +
                ", da4=" + da4 +
                ", da5=" + da5 +
                ", da6=" + da6 +
                ", da7=" + da7 +
                ", da8=" + da8 +
                ", a='" + a + '\'' +
                ", b='" + b + '\'' +
                ", c='" + c + '\'' +
                ", d='" + d + '\'' +
                ", e='" + e + '\'' +
                ", f='" + f + '\'' +
                ", h='" + h + '\'' +
                ", i='" + i + '\'' +
                ", j='" + j + '\'' +
                ", k='" + k + '\'' +
                ", l='" + l + '\'' +
                '}';
    }
}
