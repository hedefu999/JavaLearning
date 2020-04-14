package leetcode;

import org.junit.Test;

import javax.validation.constraints.Min;
import java.util.*;
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
     * 最小栈问题
     */
    //用两个栈处理
    static class MinStack{
        Stack<Integer> data = null;
        Stack<Integer> assist = null;
        public MinStack(){
            data = new Stack<>();
            assist = new Stack<>();
        }
        public void push(Integer x){
            if (assist.isEmpty()){
                assist.push(x);
            }else {
                Integer peek = assist.peek();
                if (x<=peek){
                    assist.push(x);
                }
            }
            data.push(x);
        }
        public Integer pop(){
            Integer pop = data.pop();
            if (pop == assist.peek()){
                assist.pop();
            }
            return pop;
        }
        public Integer top(){
            return data.peek();
        }
        public Integer getMin(){
            if (assist.isEmpty()){
                return 0;
            }else {
                return assist.peek();
            }

        }
    }
    //方案2 标准解法
    static class Node{
        private int value;
        private int min;
        private Node next;

        public Node(int value, int min, Node next) {
            this.value = value;
            this.min = min;
            this.next = next;
        }
    }
    static class MinStack2 {
        private Node head;
        /** initialize your data structure here. */
        public MinStack2() {

        }
        public void push(int x) {
            if (head == null){
                head = new Node(x,x,null);
            }else {
                head = new Node(x,Math.min(x,head.min),head);
            }
        }
        public void pop() {
            head = head.next;
        }
        public int top() {
            return head.value;
        }
        public int getMin() {
            return head.min;
        }
    }
    //方案3:-2 0 -3 依次进栈后 -2 -2 0 -3 -3,getMin是一次pop，pop是两次pop
    @Test
    public void test155(){
        MinStack2 minStack = new MinStack2();
        minStack.push(512);
        minStack.push(-1024);
        minStack.push(-1024);
        minStack.push(512);
        minStack.pop();
        System.out.println(minStack.getMin());
        minStack.pop();
        System.out.println(minStack.getMin());
        minStack.pop();
        System.out.println(minStack.getMin());
        minStack.pop();
        minStack.pop();
    }

    /**
     * 使用单向对列实现栈（只允许使用peek pop push2Back size isEmpty）
     *
     * LinkList（双向队列） apis:
     * pop、poll、pollFirst 出头结点,pollLast出尾结点
     * peek、peekFirst取头结点，peekLast取尾结点
     * offer、offerLast放入尾部，offerFirst 放入头部,带返回结果boolean
     * add、addLast、linkLast 放入尾部，addFirst、push、linkFirst 放入头部
     * remove(Index)移除第Index个元素（从0计数）removeFirst移除头部,removeLast移除尾部，都返回移除的元素
     *
     * 方案1：pop比push复杂
     *
     */
    static class StackByLinkedList1{
        private LinkedList<Integer> q1 = new LinkedList<>();
        private LinkedList<Integer> q2 = new LinkedList<>();
        public void push(int x){
            q1.add(x);
        }
        public int pop(){
            while (q1.size()>1){
                Integer pop = q1.pop();
                q2.add(pop);
            }
            Integer result = q1.pop();
            LinkedList<Integer> tmp = q1;
            q1 = q2;
            q2 = tmp;
            return result;
        }
        public int top(){
            while (q1.size()>1){
                Integer pop = q1.pop();
                q2.add(pop);
            }
            Integer result = q1.pop();
            q2.add(result);
            LinkedList<Integer> tmp = q1;
            q1 = q2;
            q2 = tmp;
            return result;
        }
        public boolean empty(){
            return q1.isEmpty();
        }
    }

    /**
     * 方案2：在push的时候就转成真正的stack
     */
    static class StackByLinkedList2{
        private LinkedList<Integer> q1 = new LinkedList<>();
        private LinkedList<Integer> q2 = new LinkedList<>();
        public void push(int x){
            q2.add(x);
            while (!q1.isEmpty()){
                q2.add(q1.pop());
            }
            LinkedList<Integer> tmp = q1;
            q1 = q2;
            q2 = tmp;
        }
        public int pop(){
            return q1.pop();
        }
        public int top(){
            return q1.peek();
        }
        public boolean empty(){
            return q1.isEmpty();
        }
    }

    /**
     * 方案3：用一个队列实现stack
     */
    static class StackByLinkedList3{
        LinkedList<Integer> q = new LinkedList<>();
        public void push(int x){
            q.add(x);
            int size = q.size();
            for (int i = 0; i < size; i++) {
                q.add(q.pop());
            }
        }
        public int pop(){
            return q.pop();
        }
        public int top(){
            return q.peek();
        }
        public boolean empty(){
            return q.isEmpty();
        }
    }
    @Test
    public void test255(){
        StackByLinkedList3 stack = new StackByLinkedList3();
        stack.push(1);
        stack.push(4);
        System.out.println(stack.top());//4
        stack.push(3);
        System.out.println(stack.pop());//3
        stack.push(7);
        System.out.println(stack.top());//1 4 7,7
        System.out.println(stack.pop());//14,7
        System.out.println(stack.top());//4
        System.out.println(stack.empty());//false
        System.out.println(stack.pop());//4
        System.out.println(stack.top());//1
        System.out.println(stack.pop());//1
        System.out.println(stack.empty());//true
    }
































}
