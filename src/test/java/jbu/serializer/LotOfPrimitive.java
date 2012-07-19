package jbu.serializer;

// SIZE = 8 + 16 +8 +16+32+64+32+64
public class LotOfPrimitive {
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

    public LotOfPrimitive(boolean someValueZero) {
        if (someValueZero) {
            b1 = false;
            c1 = 'z';
            by1 = 0;
            s1 = 0;
            i1 = 0;
            l1 = 0;
            f1 = 0f;
            d1 = 0.0;
        }
    }

    public LotOfPrimitive() {

    }

    @Override
    public String toString() {
        return "LotOfPrimitive{" +
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
                '}';
    }
}
