package designpatternchan.c02.clientfather;

import java.util.HashMap;
import java.util.Map;

public class Client {

    public static void invoker(){
        HashMap hashMap = new HashMap();
        Map map = new HashMap();

        Father father = new Son();
        father.doSomething(hashMap);// 父类doSomething方法执行
        //father.doSomething(map1);编译不通过

        Son son =new Son();
        son.doSomething(hashMap); //父类doSomething方法执行
        son.doSomething(map);//子类doSomething重载方法被执行
    }

    public static void main(String[] args) {
        invoker();
    }
}
