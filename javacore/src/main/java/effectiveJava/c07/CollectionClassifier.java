package effectiveJava.c07;

import java.util.*;

public class CollectionClassifier {
    public static String classify(Set<?> set){
        return "set";
    }
    public static String classify(List<?> list){
        return "list";
    }
    public static String classify(Collection<?> collection){
        return "collection";
    }
    //重载方法的调用是在编译时就确定了的，IDEA只会提示第3个classify方法 can be private
    public static void main(String[] args) {
        Collection<?>[] collectionss = {
                new HashSet<>(),
                new ArrayList<>(),
                new HashMap<String,Integer>().values()
        };
        for (Collection<?> c : collectionss){
            System.out.println(classify(c));
        }
    }
}
