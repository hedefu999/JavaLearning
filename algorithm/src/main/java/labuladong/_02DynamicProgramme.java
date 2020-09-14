package labuladong;

public class _02DynamicProgramme {
    /*-=-=-=-=-=-=-=-=- 系列背包问题 -=-=-=-=-*/
    /**
     # 0-1背包问题
     一个可装载重量为W的背包和N个物品，每个物品有重量和价值两个属性。
     其中第i个物品的重量 wt[i]，价值为val[i]，现在让你用这个背包装物品，最多能装的价值是多少？
     0-1的含义就是物品不可分割，要么放进背包，要么不放
     */
    /*
    ## 分析
    一维动态规划就是：第i个物品的可放入的总价值，在没有重量限制的情况下就是前一个i-1判断结果加上当前
    二维动态规划就是：第i个物品要放，看第i-1个物品在总容量为减去当前物品后余量的情况下的最大价值，加上当前价值。
                    第i个物品不放，取第i-1个物品的最大价值
                    比较上述两种情况，作为当前最大价值

     */
    static class ZeroOnePackageProblem{
        /*
        DP分析
        「状态」有两个：背包容量、可选择物品，所以dp数组是一个二维数组。
        「选择」有两个：装进背包和不装
            当前物品装入的价值是 这个物品的价值+剩余容量是背包最大容量时的最大价值
            当前物品不装的价值就是上一个物品同等最大容量的价值
            当前物品的最大价值就是上面两个的max比较

            dp[i][j]定义：对当前第i个物品，背包总容量为j，可以装入的最大价值。
         「base case」：dp[0][...] = dp[...][0] = 0
         「STF」
         将第i个物品装入背包：dp[i][j] = dp[i-1][j-wt[i-1]] + val[i-1]
         第i个物品不放入背包：dp[i][j] = dp[i-1][j]
         dp[i][j] = max(..上面两个..)
         这样先写出dpTable
           0 1 2 3 ←第i个物品
         0 0 0 0 0
         1 0 0 2 2 二维数组表示在试验到第i个物品，总容量是j的情况下的最大容量
         2 0 4 4 4
         3 0 4 6 6
         4 0 4 6 6
         ↑背包总容量
         穷举不是穷举2^N种放置物品可能性，而是将剩余重量也作为状态进行规划
        * */
        static int packageLoadDPSolution(int W, int[] wt, int[] val){
            int[][] dp = new int[wt.length][W+1];
            for (int i = 1; i < wt.length; i++) {
                for (int j = 1; j <= W; j++) {
                    int preVolume = j - wt[i];
                    if (preVolume < 0){//不能放入此物品
                        dp[i][j] = dp[i-1][j];
                    }else {//可以放入此物品
                        dp[i][j] = Math.max(dp[i-1][preVolume]+val[i], dp[i-1][j]);
                    }
                    //dp[i][j] = Math.max(dp[i-1][preVolume<0?0:preVolume] + val[i], dp[i-1][j]);
                }
            }
            return dp[wt.length-1][W];
        }
        /*
        其他思路
        2^N种情况的穷举：填充数组，取出最大值
        * */
        static int maxWgtPackageLoad(int[] wt, int[] val){
            int size = 2 ^ wt.length;
            int[] valsums = new int[size];
            /*  以3个物品为例，要进行8轮计算，复杂度都是2^N,使用二进制位标记是否放入包中，难以计算重量
                0 000 000-0
              0
            0   1 001 100-1
                0 010 010-2
              1
                1 011 110-3
                0 100 001-4:
              0
                1 101 101-5
            1
                0 110 011-6
              1
                1 111 111-7
            * */
            valsums[0]=0;
            for (int i = 1; i < valsums.length; i++) {
                //过于复杂
            }
            return 0;
        }

        public static void main(String[] args) {
            int N = 3,W = 4;
            int[] wt = {2,1,3};
            int[] val = {4,2,3};
            //dp[i] 第i个物品
            System.out.println(packageLoadDPSolution(W,wt,val));
        }
    }
}
