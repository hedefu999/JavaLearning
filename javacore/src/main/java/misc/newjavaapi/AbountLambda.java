package misc.newjavaapi;

import java.util.function.Function;

public class AbountLambda {
    /**
     * 匿名内部类中的this指的是：匿名内部类对象本身
     * 所以 Thread thread = ()->{this.可以使用isInterrupt方法}
     */
    static class ThisInAnnoyInner{
        public static void main(String[] args) {
            Function<String,String> func = new Function<String, String>() {
                @Override
                public String apply(String s) {
                    System.out.println(this.getClass().getName());
                    return "";
                }
            };
            func.apply("name");
        }/*
         * misc.newjavaapi.AbountLambda$ThisInAnnoyInner$1
         */
    }

    /**
     * lambda表达式中的this指lambda所在方法的所属类，比匿名内部类的要更靠外一层
     * 所以在main方法中的lambda表达式中使用this会提示this cant be accessed in static method
     */
    static class ThisInLambda{
        public static void main(String[] args) {
            Function<String,String> func = (s)->{
                //System.out.println(this.getClass().getName());
                return "";
            };
            new ThisInLambda().method();
        }
        public void method(){
            Function<String,String> func = (s)->{
                System.out.println(this.getClass().getName());
                this.method2();
                return s;
            };
            func.apply("执行");
        }
        public void method2(){
            System.out.println("lambda中的this不同于匿名内部类的this");
        }
        /*
         * misc.newjavaapi.AbountLambda$ThisInLambda
         * lambda中的this不同于匿名内部类的this
         */
    }
    /**
     * 造成lambda与匿名内部类的区别的原因是编译器
     * 可查看字节码发现编译器的操作 javap -c xxx.class
     */
}
