package misc.core;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 泛型研究
 */
public class GenericsTest {
    /**
     # PECS原则研究
     Fruit extends Food, Food super Fruit
     但 List<Fruit> 并不extends List<Food>
     */
    static class PECSPrinciple{
        static class Food {}
        static class Fruit extends Food{}
        static class Apple extends Fruit{}
        static class Orange extends Fruit{}
        static class RedFuji extends Apple{}
        /*
        //协变 <? extends Fruit>
        //容纳Fruit子类的List
        List<? extends Fruit> fruits = new ArrayList<>();
        //不能加入任何元素
        fruits.add(new Food());     // compile error
        fruits.add(new Fruit());    // compile error
        fruits.add(new Apple());    // 【A处】 但是并不能加入Fruit的子类Apple，因为不能确定? extends Fruit指的是Fruit的哪个子类，所以禁止加入任何元素
        //集合元素的类型，符合extends Fruit，可赋值给 变量fruits
        fruits = new ArrayList<Food>(); // compile error
        fruits = new ArrayList<Fruit>(); // compile success
        fruits = new ArrayList<Apple>(); // compile success，此时fruits已不是协变类型，是Apple类型的集合，所以下面语句正确
        fruits.add(new Apple());   //compile error,原因与A处相同
        //不能往一个 ？extend XXX的集合里写入任何数据
        fruits = new ArrayList<? extends Fruit>(); // 在java中会出现 compile error: 通配符类型无法实例化
        Fruit object = fruits.get(0);    // compile success 虽然不能写入，但读取却是可以的

        List<? super Apple> fruits2 = new ArrayList<>();
        fruits.add(new Apple()); //work
        fruits.add(new Fruit());                 //compile error
        fruits.add(new Object());                //compile error
        * */
        public static void main(String[] args) {
            List<? extends Fruit> fruits = new ArrayList<Apple>();
            Fruit object = fruits.get(0);//协变禁止放入任何元素，但允许取，作为Producer要Extends

            List<? super Apple> fruits2 = new ArrayList<Fruit>();
            fruits2.add(new Apple());//只可以放入Apple及其子类
            // fruits2.add(new Orange());
            fruits2.add(new RedFuji());
            Fruit fruit = fruits.get(0);//只能取出Apple的父类
            //逆变只能取出父类，但可以放入本身及其子类，作为Consumer要Super
        }
    }

    @Test
    public void test1613() {
        List<String> generics = null;
        List notGenerics = new ArrayList(10);
        notGenerics.add(new Object());
        notGenerics.add(new Integer(1));
        generics = notGenerics;
        String name = generics.get(0);//java.lang.ClassCastException: java.lang.Object cannot be cast to java.lang.String
        //注意使用instanceof
    }
}
