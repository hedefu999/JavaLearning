package misc.core;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
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

        @Test
        public void test68() {

        }

    }
}
