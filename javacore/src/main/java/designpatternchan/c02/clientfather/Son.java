package designpatternchan.c02.clientfather;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Son extends Father {
    //放大输入参数类型，这不是方法的override，入参并不相同
    public Collection doSomething(HashMap map){
        System.out.println("子类doSomething重载方法被执行");
        return map.values();
    }

    @Override
    public Set doSomething(Map map) {
        System.out.println("子类doSomething覆写方法被执行");
        return null;
    }
}
