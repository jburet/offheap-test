package jbu.cache;

import jbu.map.MapCache;
import jbu.offheap.AddrAlign;
import org.junit.Test;

import java.io.Serializable;

import static org.junit.Assert.assertEquals;

public class TestMapCache {

    @Test
    public void test1() {
        MapCache<Integer, TestObject> cache = new MapCache<Integer, TestObject>();
        TestObject source = generateTestObject(0);
        cache.put(1, source);
        TestObject res = cache.get(1);
        assertEquals(source, res);
    }

    @Test
    public void test2() {
        MapCache<Integer, TestObject> cache = new MapCache<Integer, TestObject>();
        long start = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            TestObject source = generateTestObject(0);
            cache.put(i, source);
            TestObject res = cache.get(i);
            assertEquals(source, res);
        }
        System.out.println("Write/Read 10000 object in : " + (System.nanoTime() - start)/1000/1000 + " ms");
    }

    private TestObject generateTestObject(int i) {
        Addr addr = new Addr(i, "le nom de la rue" + i, "1000" + i, "LA VILLE" + i);
        TestObject to = new TestObject("firstName" + i, "lastName" + i, 18, addr);
        return to;
    }
}


class TestObject implements Serializable {
    public String firstName;
    public String lastName;
    public int age;
    public Addr addr;

    TestObject(String firstName, String lastName, int age, Addr addr) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.addr = addr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestObject that = (TestObject) o;

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