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
        Integer preNum = high.get(romans[0]);
        sum+=preNum;
        for (int i = 1; i < romans.length; i++) {
            int value = high.get(romans[i]);
            sum += value;
            if (value > preNum){//要减数字时只需要向前看一位
                sum -= preNum*2;
            }
            preNum = value;
        }
        return sum;
    }
    /**
     * Map改switch case可以提升时间空间效率
     * 尽量避免使用java封装的复杂数据结构
     */
    public int romanToInt3(String s) {
        Map<String, Integer> map = new HashMap<>();
        map.put("I", 1);
        map.put("IV", 4);
        map.put("V", 5);
        map.put("IX", 9);
        map.put("X", 10);
        map.put("XL", 40);
        map.put("L", 50);
        map.put("XC", 90);
        map.put("C", 100);
        map.put("CD", 400);
        map.put("D", 500);
        map.put("CM", 900);
        map.put("M", 1000);

        int ans = 0;
        for(int i = 0;i < s.length();) {
            if(i + 1 < s.length() && map.containsKey(s.substring(i, i+2))) {
                ans += map.get(s.substring(i, i+2));
                i += 2;
            } else {
                ans += map.get(s.substring(i, i+1));
                i ++;
            }
        }
        return ans;
    }
    /**
     * 罗马数字 数值范围 1-3999
     * I             1
     * V             5
     * X             10
     * L             50
     * C             100
     * D             500
     * M             1000
     * IV IX XL XC CD CM 其他都是小数在大数右边
     * 罗马数字可能写法不唯一，正则 - ^M{0,4}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$
     */
    @Test
    public void test13() {
        String a = "III";
        String b = "IV";//4
        String c = "IX";//9
        String d = "LVIII";//58
        String e = "MCMXCIV";//1994
        String f = "DCC LXXX II";//782
        String g = "CCCLXXIX";//379
        String h = "MCCLXXIII";//1273  错误的罗马数字MCCCDLXXIIV
        System.out.println(romanToInt(e));
    }

    /**
     * 水平扫描
     * LCP(s1,s2,...,sn) = LCP(LCP(s1,s2)s3,...,sn)
     * TC(Time Complicity) - O(所有字符串字符数量总和)
     * SP(Space Complicity)
     */
    public String longestCommonPrefix(String[] strs) {
        if (strs.length == 0) return "";
        String prefix = strs[0];
        for (int i = 1; i < strs.length; i++){
            while (strs[i].indexOf(prefix) != 0) {
                prefix = prefix.substring(0, prefix.length() - 1);
                if (prefix.isEmpty()) return "";
            }
        }
        return prefix;
    }

    /**
     * 垂直扫描
     * 时间复杂度：O(S)，S 是所有字符串中字符数量的总和。
     *
     * 最坏情况下，输入数据为 nn 个长度为 mm 的相同字符串，算法会进行 S = m*nS=m∗n 次比较。可以看到最坏情况下，本算法的效率与算法一相同，但是最好的情况下，算法只需要进行 n*minLen 次比较，其中 minLen 是数组中最短字符串的长度。
     *
     * 空间复杂度：O(1)，我们只需要使用常数级别的额外空间。
     *
     */
    public String longestCommonPrefix2(String[] strs) {
        if (strs == null || strs.length == 0) return "";
        for (int i = 0; i < strs[0].length() ; i++){
            char c = strs[0].charAt(i);
            for (int j = 1; j < strs.length; j ++) {
                if (i == strs[j].length() || strs[j].charAt(i) != c)
                    return strs[0].substring(0, i);
            }
        }
        return strs[0];
    }

    /**
     * LCP操作的结合律：LCP(S1,S2...,Sn)=LCP(LCP(S1...Sk),LCP(Sk+1...Sn))=LCP(LCP.left, LCP.right)
     * 最坏情况：有n个长度为m的字符串。
     * TC - O(S),S=m*n
     *
     */
    public String longestCommonPrefix3(String[] strs) {
        if (strs == null || strs.length == 0) return "";
        return longestCommonPrefix(strs, 0 , strs.length - 1);
    }

    private String longestCommonPrefix(String[] strs, int l, int r) {
        if (l == r) {
            return strs[l];
        }
        else {
            int mid = (l + r)/2;
            String lcpLeft =   longestCommonPrefix(strs, l , mid);
            String lcpRight =  longestCommonPrefix(strs, mid + 1,r);
            return commonPrefix(lcpLeft, lcpRight);
        }
    }

    String commonPrefix(String left,String right) {
        int min = Math.min(left.length(), right.length());
        for (int i = 0; i < min; i++) {
            if ( left.charAt(i) != right.charAt(i) )
                return left.substring(0, i);
        }
        return left.substring(0, min);
    }

    /**
     * 二分查找法
     */
    public String longestCommonPrefix4(String[] strs) {
        if (strs == null || strs.length == 0)
            return "";
        int minLen = Integer.MAX_VALUE;
        for (String str : strs)
            minLen = Math.min(minLen, str.length());
        int low = 1;
        int high = minLen;
        while (low <= high) {
            int middle = (low + high) / 2;
            if (isCommonPrefix(strs, middle))
                low = middle + 1;
            else
                high = middle - 1;
        }
        return strs[0].substring(0, (low + high) / 2);
    }

    private boolean isCommonPrefix(String[] strs, int len){
        String str1 = strs[0].substring(0,len);
        for (int i = 1; i < strs.length; i++)
            if (!strs[i].startsWith(str1))
                return false;
        return true;
    }

    /**
     * 使用字典树查找
     */

    @Test
    public void test14() {
        String[] strs = {"flower","flow","flight"};
        String[] strs2 = {"dog","racecar","car"};
        System.out.println(longestCommonPrefix2(strs));
    }
}
