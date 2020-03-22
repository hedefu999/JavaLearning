package effectiveJava.c06;

import java.util.Arrays;
import java.util.Collection;

/**
 * 扩展的运算符
 * <li>{@link #EXP}</li>
 * <li>{@link #REMAINDER}</li>
 */
public enum ExtendedOperation implements Operation {
    /**
     * 指数运算
     */
    EXP("^"){
        @Override
        public double apply(double x, double y) {
            return Math.pow(x,y);
        }
    },
    /**
     * 取余运算
     */
    REMAINDER("%"){
        @Override
        public double apply(double x, double y) {
            return x%y;
        }
    };
    private String symbol;
    ExtendedOperation(String symbol) { this.symbol = symbol; }
    @Override
    public String toString() { return this.symbol; }

    public static void main(String[] args) {
        test(ExtendedOperation.class,12.45,2);
        test2(Arrays.asList(ExtendedOperation.values()),2.34,5);
    }
    private static <T extends Enum<T> & Operation> void test(Class<T> opSet, double x, double y){
        //getEnumConstants是Class.java中的方法，通过反射传入的枚举类型的values方法实现
        for (Operation op : opSet.getEnumConstants()) {
            System.out.printf("%f %s %f = %f%n", x, op, y, op.apply(x, y));
        }
    }
    private static void test2(Collection<? extends Operation> opSet, double x, double y){
        for (Operation op : opSet){
            System.out.printf("%f %s %f = %f\n",x,op,y,op.apply(x,y));
        }
    }
}
