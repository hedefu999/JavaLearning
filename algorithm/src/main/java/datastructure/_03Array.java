package datastructure;

import org.junit.Test;

import java.util.Arrays;

public class _03Array {
    /**
     * 由斐波那契数列引出的动态规划知识
     * 1 1 2 3 5 8 13 21 34 55
     */
    //常规写法：时间复杂度O(n),空间复杂度O(1)
    public void printFibonaci(int length){
        int first = 1;
        int second = 1;
        System.out.print(1+" ");
        System.out.print(1+" ");
        while (length-2 > 0){
            int temp = second;
            second = second+first;
            first = temp;
            System.out.print(second);
            System.out.print(" ");
            length--;
        }
    }
    //计算方程是 F(N) = F(N-1)+F(N-2)
    public int calcFibo(int index){ //缺少非负检查
        if (index == 1 || index == 2)
            return 1;
        int fibo = calcFibo(index-1)+calcFibo(index-2);
        return fibo;
    }
    @Test
    public void test19(){
        //printFibonaci(10);
        for (int i = 1; i < 10; i++) {
            System.out.print(calcFibo(i)+" ");
        }
    }
    //改进方案一 使用备忘录的递归求斐波那切数列的算法复杂度是O(n)
    public int printFibberWithMemo(int N){
        if (N < 1) return 0;
        int[] memo = new int[N+1];//因为取备忘录时是直接memo[n]的，所以这里容量至少为N+1个
        return recursiveCalculator(memo,N);
    }
    public int recursiveCalculator(int[] memo, int n){
        if (n==1||n==2) return 1;
        if (memo[n] == 0){
            memo[n] = recursiveCalculator(memo,n-1)+ recursiveCalculator(memo, n-2);
        }
        return memo[n];
    }
    @Test
    public void test51(){
        for (int i = 0; i < 10; i++) {
            System.out.print(printFibberWithMemo(i)+" ");
        }
    }
    //改进方案二：使用dp数据进行迭代 -- 正统的动态规划
    public int dynamicProgrammingFibber(int N){
        int[] memo = new int[N+1];
        memo[1] = memo[2] = 1;//memo[0]用不到
        for (int i = 3;i<=N;i++){
            memo[i] = memo[i-1]+memo[i-2];
        }
        System.out.println(Arrays.toString(memo));
        return memo[N];
    }
    @Test
    public void test70(){
        dynamicProgrammingFibber(10);
    }


    /**
     * 比较两个排好序的int数组中是否有相同的数字
     * 推广至任意数组
     */
    public void findAllSameNumber(int[] a, int[] b){
        int aindex = 0,bindex=0;
        while (aindex < a.length && bindex < b.length){
            int diff = a[aindex] - b[bindex];
            if (diff == 0){
                System.out.println("找到相同数字："+a[aindex]);
                bindex++;aindex++;
            }else if (diff > 0){
                bindex++;
            }else{
                aindex++;
            }
        }
    }
    @Test
    public void test24(){
        int[] inta = {1,2,4,6,7,8};
        int[] intb = {5,7,8,9,12,16};
        findAllSameNumber(inta,intb);
    }
    /**
     可能含有负数的数组的最大子序列问题
     ai-aj 来自 a0 - an, ai+...+aj最大
     整数序列-2, 11, -4, 13, -5, 2, -5, -3, 12, -9的最大子序列的和为21
     */
    //Sum(i,j+1) = Sum(i,j)+A[j+1]
    //使用两层循环试验所有的子序列，记录最大值到max
    public void calculateMaxSubArrays(int[] a){
        int i,j,v,max =a[0];
        for (i = 0;i<a.length;i++){
            v=0;
            for (j=i;j<a.length;j++){
                v = v+a[j];
                if (v>max){
                    max = v;
                }
            }
        }
        System.out.println("最大字序列和是："+max);
    }
    //使用动态规划找这个最大值
    public void findMxByDP(int[] a){
        int i,max = 0,temp_sum = 0;
        for (i=0;i<a.length;i++){
            temp_sum+=a[i];
            if (temp_sum > max){
                max = temp_sum;
            }else if(temp_sum < 0) {
                temp_sum = 0;
            }
        }
        System.out.println("动态规划得到的最大子序列和："+max);
    }
    @Test
    public void test39(){
        int[] a = {-2, 11, -4, 13, -5, 2, -5, -3, 12, -9};
        //calculateMaxSubArrays(a);
        findMxByDP(a);
    }
    /**
     * 默写二分查找
     * arr递增排序
     */
    public int getIndexFromArray(int[] arr, int number){
        int size = arr.length;
        int left = 0;
        int right = size - 1;
        int middle = (left + right)/2;
        while (left <= right){
            if (number < arr[middle]){
                right = middle - 1;
                middle = (left+right)/2;
            }else if (number > arr[middle]){
                left = middle + 1;
                middle = (left + right)/2;
            }else {
                return middle;
            }
        }
        return -1;
    }
    //递减序列
    public int getIndexFromDecreasingArray(int[] arr, int number){
        int left = 0;
        int right = arr.length - 1;
        int middle = 0;
        while (left <= right){
            middle = (left + right)/2;
            if (number == arr[middle]){
                return middle;
            }else if (number < arr[middle]){
                left = middle + 1;
            }else {
                right = middle - 1;
            }
        }
        return -1;
    }

    @Test
    public void test162(){
        int[] arr = {1,3,5,6,12,13,16,23,25,32};
        int[] arr2 = {32,25,23,16,13,12,6,5,3,1};
        //System.out.println(getIndexFromArray(arr,6));
        System.out.println(getIndexFromDecreasingArray(arr2,100));
    }

    /**
     * 合并两个递增数组到第三个递增数组
     */
    public static int[] mergeArray(int[] a, int[] b){
        int[] c = new int[a.length+b.length];
        int i = 0, j = 0, t = 0;
        while (i<a.length && j<b.length){
            if (a[i] <= b[j]){
                c[t++] = a[i++];
            }else {
                c[t++] = b[j++];
            }
        }
        while (i < a.length){
            c[t++] = a[i++];
        }
        while (j < b.length){
            c[t++] = b[j++];
        }
        return c;
    }
    public static void main(String[] args) {
        int[] a = {1,2,6,7};
        int[] b = {3,4,5,8,9};
        int[] c = mergeArray(a,b);
        System.out.println(Arrays.toString(c));
    }

}
