package leetcode;

import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

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

    /** 14 最长公共前缀 todo 待回溯
     * 水平扫描
     * LCP(s1,s2,...,sn) = LCP(LCP(s1,s2)s3,...,sn)
     * TC(Time Complicity) - O(所有字符串字符数量总和)
     * SC(Space Complicity)
     */
    public String longestCommonPrefix3_1(String[] strs) {
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
     * 最坏情况：有n个长度为m的完全相同的字符串。
     * TC - O(S),S=m*n
     * TC的递推式 T(n) = T(left)+T(right)+O(m) = 2*T(n/2)+O(m) [todo 如何化简至O(S),参考离散数学]
     * SC - O(m*log(n))
     */
    public String longestCommonPrefix3(String[] strs) {
        if (strs == null || strs.length == 0) return "";
        return longestCommonPrefix3_1(strs, 0 , strs.length - 1);
    }
    private String longestCommonPrefix3_1(String[] strs, int l, int r) {
        if (l == r) {
            return strs[l];
        }
        else {
            int mid = (l + r)/2;
            String lcpLeft = longestCommonPrefix3_1(strs, l , mid);
            String lcpRight = longestCommonPrefix3_1(strs, mid + 1,r);
            return commonPrefix3_2(lcpLeft, lcpRight);
        }
    }
    String commonPrefix3_2(String left, String right) {
        int min = Math.min(left.length(), right.length());
        for (int i = 0; i < min; i++) {
            if ( left.charAt(i) != right.charAt(i) )
                return left.substring(0, i);
        }
        return left.substring(0, min);
    }/**
     * 递归产生的数据、待执行方法在堆栈中是怎样变化的？
     *
     */

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
            if (isCommonPrefix4_1(strs, middle))
                low = middle + 1;
            else
                high = middle - 1;
        }
        return strs[0].substring(0, (low + high) / 2);
    }

    private boolean isCommonPrefix4_1(String[] strs, int len){
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
        System.out.println(longestCommonPrefix3(strs));
    }

    /**
     * 反转整数. 32位整数的取值范围 -2^31 - 2^31-1，即
     * 123 - 321; -123 - -321; 120 - 21;
     */
    //错误思路
    public int reverseError(int x){
        int level = (int)Math.log10(Math.abs(x));
        int result = handlerError(x,level);
        return result==-1?0:result;
    }
    public int handlerError(int x, int level){
        if (x/10 == 0){
            return x;
        }
        int reverse = handlerError(x / 10,level-1);
        if (reverse == -1)//递归时拿到溢出判断结果，没法直接返回，递归写法存在缺陷，放弃。。。
            return -1;
        long higher = (long) Math.pow(10, level) * (x % 10);//重点在于,这一步如何检测发生了溢出
        if ((higher <= -1<<31 || higher >= (1<<31)-1))
            return -1;
        return (int)higher+reverse;
    }

    /**
     * 编码中的难点在于 后面的数字*10^x + 前面的数 可能会溢出,判断溢出的方式有
     * 1. 转成long跟 2147483647 比较
     * 2. 在进行 之前的结果reverse*10+个位数 之前,比较reverse与Integer.Max/10的大小差异
     *     正数的情况:
     *      reverse > Integer.max/10,一定溢出;
     *      reverse == Integer.max/10 并且 个位数>7(也就是2147483647的个位数)
     *     负数的情况:
     *      reverse < Integer.min/10,一定溢出;
     *      reverse == Integer.min/10 并且 个位数<-8(-2147483648的个位数)
     *    这种解法思路清晰,性能也好
     *    OC = O(log(x)) SC = O(1)
     */
    public int reverseOfficialy(int x){
        int reverse = 0;
        while (x != 0){
            int lowestNumber = x % 10;
            x /= 10;
            if (reverse > 0x7fffffff/10 || (reverse == 0x7fffffff/10 && lowestNumber > 7))
                return 0;
            if (reverse < 0x80000000/10 || (reverse == 0x80000000/10 && lowestNumber < -8))
                return 0;
            reverse = reverse * 10 + lowestNumber;
        }
        return reverse;
    }
    //暴力解法,直接用long型解决溢出的问题(如果限制使用32位整数就不可行了)
    public int reverseWithLong(int x){
        long reverse = 0;
        while (x!=0){
            reverse = reverse*10 + x%10;
            x = x / 10;
            if (reverse > Integer.MAX_VALUE || reverse < Integer.MIN_VALUE){
                return 0;
            }
        }
        return (int)reverse;
    }
    @Test
    public void test7() {
        int number=12;//
        int number2 = 1463847422;//2147483647;
        int number3 = -1463847412;
        int reverse = reverseWithLong(number3);
        System.out.println(reverse);
    }

    /** 28 子串位置
     * haystack = "hello", needle = "ll"
     * 输出: 2
     * haystack = "aaaaa", needle = "bba"
     * 输出: -1
     * 解法1:逐个从头开始比较
     * 解法2:Rabin Karp 常数复杂度
     *  可以将纯小写的字符串看作26进制数,计算出对应的10进制数字作为hash进行比较就快了
     *  所以这种算法的时间消耗在许多子串的hash计算上
     *  关于此问题的wiki - https://en.wikipedia.org/wiki/Linear_congruential_generator#Parameters_in_common_use
     */
    //mySolution 线性时间复杂度 TC=O((M-N)N) SC=O(1)
    public int strStr(String haystack, String needle) {
        if (needle == null || needle.length() == 0) return 0;
        if (haystack == null || haystack.length() == 0) return -1;
        int haystackLength = haystack.length();
        int needleLength = needle.length();
        for (int i = 0; i <= haystackLength - needleLength; i++) {
            if (haystack.charAt(i) == needle.charAt(0)){
                int j = 0;
                //for循环使用subString也可以
                while (j < needleLength && haystack.charAt(j+i) == needle.charAt(j)){
                    j++;
                }
                if (j == needleLength){
                    return i;
                }
            }
        }
        return -1;
    }
    //解法2- 万物皆可hash

    /**
     * 26个英文字母序号对照表
     * a b c d e f g h i j  k  l  m  n
     * 0 1 2 3 4 5 6 7 8 9 10 11 12 13
     * 根据rabinKarp算法， he计算出的hash是186,ll的hash是297（11*26^0+11*26^1 = 11+286）
     * 注意是从左扫描，高阶次位在左边，类似通常的数字
     */
    public int rabinKarpSolution(String haystack, String needle){
        int L = needle.length(),n=haystack.length();
        if (L > n) return -1;

        int a = 26;
        //使用一个较大的数进行取模，避免溢出
        long modules = (long)Math.pow(2,31);//int最大2^31-1,这里取模用的是2^31

        long h = 0, ref_h = 0;
        //先计算haystack前L个字符的hash，顺便把needle的hash(ref_h)计算出来以便使用
        for (int i = 0; i < L; i++) {
            //关键计算公式：将字符串视作二十六进制数，计算十进制值作为hash
            h = (h*a + charToInt(haystack,i)) % modules;
            ref_h = (ref_h * a + charToInt(needle,i)) % modules;
        }
        if (h == ref_h) return 0;

        long aL = 1;
        for (int i = 1; i <= L; i++) {
            aL = (aL * a) % modules;
        }
        //aL=26^L=676，这里计算这个就是为了下面计算hash方便使用的一个常量
        //为节省hash计算的成本，并不是每次重新计算haystack滑动窗口内字符串的hash，而是减去左侧低位
        /*
         * 滑动窗口：h  e      e  l
         * 序   号：7  4  ==> 4  11
         * 阶   次：26 1      26  1
         * 滑动窗口前进一次，7*26+4*1==>4*26+11*1, reHash的公式是：hash=hash*level-preNo*level^L+nextNo
         */
        for (int start = 1; start < n - L + 1; start++) {
            h = ((h*a) - charToInt(haystack,start - 1) * aL
                    + charToInt(haystack, start+L-1))%modules;
            if (h == ref_h) return start;
        }
        return -1;
    }
    //第index位上的二十六进制值，index从0开始
    private int charToInt(String str, int index) {
        return (int) str.charAt(index) - (int)'a';
    }

    @Test
    public void test28() {
        String haystack = "hello", needle = "ll";
        String haystack1 = "aaaaa", needle1 = "bba";
        System.out.println(rabinKarpSolution(haystack,needle));
        /**
         *
         */
        Double a = 234.45D;
    }
    /**
     * 38外观数列
     *  n=1,固定为 1
     *  n=2,观察上一个n=1的结果的情况：1个1 ==> 11
     *  n=3,观察n=2是有两个1 ==> 21
     *  n=4,观察n=3时有1个2，1个1 ==> 1211
     *  n=5 1个1，1个2，两个1 ==> 111221
     *  n=6 312211
     *   n  ?
     */
    public String countAndSay(int n) {
        if (n==1) return "1";
        String pre = countPre("1");
        for (int i = 2; i < n; i++) {
            pre = countPre(pre);
        }
        return pre;
    }
    public String countPre(String s){
        char[] pre = s.toCharArray();
        StringBuilder builder = new StringBuilder();
        char current = pre[0];
        int count = 1;
        for (int i = 1; i < pre.length; i++) {
            if (pre[i] == current){
                count++;
            }else {
                //数字变化，添加统计结果
                builder.append(count).append(current);
                //重置统计
                count = 1;current = pre[i];
            }
        }
        //不可遗漏
        builder.append(count).append(current);
        return builder.toString();
    }
    //递归写法
    public String countAndSayRecursively(int n){
        if (n == 1) return "1";
        String result = countAndSayRecursively(n-1);
        return countPre(result);
    }
    @Test
    public void test38() {
        System.out.println(countAndSayRecursively(5));//1112231214
    }

    /**
     * 58 最后一个单词的长度
     */
    public int lengthOfLastWord(String s) {
        if (s == null || s.length() == 0){
            return 0;
        }
        if (s.charAt(s.length()-1) == ' '){
            return 0;
        }
        return s.length()-1 - s.lastIndexOf(' ');
    }
    @Test
    public void test58() {
        String input = "Hello World";
        String input2 = " ";
        String input3 = "a ";
        System.out.println(lengthOfLastWord(input3));
    }

    /**
     * 67 二进制加法
     * Integer.toBinaryString(Integer.parseInt(a, 2) + Integer.parseInt(b, 2));
     */
    //官解 逐位计算 OC=O(max(M,N)) SC=O(max(M,N))
    public String addBinaryRecursively(String a,String b){
        int al = a.length();
        int bl = b.length();
        //两个数字不一样长时，代码逻辑里要确定哪一个更长，这里主动将 a b 替换，较长的数作为a
        if (al < bl) return addBinaryRecursively(b,a);
        char[] achars = a.toCharArray();
        char[] bchars = b.toCharArray();
        StringBuilder builder = new StringBuilder();
        int carry = 0;
        int L = Math.max(al,bl);
        for (int i = L-1; i > -1; i--) {
            if (a.charAt(i) == '1') carry++;
            if (bl+i>L-1 && b.charAt(bl+i-L) == '1') carry++;
            //判断两个bit位加法计算结果的方法是：直接对1计数，再对2取模
            builder.append(carry%2);
            carry = carry/2;
        }
        if (carry==1) builder.append(1);
        builder.reverse();//使用prepend
        return builder.toString();
    }
    //这个思路清晰点
    public String addBinary4(String a, String b) {
        StringBuilder ans = new StringBuilder();
        int pa = a.length()-1;
        int pb = b.length()-1;
        int carry = 0;
        while (pa >=0 || pb >= 0) {
            int va = pa<0 ? 0 : a.charAt(pa)-'0';
            int vb = pb<0 ? 0 : b.charAt(pb)-'0';
            int sum = (va + vb + carry)%2;
            ans.append(sum);
            carry = (va + vb + carry)/2;
            pa--;
            pb--;
        }
        ans.append(carry==0 ? "" : carry);
        return  ans.reverse().toString();
    }
    /**
        解法【巧】 - 使用位操作实现加法
     XOR操作本身就带有加法的效果，只不过忽略了进位
     a - 11011 , b - 1010
     a` = a XOR b - 10001
     b` = (a & b)<< 1 - 10100
    开始循环
     a = a` XOR b` - 00101
     b = (a` & b`)<<1 - 100000
    循环结束的条件是进位变成0
     a` - 100101
     b` - 000000 <-- 进位变成了0，终止循环

     把 aa 和 bb 转换成整型数字 xx 和 yy，xx 保存结果，yy 保存进位。
     当进位不为 0：y != 0：
     计算当前 xx 和 yy 的无进位相加结果：answer = x^y。
     计算当前 xx 和 yy 的进位：carry = (x & y) << 1。
     完成本次循环，更新 x = answer，y = carry。
     返回 xx 的二进制形式。

     OC=O(M+N), SC=O(max(N,M))
     */
    //下述算法题也使用了这一思路
    // 只出现一次的数字 III，
    // 数组中两个数的最大异或值，
    // 重复的DNA序列，
    // 最大单词长度乘积，
    public String addBinary3(String a, String b) {
        BigInteger x = new BigInteger(a, 2);
        BigInteger y = new BigInteger(b, 2);
        BigInteger zero = new BigInteger("0", 2);
        BigInteger carry, answer;
        while (y.compareTo(zero) != 0) {
            answer = x.xor(y);
            carry = x.and(y).shiftLeft(1);
            x = answer;
            y = carry;
        }
        return x.toString(2);
    }

    @Test
    public void test67() {
        String a = "11",b="1";
        String a1 = "1000",b1="11";
        String a2 = "1010",b2="11011";
        System.out.println(addBinary3(b2,a2));
        // Integer.toBinaryString(Integer.parseInt(a, 2) + Integer.parseInt(b, 2));
    }

    /**
     * #125 验证回文串
     */
    public boolean isPalindrome(String s) {
        if (s == null || s.length() == 0){
            return true;
        }
        String s1 = s.replaceAll(" ", "").toLowerCase();
        int length = s1.length();
        for (int i = 0; i < length/2; i++) {
            if (s1.charAt(i) != s1.charAt(length-1-i))
                return false;
        }
        return true;
    }
    static class HeadTailPointer{
        private static boolean isNumOrChar(char c){
            if ((Character.toLowerCase(c)>='a' && Character.toLowerCase(c)<='z')
                    || (Character.toLowerCase(c)>='0' && Character.toLowerCase(c)<='9')){
                return true;
            }
            return false;
        }
        public static boolean isPalindrome(String str){
            int length = str.length();
            if (length == 0) return true;
            int low = 0, high = length - 1;
            while (!isNumOrChar(str.charAt(low)) && low < length-1) low++;
            while (!isNumOrChar(str.charAt(high)) && high > 0) high--;
            while (low < high){
                while (!isNumOrChar(str.charAt(low)) && low < length-1) low++;
                while (!isNumOrChar(str.charAt(high)) && high > 0) high--;
                if (Character.toLowerCase(str.charAt(low)) != Character.toLowerCase(str.charAt(high))){
                    return false;
                }
                low++;high--;
            }
            return true;
        }
    }

    @Test
    public void test125() {
        String a = "A man, a plan, a   canal: Panama";//true
        String b = "race a car";//false
        String c = ".,";
        String d = ",.a";
        String e = ",4.";
        System.out.println(HeadTailPointer.isPalindrome(e));
    }

    /**
     * 回文数
     */
    //MINE - 接上面回文字符串双指针判断的方法，这里对数字也采取双指针取值，性能34 内存5
    public boolean isPalindrome(int x) {
        if (x < 0) return false;
        int bit = (int)Math.log10(x);
        for (int i = 0; i < bit/2+bit%2; i++) {
            int high = (x / (int)Math.pow(10, bit-i))%10;
            int end = (x/(int)Math.pow(10,i)) % 10;
            if (high != end) return false;
        }
        return true;
    }
    //上述的 数学计算很低效
    //官解1 整数反转判断是否相等，但整体反转会有Integer溢出问题，所以翻转一半
    //TC = O(lgn) SC = O(1)
    public boolean isPalindrome2(int x){
        //先排除负数和末位数字是0的整数
        if (x<0) return false;
        if (x%10==0&&x!=0) return false;
        int revertNumber = 0;
        //反转一半的临界点 是 反转数>=前半部截短的数
        while (x > revertNumber){
            //原始数字截到末位数字不断 *10+ 达到反转的效果
            revertNumber = revertNumber*10+x%10;
            x/=10;
        }
        return x == revertNumber || x == revertNumber/10;
    }
    @Test
    public void leco9() {
        System.out.println(isPalindrome(1));//10250-4
        // System.out.println((10^1));
    }

    /**
     * #345 反转字符串中的元音字母
     * 没有官解，不知道怎么提升性能。。。
     */
    public String reverseVowels(String s) {
        char[] chars = s.toCharArray();
        Set<Character> vowelSet = new HashSet<Character>(){{
            add('a');add('i');add('o');
            add('e');add('u');add('A');add('I');add('O');
            add('E');add('U');
        }};
        int i = 0,j=chars.length-1;
        while (i<j){
            while (i<j && vowelSet.contains(chars[i]) && !vowelSet.contains(chars[j])){
                j--;
            }
            while (i<j && !vowelSet.contains(chars[i]) && vowelSet.contains(chars[j])){
                i++;
            }
            if (i<j && vowelSet.contains(chars[i]) && vowelSet.contains(chars[j])){
                char temp = chars[i];
                chars[i]=chars[j];
                chars[j]=temp;
            }
            i++;j--;
        }
        return String.copyValueOf(chars);
    }
    //这个性能好很多
    public String reverseVowels2(String s){
        if(s.length()<2) return s;
        char[]chars = s.toCharArray();
        int i= 0; int j = chars.length -1 ;
        while(i<j){
            if(chars[i]=='a'||chars[i]=='e'||chars[i]=='o'||chars[i]=='i'||chars[i]=='u'||chars[i]=='A'||chars[i]=='E'||chars[i]=='O'||chars[i]=='I'||chars[i]=='U'){
                if(chars[j]=='a'||chars[j]=='e'||chars[j]=='o'||chars[j]=='i'||chars[j]=='u'||chars[j]=='A'||chars[j]=='E'||chars[j]=='O'||chars[j]=='I'||chars[j]=='U'){
                    if(chars[i]!=chars[j]){
                        char temp = chars[i];
                        chars[i] = chars[j];
                        chars[j] = temp;
                    }
                    ++i;
                    --j;
                }else{
                    --j;
                }
            }else{
                ++i;
            }
        }
        return new String(chars);
    }
    @Test
    public void leco345() {
        String input = "hello";
        String input2 = "leetcode";
        String input3 = "shfgug";
        String input4 = "suhgfg";
        String input5 = "aA";
        System.out.println(reverseVowels(input5));
    }


}
