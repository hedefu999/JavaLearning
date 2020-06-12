package misc;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HashCodeTest {
    static class User1{
        private String name;
        public User1(String name) {
            this.name = name;
        }
        //下面的equals和hashcode方法注掉，junit test 中就打印 null
        //@Override
        //public boolean equals(Object o) {
        //    if (this == o) return true;
        //    if (o == null || getClass() != o.getClass()) return false;
        //    User1 user1 = (User1) o;
        //    return Objects.equals(name, user1.name);
        //}
        //
        //@Override
        //public int hashCode() {
        //    return Objects.hash(name);
        //}
    }
    @Test
    public void test11(){
        Map<User1,String> map = new HashMap<>();
        map.put(new User1("jack"),"chen");
        String jackFamily = map.get(new User1("jack"));
        System.out.println(jackFamily);
    }
}
