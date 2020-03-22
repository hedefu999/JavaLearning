package effectiveJava.c04;

public interface Acca {
    //public abstract 可以省略
    public abstract void getA();
    default void getB(){}
    public static String getC(){return "";}
    //java9才支持
    //private Integer getD(){return 0;}
}
