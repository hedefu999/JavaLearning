package misc.homemadecache;

import lombok.AllArgsConstructor;
import lombok.Data;

public class CacheClient {
    @Data
    @AllArgsConstructor
    static class Student{
        private String name;
        private Integer age;
    }
    public static void main(String[] args) {
        new ExpireThread();
        CacheUtils.put("0001",new Student("jack",12),3);
        CacheUtils.put("0002",new Student("lucy",14),8);
        System.out.println(CacheUtils.get("0001"));
        System.out.println(CacheUtils.get("0004"));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(CacheUtils.get("0001"));
        System.out.println(CacheUtils.get("0002"));
    }
}
