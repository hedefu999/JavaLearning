package misc.core;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashMap;

public class MapTest {
    @Test
    public void test270(){
        HashMap<String,Integer> map = new HashMap();
        map.put("a",2);map.put("b",3);map.put("c",4);map.put("4",5);map.put("d",5);
        map.put("e",5);map.put("6",5);map.put("7",5);map.put("8",5);map.put("i",5);
        printHashMapNodes(map);
        map.put("k",5);map.put("l",5);map.put("n",5);map.put("x",5);
        printHashMapNodes(map);
        System.out.println("----------------");
        String[] keysIn16 = {"a","b","c","4","d","e","6","7","8","i"};
        String[] keysIn32 = {"a","b","c","4","d","e","6","7","8","i","k","l","n","x"};
        printKeyIndexAndHashInHashMap(keysIn16);
        printKeyIndexAndHashInHashMap(keysIn32);
    }
    public static int getHashMapCapcity(int keysCount,float loadFactor){
        if (16*loadFactor > keysCount){
            return 16;
        }
        int n = keysCount - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        int min2power =  (n < 0) ? 1 : (n >= 1 << 30) ? 1 << 30 : n + 1;
        if (min2power*loadFactor < keysCount){
            return min2power*2;
        }
        return min2power;
    }
    public static void printKeyIndexAndHashInHashMap(Object[] keys){
        for (Object key : keys){
            int keyHash = getKeyHash(key);
            int capcity = getHashMapCapcity(keys.length, 0.75f);
            int index = getIndexInTable(keyHash,capcity);
            System.out.printf("%d-%d; ",index,keyHash);
        }
        System.out.println();
    }
    public static void printHashMapNodes(HashMap map){
        try {
            Class<?> hashMapClazz = Class.forName("java.util.HashMap");
            Class<?> nodeClazz = Class.forName("java.util.HashMap$Node");
            Field nodeNextFiled = nodeClazz.getDeclaredField("next");
            Field nodeHashFiled = nodeClazz.getDeclaredField("hash");
            nodeHashFiled.setAccessible(true);
            nodeNextFiled.setAccessible(true);
            Field hashMapTable = hashMapClazz.getDeclaredField("table");
            hashMapTable.setAccessible(true);
            Object[] os = (Object[]) hashMapTable.get(map);
            System.out.printf("当前table数组大小：%d\n", os.length);
            for (int i = 0; i < os.length; i++) {
                int indent = 1;
                Object node = os[i];
                while (node != null){//有很多节点是空的
                    System.out.printf("%"+indent+"sNode{ index-hash: %d-%d; %s}\n",
                            "+", i, nodeHashFiled.get(node), node.toString());
                    node = nodeNextFiled.get(node);
                    indent = 2;
                }
            }
        }catch (Exception e){e.printStackTrace();}
    }

    private static int getIndexInTable(int hashCode, int capcity) {
        return hashCode & (capcity-1);
    }
    private static int getKeyHash(Object key){
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
    @Test
    public void test81(){
        int h = 123450000;
        int hash = (h ) ^ (h >>> 16);
        System.out.println(hash);
    }
}
