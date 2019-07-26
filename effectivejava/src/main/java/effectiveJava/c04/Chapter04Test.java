package effectiveJava.c04;

import org.junit.Test;

public class Chapter04Test {
    @Test
    public void test1(){
        TestFinal testFinal = new TestFinal();
        Integer[] data = testFinal.getArray0();
        Integer[] data2 = testFinal.getArray0();
        System.out.println(data == data2);

        Integer[] data3 = testFinal.getArray2();
        Integer[] data4 = testFinal.getArray2();
        System.out.println(data3.equals(data4));


    }
}
