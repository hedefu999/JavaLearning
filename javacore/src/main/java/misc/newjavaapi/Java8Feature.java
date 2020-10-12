package misc.newjavaapi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import misc.newjavaapi.Java8StreamAPI.Student;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

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

    static class FunctionalInterfaceTest{
        @Data @AllArgsConstructor
        static class Apple{
            private String color;
            private double weight;
        }
        // @FunctionalInterface 这个注解仅用于提示、声明、限制，不影响lambda表达式发挥作用
        public interface AppleFilter{
            boolean filter(Apple apple);
        }
        public static void filterApple(Apple apple, AppleFilter filter) {
            if (filter.filter(apple)) {
                System.out.println(apple);
            }
        }

        public static void main(String[] args) {
            Apple apple = new Apple("RED",12.3);
            filterApple(apple, item -> item.getColor().equals("RED") && item.getWeight() > 10.0);
        }
    }

    /**
     * Lambda表达式对外层作用域的变量操作存在限制
     */
    @Test
    public void testVariableReferenceInLambdaExp() {
        int x = 200;
        Runnable runnable = ()-> System.out.println(x);
        // Runnable runnable2 = ()-> x++;// Variable used in lambda expression should be final or effectively final
        /**
         * 对局部变量只能都不能改，因为局部变量保存在栈上，隐式表示他们仅限于其所在线程。
         * 而如果允许lambda表达式修改时，如果这个lambda传给了一个Runnable，就会产生两个线程操作一个局部变量的问题，带来线程安全问题
         * 相对地，实例变量保存在堆中，而堆在线程间是共享的
         */
        /**-=-=-= 如果确实需要修改外层变量，通常使用forEach时会有这种操作，有3种方式 -=-=-=**/
        AtomicInteger y = new AtomicInteger(300);
        Stream.of(1,2,3).forEach(item -> {
            y.getAndIncrement();
        });

        final int[] z = {400};
        System.out.println(Thread.currentThread().getName());
        Stream.of(1,2,3).forEach(item -> {
            System.out.println(Thread.currentThread().getName());//lambda表达式并未在另一个线程中执行
            z[0]++;
        });
        System.out.println(z[0]);


        Stream.of(1,2,3).forEach(item -> {
            a = a + item%2;
        });
    }
    static int a = 500;

    /**
     * Optional的使用
     */
    static class OptionalApiTest {
        @AllArgsConstructor @ToString
        static class User implements Serializable {
            private static final long serialVersionUID = 2656912883914237052L;
            public static User JACK = new User("jack", "London");
            private String name;
            private String address;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public Optional<String> getAddress() {
                return Optional.ofNullable(address);
            }

            public void setAddress(String address) {
                this.address = address;
            }
        }
        public static void main(String[] args) {
            String input = "java 8 optional";
            // input = null;
            //如果input不为空，并且长度超过10就提取第9个字符t，否则返回字符0
            char tenthCharacter = Optional.ofNullable(input).filter(str -> str.length() > 10).map(str -> str.charAt(9)).orElse('0');
            System.out.println(tenthCharacter);
            // EnumSet.noneOf()
            Optional<Student> optStu = Optional.empty();
            // Optional.of(obj) 与 Optional.ofNullable(obj) 前者不允许传入null
            String s = Optional.ofNullable(input).orElseThrow(() -> new RuntimeException("test optional's throw exception"));

            //flatMap可以对Optional返回类型的getter方法一步到位取得值
            String address = Optional.of(User.JACK).flatMap(User::getAddress).orElse("earth");
            //Optional.of(User.JACK).map(User::getAddress).orElse(Optional.of("earth")).orElse(""); 这种写法很难看
            testSerializeObjectWithOptionalFields(false);
        }
        public static void testSerializeObjectWithOptionalFields(boolean tag){
            String fileName = "jack_user.objstream";
            try {
                if (tag){
                    FileOutputStream fs = new FileOutputStream(fileName);
                    ObjectOutputStream os = new ObjectOutputStream(fs);
                    os.writeObject(User.JACK);
                    os.close();
                }else {
                    FileInputStream fs = new FileInputStream(fileName);
                    ObjectInputStream os = new ObjectInputStream(fs);
                    User dest = (User)os.readObject();//再强制转换
                    System.out.println(dest);
                    os.close();
                }
            }catch (Exception e){
            }

        }
    }


}
