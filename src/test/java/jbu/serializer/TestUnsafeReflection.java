package jbu.serializer;

import jbu.UnsafeReflection;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: julienburet
 * Date: 19/06/12
 * Time: 21:04
 * To change this template use File | Settings | File Templates.
 */
public class TestUnsafeReflection {

    @Test
    public void test_get_int() throws NoSuchFieldException {
        Field f = A.class.getDeclaredField("a");
        A a = new A();
        assertEquals(42, UnsafeReflection.getInt(f, a));
    }

    @Test
    public void test_get_string() throws NoSuchFieldException {
        Field f = A.class.getDeclaredField("b");
        A a = new A();
        assertEquals("42", UnsafeReflection.getObject(f, a));
    }

    @Test
    public void test_array_size_in_mem() throws NoSuchFieldException {
        Field f = A.class.getDeclaredField("ar");
        A a = new A();
        Object array = UnsafeReflection.getObject(f, a);
        System.out.println(UnsafeReflection.getArraySizeContentInMem(array));
    }

}

class A {
    private int a = 42;
    private String b = "42";
    private long[] ar = new long[]{1, 2, 5, 10, 20};
    private int[][] ar2 = new int[][]{{1,1},{1,1},{1,1}};
}
