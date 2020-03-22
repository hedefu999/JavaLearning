package effectiveJava.c07;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class VarArgs {
    /**
     * @<code>int a = b;</code>
     */
    @Test
    public void test0(){
        int[] ints = {1,3,4,1,6};
        Integer[] integers = {1,4,5,3,1};
        System.out.println(ints);//[I@5b80350b
        System.out.println(integers);//[Ljava.lang.Integer;@5d6f64b1
        System.out.println(Arrays.asList(ints));//[[I@5b80350b]
        //下述3个打印可以得到理想结果，且不会去重
        System.out.println(Arrays.asList(integers));
        System.out.println(Arrays.toString(ints));
        System.out.println(Arrays.toString(integers));
/**/
    }
    @Test
    public void testNonAccurateCalc(){
        double funds = 1.00;
        int itemsBought = 0;
        for(double price = 0.10;funds>=price;price+=0.10){
            funds -= price;
            System.out.println(funds);
            itemsBought++;
        }
        System.out.println(itemsBought + "items Bought");
        System.out.println("change: "+funds);
    }
    @Test
    public void testAccurateCalc(){
        final BigDecimal TEN_CENTS = new BigDecimal(".10");
        int itemsBought = 0;
        BigDecimal funds = new BigDecimal("1.00");
        for (BigDecimal price = TEN_CENTS;
             funds.compareTo(price)>=0;
             price=price.add(TEN_CENTS)){
            itemsBought++;
            funds = funds.subtract(price);
            System.out.println("funds left: "+funds);
        }
        System.out.println(itemsBought+"items bought");
        System.out.println("money left over: "+funds);
    }
    @Test
    public void testBoxing(){
        String json = "{\"name\":\"jack\",\"home\":{\"country\":\"china\",\"province\":\"sh\"}}";
        Map<String,JSONObject> map = (Map<String, JSONObject>) JSON.parse(json);
        System.out.println(map.size());
    }

    @Test
    public void test3(){
        String[] data = {"java.util.TreeSet","df","123","*&ji"};
        System.out.println(checkAndInitSet(data));
    }
    private Set<?> checkAndInitSet(String[] data){
        Class<?> clazz = null;
        try {
            clazz = Class.forName(data[0]);
        }catch (ClassNotFoundException e){
            System.err.println("class not founs");
        }
        Set<String> set = null;
        try {
            set = (Set<String>) clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        set.addAll(Arrays.asList(data).subList(1,data.length));
        return set;
    }

}
