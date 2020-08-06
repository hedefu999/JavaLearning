package misc.javaagent.senior;

import java.util.HashMap;
import java.util.Map;

public class TimeHolder {
    private static Map<String, Long> timeCache = new HashMap<>();

    public static void start(String method){
        timeCache.put(method, System.currentTimeMillis());
    }
    public static long cost(String method){
        return System.currentTimeMillis() - timeCache.get(method);
    }
}
