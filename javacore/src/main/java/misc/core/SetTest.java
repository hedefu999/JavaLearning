package misc.core;

import org.junit.Test;

import java.util.*;

public class SetTest {
    static class User implements Comparable<User>{
        private String user;

        public User(String user) {
            this.user = user;
        }

        @Override
        public String toString() {
            return "User{" +
                    "user='" + user + '\'' +
                    '}';
        }

        @Override
        public int compareTo(User o) {
            return this.user.compareTo(o.user);
        }
    }
    @Test
    public void test5(){
        TreeSet<String> treeSet = new TreeSet<>();
        //List<Integer> list = new ArrayList<>();
        treeSet.add("jack");
        treeSet.add("lucy");
        treeSet.add("daniel");
        Iterator<String> iterator = treeSet.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }
        //TreeSet元素必须 implements Comparable，Comparable的泛型声明是Comparable<User>不是Comparable<String>
        Set<User> set = new TreeSet<>();
        set.add(new User("jack"));
        set.add(new User("lucy"));
        set.add(new User("daniel"));
        Iterator<User> iterator1 = set.iterator();
        while (iterator1.hasNext()){
            System.out.println(iterator1.next());
        }
        //System.out.println(iterator.next()); 抛出异常
        //TreeSet的元素被排序了，丢失元素添加的顺序
    }
    @Test
    public void test44(){
        Set<String> set = new LinkedHashSet<>();
        set.add("jack");
        set.add("lucy");
        set.add("daniel");
        for (String item:set){
            System.out.println(item);
        }
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }
        //很明显，HashSet加链表，就是为了保留元素add的顺序
    }
}
