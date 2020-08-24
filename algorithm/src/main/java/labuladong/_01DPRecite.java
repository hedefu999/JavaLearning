package labuladong;

import org.junit.Test;

/**
 * 日常测试
 * 0822 默写fibnacci数列的递归、备忘录解法
 *      硬币问题的递归、带备忘录的递归和动态规划解法
 *      最长递增子序列的
 */
public class _01DPRecite {
    //fibonacci

    /**
     * index      1 2 3 4 5 6  7
     * fibonacci  1 1 2 3 5 8 13
     */
    //递归写法
    int fibnacci1(int index){
        if (index == 1 || index == 2){
            return 1;
        }
        return fibnacci1(index - 1)+fibnacci1(index - 2);
    }
    //递归时使用备忘录
    int fibnacci3(int index){
        int[] memo = new int[index+1];
        memo[1] = 1;
        memo[2] = 1;
        return helper(memo, index);
    }
    int helper(int[] memo, int index){
        if (memo[index] != 0) return memo[index];
        memo[index] = helper(memo, index-1) + helper(memo, index-2);
        return memo[index];
    }
    //动态规划时使用备忘录
    int fibnacci2(int index){
        int pre = 1, ppre = 1, output = 1;
        for (int i = 1; i <= index; i++) {
            if (i == 1 || i == 2){
                continue;
            }
            int tmp = output;
            output = output + pre;
            pre = tmp;
        }
        return output;
    }
    @Test
    public void test5(){
        System.out.println(fibnacci3(7));
    }

    /**
     默写凑零钱算法
     3.标准动态规划解法：动态规划的几个步骤：确定变化的state 硬币数量 dp[sum] = coun; 确定base case,dp[0]=0,dp[1]=1; 确定状态转移方程 dp[sum] = dp[sum - coin] + 1;
     1.自顶向下的普通递归解法
     2.自顶向下的带备忘录的递归解法
     */
    int coins1(int[] coins, int sum){
        int[] dp = new int[sum+1];
        for (int i = 1; i <= sum; i++) {
            int min = Integer.MAX_VALUE;
            for (int j = 0; j < coins.length; j++) {
                int pre = i - coins[j];
                if (pre >= 0 && dp[pre] != -1){
                    int curr = dp[pre] + 1;
                    min = min < curr?min:curr;
                }
            }
            dp[i] = min == Integer.MAX_VALUE? -1 :min;
        }
        return dp[sum];
    }
    int coins2(int[] coins, int amount){
        if (amount < 0) return -1;
        if (amount == 0) return 0;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < coins.length; i++) {
            int curr = coins2(coins, amount - coins[i]);
            if (min > curr && curr!= -1) min = curr;
        }
        return min == Integer.MAX_VALUE?-1:min+1;
    }
    int coins3(int[] coins, int amount){
        int[] memo = new int[amount+1];
        helper(memo,coins, amount);
        return memo[amount];
    }
    int helper(int[] memo, int[] coins, int amount){
        if (amount < 0) return -1;
        if (amount == 0) return 0;
        if (memo[amount] != 0) return memo[amount];
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < coins.length; i++) {
            int curr = helper(memo, coins, amount - coins[i]);
            if (curr < min && curr != -1){
                min = curr;
            }
        }
        min = (min == Integer.MAX_VALUE)?-1:min+1;
        memo[amount] = min;
        return min;
    }
    @Test
    public void test53(){
        int[] coins = {2,5};//k=coins.length
        int amount = 12;
        System.out.println(coins3(coins,amount));
    }

    /**
     默写 最长递增子序列（LIS） 的解法
        1. 动态规划解法：changing state：长度;base case:0号元素结尾的是1；1号元素结尾的是..;state transfer formula:dp[i] = dp[preLow]+1;
        2. 纸牌堆叠：可分堆，小牌压大牌（可任意建堆的汉诺塔游戏）
     */
    int maxlengthOfLIS(int[] nums){
        int[] dpTable = new int[nums.length];
        dpTable[0] = 1;
        for (int i = 1; i < nums.length; i++) {
            int j = i;
            for (; j >= 0; j--) {
                if (nums[j] < nums[i]){//不可<=
                    dpTable[i] = dpTable[j] + 1;
                    break;
                }
            }
            if (j < 0){
                dpTable[i] = 1;
            }
        }
        return 0;
    }

    /**
     纸牌解法(假设都是正数)
     从左到右放纸牌，小的才可以叠上去，否则要另起一堆。堆的数量才是递增字序列的最大长度
     10 5 7 101
     9 3    18
     2

     如果从小到大堆叠，会出来一个毫无用处的堆。。。
     10   9  2  5
     101 18  3
             7

        7
        5  18  101
     3  2   9   10   好像这样写可以求最大递减子序列的长度

     */
    int maxLengthOfLISByMultiStack(int[] nums){
        int[] stack = new int[nums.length];
        int stackSize = 0;
        for (int i = 0; i < nums.length; i++) {
            int curr = nums[i];
            int left = 0, right = stackSize;
            while (left < right){
                int middle = (left+right)/2;
                if (curr < stack[middle]){
                    right = middle;//比middle上的元素小可能最后还要放在middle上所以不用减1
                }else {
                    left = middle+1;
                }
            }
            if (curr > stack[right] && stack[right] != 0){
                stack[right+1]= curr;
                stackSize++;
            }else {
                stack[right] = curr;
            }
        }
        return stackSize+1;
    }

    @Test
    public void test118() {
        int[] nums = {10,9,2,5,3,7,101,18};
        System.out.println(maxLengthOfLISByMultiStack(nums));
    }


    /**
     默写 股票问题I II III 的递归与动态规划解法
     */



}
