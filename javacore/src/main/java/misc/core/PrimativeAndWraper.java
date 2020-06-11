package misc.core;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PrimativeAndWraper {
    @Test
    public void test5(){
        char a = 'a'+65034;
        char b = 65535;
        //char m='中'+'国'+'国'+'国';
        char m='中'+1;
        System.out.println(m);

        System.out.println(Integer.toBinaryString(m));
        TestClass testClass = new TestClass();
        //testClass.b c d

    }

    class Person{}
    @Test
    public void test16(){
        int b = 2;
        Integer a = 3;
        //System.out.println(b instanceof Integer);
        Person person = new Person();
        //System.out.println(person instanceof String);
        System.out.println(person instanceof List);
        List<Person> list = new ArrayList<>();
        //System.out.println(list instanceof List<Person>);
        String[] array = new String[5];
        System.out.println(array instanceof Object);
        System.out.println(list instanceof Object);
        System.out.println(array.getClass().getSuperclass().getName());
        System.out.println(array.getClass().getName());
    }
    @Test
    public void test41(){
        int i = 0;
        int j = 2;
        i = j = 3;
        System.out.println(i+" "+j);
    }
    @Test
    public void test48(){
        int i = 3;
        System.out.println(i+'a');
//据此可以进行int到char的转化，下面两种方式相同
        char j = (char)(i+48);  //(char)(i+'0');
        char ch = '9';
        int n = (int)(ch) - (int)('0');//此处ch也是‘0’至‘9’的数字字符
    }
}
