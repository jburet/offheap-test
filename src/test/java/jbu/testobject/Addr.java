package jbu.testobject;

import java.io.Serializable;

class Addr implements Serializable {
    public int number;
    public String name;
    public String postalCode;
    public String city;

    Addr(int number, String name, String postalCode, String city) {
        this.number = number;
        this.name = name;
        this.postalCode = postalCode;
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Addr addr = (Addr) o;

        if (number != addr.number) return false;
        if (city != null ? !city.equals(addr.city) : addr.city != null) return false;
        if (name != null ? !name.equals(addr.name) : addr.name != null) return false;
        if (postalCode != null ? !postalCode.equals(addr.postalCode) : addr.postalCode != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = number;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (postalCode != null ? postalCode.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        return result;
    }
}
