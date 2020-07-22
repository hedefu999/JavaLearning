package misc.core;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Java8Feature {
    /**
     * 常见的 Map<String, List<String>> 生成操作
     * computeIfAbsent key不存在时初始化一个value放到map里，并把这个value返回回来;key如果存在就是get(key)的效果
     * 返回回来的如果是集合/引用类型，可直接操作内容，否则要再put一次
     *
     */
    @Test
    public void testComputeIfAbsent() {
        String[] data = {"001-jack","002-lucy","001-new york","002-washington","003-daniel"};
        Map<String, List<String>> map = new HashMap<>();
        for (String info : data){
            String[] split = info.split("-");
            List<String> infoList = map.computeIfAbsent(split[0], nothing -> new ArrayList<>());
            infoList.add(split[1]);
        }
        System.out.println(map);
    }
    @Test
    public void testComputeIfAbsent2() {
        Map<Character,Integer> map = new HashMap<>();
        Integer integer = map.computeIfAbsent('d', key -> key + 12);
        System.out.println(integer);
        // map.put('d',++integer); 如果要实现value+1的操作，需要再put一次
        System.out.println(map);
        System.out.println(map.computeIfAbsent('d',key -> key + 13));
        System.out.println(map);
    }

    /**
     * computeIfAbsent对于非引用类型操作不方便，如
     * Integer count = rmap.computeIfAbsent(cr, key -> 0);
     * rmap.put(cr,++count);
     * 使用getordefault
     */
    @Test
    public void testGetOrDefault() {
        Map<Character,Integer> map = new HashMap<>();
        char c = 'w';
        map.put(c, map.getOrDefault(c, 0) + 1);
    }

    /**
     * Optional的使用
     */
    @Test
    public void testOptionalApi() {

    }

}
