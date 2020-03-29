package misc.newjavaapi;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;

import java.util.*;

/**
 * 本类用于测试JDK8相关API，包含 流式编程
 */
public class JDK8ListMapTest {

    @Test
    public void test(){
        Map<String, List<Integer>> data = new HashMap<>();
        data.computeIfAbsent("aaa",key -> new LinkedList<>());
        data.get("aaa").add(12);
        data.get("aaa").addAll(Arrays.asList(23,24,45));
        System.out.println(data);
    }

    @Data
    @AllArgsConstructor
    static class Student{
        private String name;
        private String city;
        private Integer age;
    }
    @Test
    public void test18(){
        Student jack = new Student("jack","jarney",12);
        Student lucy = new Student("lucy","dunming",13);
        Student daniel = new Student("daniel","aseelia",14);
        Student lily = new Student("lily","jarney",12);
        Student talker = new Student("talker","jarney",12);

        /**
         * 1.这种写法直接抛空指针异常
         * List<Student> data = Arrays.asList(jack,lucy,daniel,null,lily,talker);
         * Map<String, Student> studentMap = data.stream().collect(Collectors.toMap(Student::getName, stu -> stu));
         *
         * 2.这种写法抛键名冲突异常 java.lang.IllegalStateException: Duplicate key JDK8ListMapTest.Student(name=jack, city=jarney, age=12)
         * List<Student> data = Arrays.asList(jack,lucy,daniel,lily,talker);
         * Map<String, Student> map = data.stream().collect(Collectors.toMap(Student::getCity, stu -> stu));
         *
         * 3.解决上面的键名冲突问题的一个办法
         * List<Student> data = Arrays.asList(jack,lucy,daniel,lily,talker);
         * Map<String, Student> map = data.stream().collect(Collectors.toMap(Student::getCity, stu -> stu,(val1,val2) -> val1));
         * 第三个BinaryOperator<U>参数可以自定义键冲突时的处理策略
         * (val1,val2) -> val1  表示抛弃第二个key相同的元素
         * (val1,val2) -> val2  表示用第二个值覆盖
         * (val1,val2)-> new Student(val1.getName()+","+val2.getName(),val1.getCity(),val1.getAge()+val2.getAge())  奇葩的拼接规则
         *
         *
         */
        //下述写法不仅能处理item为空的问题，也能解决键名冲突的问题，但只能覆盖冲突的键名
        List<Student> data = Arrays.asList(jack,lucy,daniel,null,lily,talker);
        HashMap<Object, Object> map = data.stream().collect(HashMap::new, ((hashMap, student) -> {if(student != null){hashMap.put(student.getCity(), student);}}), HashMap::putAll);
        System.out.println(map);
    }

    /**
     * List去除null元素的n种方法
     */
    @Test
    public void test66(){
        Student jack = new Student("jack","jarney",12);
        Student lucy = new Student("lucy","dunming",13);
        Student daniel = new Student("daniel","aseelia",14);
        Student nullu = null;
        //使用Arrays.asList()生成的List不支持remove、add操作
        //注意Arrays#ArrayList 非java.util.ArrayList!
        //List<Student> notSupportAddRemoveList = Arrays.asList(jack,lucy,nullu,daniel);
        //ArrayList<Student> students = new ArrayList<>(notSupportAddRemoveList);
        //此处使用guava api,也可以像上面一行拷贝到ArrayList
        List<Student> data = Lists.newArrayList(jack,lucy,nullu,daniel);
        System.out.println(data);

        //> JDK API
            //修改原List
        //data.removeAll(Collections.singleton(null));
            //保留原List
        //List<Student> newdata = data.stream().filter(Objects::nonNull).collect(Collectors.toList());
        //System.out.println(newdata);

        //> guava API
            //修改原List
        //Iterables.removeIf(data,Objects::isNull);
            //保留原List，新建一个List
        //List<Student> newdata = Lists.newArrayList(Iterables.filter(data, Objects::nonNull));
        //System.out.println(newdata);

        //> apache-commons api
        CollectionUtils.filter(data, Objects::nonNull);

        System.out.println(data);

    }

    public static void removeDuplicateAndEmptyItem(List<Student> input){
        List<Object> list = new ArrayList<>(null);
        input.removeAll(list);
        LinkedHashSet<Student> set = Sets.newLinkedHashSetWithExpectedSize(input.size());
        set.addAll(input);
        input.clear();
        input.addAll(set);
    }
}
