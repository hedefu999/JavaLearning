package algorithm;

import org.junit.Test;

import java.util.Arrays;

public class _01DynamicProgramming {
    /**
     * 最优子结构问题
     给你 `k` 种面值的硬币，面值分别为 `c1, c2 ... ck`，每种硬币的数量无限，再给一个总金额 `amount`，问你**最少**需要几枚硬币凑出这个金额，如果不可能凑出，算法返回 -1 。
     dp(n)
     */
    public int getMinCoinsCount(int[] coins, int total){
        int[] memo = new int[total+1];
        for (int i = 0; i < memo.length; i++) {
            memo[i] = total+1;//初始化为最大值，如果最终还是这个最大值，表明不能使用这些硬币拼出total
            //Math.min(Integer.MAX_VALUE+1, Integer.MAX_VALUE);使用过Integer.MAX 会出问题，+1时溢出成了负值
        }
        memo[0]=0;//需要初始化total为0的情况
        for (int sum = 0;sum <= total; sum++){
            for (int coinvalue : coins){
                //一个硬币就超出需要的总和，这个不能入选
                if (coinvalue > sum){
                    //memo[sum] = 0; 在这里错误
                    continue;
                }
                //2.状态转移方程 memo[sum] = Math.min(memo[sum - coinvalue]+1,memo[sum]);
                memo[sum] = Math.min(memo[sum - coinvalue]+1,memo[sum]);
            }
        }
        System.out.println(Arrays.toString(memo));
        return memo[total];
    }
    @Test
    public void test13(){
        int[] coins = {3,5};//k=coins.length
        int total = 11;
        int count = getMinCoinsCount(coins, total);
        System.out.println(count);
    }
}
