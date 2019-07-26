package effectiveJava;


import effectiveJava.c02.Singleton;
import effectiveJava.c03.CaseInsensitiveString;
import org.junit.Test;

import java.lang.reflect.Constructor;

public class ChapterTest {
    public static void testSingleton() throws Exception{
        Singleton sing1 = Singleton.INSTANCE;
        Singleton sing2 = Singleton.INSTANCE;
        Class clazz = Class.forName("effectiveJava.c02.Singleton");
        Constructor<Singleton>[] constructors = clazz.getDeclaredConstructors();
        Constructor<Singleton> cons0 = constructors[0];
        cons0.setAccessible(true);
        Singleton sing3 = cons0.newInstance();

        System.out.println(sing1 == sing2);
        System.out.println(sing1 == sing3);
    }
    @Test
    public static void test8(){
        CaseInsensitiveString str1 = new CaseInsensitiveString("hello");
        String str2 = "Hello";
        String str3 = "hello";
        System.out.println(str1.equals(str2));//true
        System.out.println(str2.equals(str1));//false
        System.out.println(str2.equals(str3));//false
        System.out.println(str1.equals(str3));//true
        System.out.println(str3.equals(str1));//false
    }
    public static void main(String[] args) throws Exception {


    }
}
