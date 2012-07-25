package jbu.testobject;

import java.io.Serializable;


class Client implements Serializable {
    public String firstName;
    public String lastName;
    public int age;
    public Addr addr;

    public Client() {
    }

    public Client(String firstName, String lastName, int age, Addr addr) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.addr = addr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Client that = (Client) o;

        if (age != that.age) return false;
        if (addr != null ? !addr.equals(that.addr) : that.addr != null) return false;
        if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null) return false;
        if (lastName != null ? !lastName.equals(that.lastName) : that.lastName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = firstName != null ? firstName.hashCode() : 0;
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + age;
        result = 31 * result + (addr != null ? addr.hashCode() : 0);
        return result;
    }
}
