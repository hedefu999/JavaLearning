package leetcode;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

public class StackTagSimple {
    /**
     * #1021 括号原语，去除最外层括号
     */
    //11ms 40.3MB
    public String getPrimativeStr(String S){
        //关于长度的获取：集合叫size() 数组的是length String的是length()
        char[] chars = S.toCharArray();
        int flag = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '('){
                flag++;
                if (flag == 1) chars[i]=' ';
            }
            if (chars[i] == ')'){
                flag--;
                if (flag == 0) chars[i] = ' ';
            }
        }
        return String.valueOf(chars).replaceAll(" ", "");
    }
    //3ms 39.6MB
    public String getPrimativeStrUpgarade(String s){
        char[] chars = s.toCharArray();
        int flag = 0;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '(' && ++flag != 1){// && flag++ > 0
                builder.append(chars[i]);
                continue;
            }
            if (chars[i] == ')' && --flag != 0){// && --flag > 0
                builder.append(chars[i]);
            }
        }
        return builder.toString();
    }
    //8ms 39.4MB
    public String getPrimativeStrByStack(String s){
        //使用堆栈处理括号原语问题
        char[] chars = s.toCharArray();
        Stack<Character> stack = new Stack<>();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == ')'){
                stack.pop();
            }
            if (!stack.isEmpty()){
                builder.append(chars[i]);
            }
            if (chars[i] == '('){
                stack.push('(');
            }
        }
        return builder.toString();
    }

    @Test
    public void test1021(){
        String s1 = "(()())(())";
        String s2 = "(()())(())(()(()))";
        String s3 = "()()";
        String s4 = "";
        System.out.println(getPrimativeStrByStack(s1));
        System.out.println(getPrimativeStrByStack(s2));
        System.out.println(getPrimativeStrByStack(s3));
    }
    /**
     * #1047 删除相邻重复字符，类似寿司消除
     */
    public String removeSameConjection(String s){
        char[] chars = s.toCharArray();
        Stack<Character> stack = new Stack<>();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            if (!stack.isEmpty() && stack.peek().equals(chars[i])){
                stack.pop();
            }else {
                stack.push(chars[i]);
            }
        }
        Stack<Character> stack2 = new Stack<>();
        //需要再new一个stack倒腾一番。。。
        return "";
    }
    public String removeSameChar(String s){
        char[] charSrc = s.toCharArray();
        char[] charDst = new char[charSrc.length];
        int dstIndex = 0;
        for (int i = 0; i < charSrc.length; i++) {
            if (dstIndex != 0 && charDst[dstIndex-1] == charSrc[i]){
                dstIndex--;//char转成字符串时赋值0也会打印出来，赋值32是空格，所以要彻底不显示就需要截取
            }else {
                charDst[dstIndex++] = charSrc[i];
            }
        }
        return String.valueOf(charDst).substring(0,dstIndex);
    }
    @Test
    public void test79(){
        String input = "abbaca";
        System.out.println(removeSameChar(input));
    }
    /**
     * #20 有效的括号
     * isValidBrankets 使用堆栈进行处理
     */
    public boolean isValidBrankets(String s){
        HashMap<Character,Character> branketPairs = new HashMap<>();
        branketPairs.put('(',')');
        branketPairs.put('[',']');
        branketPairs.put('{','}');
        char[] chars = s.toCharArray();
        Stack<Character> stack = new Stack<>();
        for (char c : chars){
            if (!stack.empty()){
                Character peek = stack.peek();
                if (branketPairs.containsKey(c)){
                    stack.push(c);
                }else if (branketPairs.get(peek).equals(c)){
                    stack.pop();
                }else {
                    return false;
                }
            }else if (branketPairs.containsKey(c)){
                stack.push(c);
            }else {
                return false;
            }
        }
        if (stack.empty()){
            return true;
        }else {
            return false;
        }
    }
    //这种方式代码简单，性能很差，内存占用也高，不推荐
    public boolean isValidBranket2(String s){
        while (s.contains("{}")||s.contains("()")||s.contains("[]")){
            s = s.replaceAll("\\{\\}","");
            s = s.replaceAll("\\[\\]","");
            s = s.replaceAll("\\(\\)","");
        }
        if (s.length() != 0)
            return false;
        else
            return true;
    }

    @Test
    public void test20(){
        String[] testCases = {"()","()[]{}","(]","","([)]","{[]}","{[]",")}{({))[{{[}"};
        for (String str : testCases){
            System.out.print(isValidBrankets(str));
        }
        /**
         * truetruefalsetruefalsetruefalsefalse
         * truetruefalsetruefalsetruefalsefalse
         */
    }
    /**
     *
     */
    @Test
    public void test170(){


        String s = "(){}[]";
        s = s.replaceAll("\\(\\)", "--");
        s = s.replaceAll("\\{\\}","--");
        s = s.replaceAll("\\[\\]","--");
        System.out.println(s);

        //boolean matches = s.matches("()");
        //System.out.println(matches);
    }































}
