package leetcode;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class StringTagSimple {
    public int romanToInt(String s) {
        char[] romans = s.toCharArray();
        int sum = 0;
        Map<Character,Integer> high = new HashMap<>();
        high.put('M',1000);
        high.put('D',500);
        high.put('C',100);
        high.put('L',50);
        high.put('X',10);
        high.put('V',5);
        high.put('I',1);
        Stack<Integer> assist = new Stack<>();
        for (Character c : romans){
            int value = high.get(c);
            sum += value;
            if (assist.empty()){

            }
            if (value <= assist.peek()){
                assist.push(value);
            }else {
                int deduct = assist.peek();
                while (assist.peek() == deduct){
                    sum -= assist.pop();
                }
            }
        }
        return sum;
    }

    /**
     * 罗马数字 数值范围 1-3999
     * I             1
     * V             5
     *
     * X             10
     * L             50
     *
     * C             100
     * D             500
     *
     * M             1000
     * IV IX XL XC CD CM 其他都是小数在大数右边
     */
    @Test
    public void test13() {
        String a = "III";
        String b = "IV";//4
        String c = "IX";//9
        String d = "LVIII";//58
        String e = "M CM XC IV";//1994
        String f = "DCC LXXX II";//782
        String g = "CCC LXX IX";//379
        String h = "M CCCD XLL IIV";//1273
        System.out.println(romanToInt(h));
    }
}
