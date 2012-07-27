package jbu.testobject;

public class LotOfString {
    public String a = "aazemlkfsdfùklgjsmkgdsfgertrieogjfdmknsdf;g,fsmdglkjerfesmfdkgndfsg;n:,dfgnds;:fvcxv dfkglmjerfuizoehfsdlfhskdljhfskdlfh";
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

        LotOfString that = (LotOfString) o;

        if (a != null ? !a.equals(that.a) : that.a != null) return false;
        if (b != null ? !b.equals(that.b) : that.b != null) return false;
        if (c != null ? !c.equals(that.c) : that.c != null) return false;
        if (d != null ? !d.equals(that.d) : that.d != null) return false;
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
        int result = a != null ? a.hashCode() : 0;
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
        return "LotOfString{" +
                "a='" + a + '\'' +
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


