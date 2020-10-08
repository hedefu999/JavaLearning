package misc.core;

import com.domain.UserDto;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class ListTest {
    @Test
    public void test5(){
        List<String> list = new ArrayList<>();
        list.add("jacxk");
        Collections.synchronizedList(list);
    }

    public static void main(String[] args) throws Exception{
        final HashMap<String, String> map = new HashMap<String, String>(2);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10000; i++) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            map.put(UUID.randomUUID().toString(), "");
                        }
                    }, "moon" + i).start();
                }
            }
        }, "ftf");
        t.start();
        t.join();
    }
    /**
     * List去重研究
     */
    static class ListFilterDuplicate{
        @Data
        @AllArgsConstructor
        static class User{
            private Integer id;
            private String name;

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                User user = (User) o;
                return Objects.equals(id, user.id) &&
                        Objects.equals(name, user.name);
            }

            @Override
            public int hashCode() {
                return Objects.hash(id, name);
            }
        }
        private List<User> users = Lists.newArrayList(
                new User(12,"jack"),
                new User(13,"lucy"),
                new User(12,"daniel"),
                new User(12,"xiaoming")
        );

        /*
         * 去除id不重复的user,两层for循环
         */
        @Test
        public void test45() {
            for (User user : users){
                List<User> users2 = new ArrayList<>();
                boolean b = users.stream().anyMatch(item -> item.getId().equals(user.getId()));
                if (!b){
                    users2.add(user);
                }
            }
            System.out.println(users);
        }
    }

    @Test
    public void test84() {
        List<String> list = new ArrayList<>();
        list.add("zhangsan");
        list.add("lisi");
        list.add("wangwu");
        list.add("1");
        list.add("2");
        list.add("3");
        //应该使用iterator移除元素，在遍历的时候
        for (String item : list) {
            if (item.startsWith("2")) {
                list.remove(item);
            }
            //但发现了另一个奇怪的问题，remove名字总是ConcurrentException,但remove 1不会出问题，只有1 2 两个元素在时remove 2会出错，如果有3垫底就能移除2
        }
    }

    @Test
    public void test103() {
        List<UserDto> userDtos = UserDto.userDtos;
        Iterator<UserDto> iterator = userDtos.iterator();
        System.out.println(userDtos);
        iterator.next();iterator.next();
        iterator.remove();
        System.out.println(userDtos);
        System.out.println(iterator.next());
    }
}
