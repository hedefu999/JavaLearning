package designpatternchan.c02.clientfather;

import java.util.Collection;
import java.util.Map;

public class Father {
    public Collection doSomething(Map map){
        System.out.println("父类doSomething方法执行");
        return map.values();
    }
}
