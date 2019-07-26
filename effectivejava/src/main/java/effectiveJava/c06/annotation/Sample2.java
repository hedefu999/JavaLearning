package effectiveJava.c06.annotation;

public class Sample2 {
    @ExceptionTest(ArithmeticException.class)
    public static void m1(){
        int i = 1/0;
    }
    @ExceptionTest({ArrayIndexOutOfBoundsException.class,ArithmeticException.class})
    public static void m2(){
        int i = 1/0;
        int a[] = new int[2];
        a[4]=4;
    }
}
