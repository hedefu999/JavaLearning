package labuladong;

import org.junit.Assert;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
        第i个物品放如就是总价值增加第i个物品的价值，不放就是总价值保持不变
        那如何确定这第i个物品放入后不会超出容量呢？答案是增加「第i个物品」之外的一个容量维度
        取总价值的最大值，这样构成了一个二维数组

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

    /**
     # 416 分割等和子集   子集背包问题
     - 二维动态规划如何压缩成一维的

     一个只包含正整数的非空数组，是否可分割成 两 个子集，使得两个子集的元素和相等
     限制：每个数组的元素个数 <= 100; 数组大小 <= 200
     测试用例：[1 5 11 5] true; [1 2 3 5]
     */
    static class CanPartitionProblemAdvanced{
        /*
        ERROR:排好序一分为二，左右的和是否相等
        计算总和获得half，按背包思想组合出half
        * */
        static boolean canParitition(int[] nums){
            int sum = 0;
            for (int i = 0; i < nums.length; i++) {
                sum += nums[i];
            }
            if (sum%2==1) return false;
            int half = sum/2;
            //下面就变成背包问题了
            return canSumToByPackageProblem(half,nums);
        }
        /*
        博客解法
        对于dp数组 boolean dp[i][j]: 对于前i个物品，当前背包的容量为j，若能恰好装满，则dp[i][j]=true;
        x表示物品index，y表示背包容量
        * */
        static boolean canSumToByPackageProblem(int half, int[] nums){
            boolean[][] dp = new boolean[nums.length+1][half+1];
            //背包容量为0能否组成，由于要用来判断当前容量-物品质量是否恰好为0，所以被置为true。dp[0][...]采用默认的false，dp[...][0]置为true
            for (int i = 0; i <= nums.length; i++) {
                dp[i][0] = true;
            }
            //外层对x-物品index遍历还是对容量进行遍历都是可以的(AB行可以对调顺序)
            for (int i = 1; i <= nums.length; i++) { //A
            for (int j = 1; j <= half; j++) { //B
                    if (j - nums[i-1] < 0){
                        //放不下就不放，看前i-1件物品能否组成此背包容量（背包容量不变）
                        dp[i][j] = dp[i-1][j];
                    }else {
                        /*
                        背包容量大于当前物品质量，但不确定背包是否已经满了
                        所以要取前i-1个物品能否组成背包容量（这是防止物品重复放入的关键），再看放入当前物品，前i-1个物品能否组成剩余背包容量
                        * */
                        dp[i][j] = dp[i-1][j] || dp[i-1][j-nums[i-1]];
                        //dp[i-1][j]是防止物品重复放入的关键
                    }
                }
            }
            //打印出dp二维数组，横向是背包容量，纵向是物品index
            TestCasesAndUtils.printBoolMatrix(dp,6);
            return dp[nums.length][half];
        }
        /*
        可能dp矩阵声明的形式是转置的，或者容量、物品的遍历顺序也是不一样的，这些都不影响dp矩阵的计算和最终结果
        下面的解法与上面仅仅是dp矩阵是转置的
        */
        static boolean canSumToByPackageProblem2(int half, int[] nums){
            boolean[][] dp = new boolean[half+1][nums.length+1];
            //dp[0][...]采用默认的false，dp[...][0]置为true
            for (int i = 0; i <= nums.length; i++) {
                dp[0][i] = true;
            }
            for (int i = 1; i <= half; i++) {
                for (int j = 1; j <= nums.length; j++) {
                    if (i < nums[j-1]){//第j个物品无法放入,就取同样的容量，前j-1个物品能否组成容量
                        dp[i][j] = dp[i][j-1];
                    }else{
                        dp[i][j] = dp[i][j-1] || dp[i-nums[j-1]][j-1];
                    }
                }
            }
            TestCasesAndUtils.printBoolMatrix(dp,6);
            return dp[half][nums.length];
        }
        //自己的解法，无法解决nums被重用的问题
        static boolean canSumTo(int half, int[] nums){
            boolean[] dp = new boolean[half+1];
            dp[0] = true;
            for (int i = 1; i <= half; i++) {
                for (int j = 0; j < nums.length; j++) {
                    if (i == nums[j]){
                        dp[i] = true;
                        nums[j]=0;
                        break;
                    }
                    if (i-nums[j] < 0){
                        dp[i] = false;
                    }else {
                        dp[i] = dp[i-nums[j]];
                        nums[j]=0;
                        break;
                    }
                }
            }
            return dp[half];
        }

        //对dp数组进行降维，压缩状态，减少SC
        static boolean canSumTo3(int half, int[] nums){
            boolean[] dp = new boolean[half+1];
            dp[0] = true;
            //此时对于half的遍历必须放在nums的遍历内部
            //如果half的遍历放在外层，观察{1,5,11,5}在i=2,j=0的计算，显然数字1重复使用了 在第一个5进行half遍历时，10被判断为false，但5可以判断为true，第二个5进入时，10才会判断为true
            for (int j = 0; j < nums.length; j++) {
                //这里要倒过来遍历，而且还要放在物品遍历的里面，如果放在外面，在{1,5,11,5}的情况下，sum=10时由于sum=5还未处理到，会错误地计算为false
                //而仅仅放在内层，half正序遍历时，对于一个数字5，sum=5时判断可以组成，sum=10时因为要取sum=5的状态，计算出true，相当于把这个数字用了两次
                for (int i = half; i > 0; i--) {
                    if (i-nums[j]>=0){
                        //dp[i]一旦为true了,不管在来几个nums[j],就不修改了
                        //在{1,5,11,5}情况下，half=11在11时判断为true，在下一个5时要使用dp[i]进行或计算，防止被改掉
                        dp[i]=dp[i]||dp[i-nums[j]];
                    }
                    //dp[i] =i>0 && i>=nums[j] && dp[i-nums[j]];
                }
            }
            return dp[half];
            /**
             half正序遍历会导致物品重复放入
             half遍历放在外层会导致在倒序遍历时小容量的情况没有先判断
             所以half的遍历要放在物品遍历内层，并且要倒序遍历，否则算法出错

             上面方法是经典的判断一组整数能否在不重复使用的情况下凑成某个和的判断  LeCo494
             如果数字允许重复使用，就变成了硬币问题
             */
        }

        public static void main(String[] args) {
            int[] ns1 = {1,5,11,5};
            int[] ns2 = {1,2,3,5};
            int[] ns3 = {1,2,3,4,5,6,7};
            int[] ns4 = {1,5,11,3};
            Assert.assertTrue(canParitition(ns1));
            Assert.assertTrue(!canParitition(ns2));
            Assert.assertTrue(canParitition(ns3));
            Assert.assertTrue(!canParitition(ns4));
        }
    }

    /**
     518 零钱问题 2
     另一种典型背包问题的变体

     给定不同面额的硬币和一个总金额，可以重复使用硬币面额（每种面额的硬币有无数个）计算凑成某个总金额的组合数

     完全背包问题：容量为amount的背包装入一系列重量为coins的物品，每个物品数量不限（完全名头的由来），多少种方法可以将背包装满
     */
    static class ChangeProblem2{
        /*
         状态：可选择的物品、背包容量
         若只使用前i个物品，恰好装满背包容量j有dp[i][j]种方法
         base case: dp[0][...]=0; dp[...][0]=1;(表示恰好装进去，通常在缩写子问题时j=0表示上一步恰好用硬币凑成了/恰好将背包装满)
         结果：dp[coins.length][amount]
         */
        static int change(int amount, int[] coins) {
            int[][] dp = new int[amount+1][coins.length+1];
            //初始情况
            // dp[...][0]=0;(一种硬币都不使用，无法凑出任意正值) 1.这种初始情况无需显式初始化，2.由于这种情形要考虑，所以数组声明和遍历时都要额外+1
            // dp[0][...]=1;(使用所有的硬币凑出0，表明使用当前硬币恰好凑出了amount)
            for (int i = 0; i <= coins.length; i++) {
                dp[0][i] = 1;
            }
            //谁放在外层遍历不影响结果
            for (int i = 1; i <= amount; i++) {
                for (int j = 1; j <= coins.length; j++) {
                    if (i>=coins[j-1]){
                        dp[i][j] =
                                dp[i-coins[j-1]][j] //第j种（j从1开始）硬币使用，就取减去这种硬币一个面值后的amount在包括当前硬币的组合方案个数，注意coins的index(j)不需要减1，表示第j种硬币会使用多次
                                        + dp[i][j-1]; //第j种硬币不使用，此种硬币有和没有一样，取同样amount但硬币种类倒退1的值：dp[i][j-1]
                    }else {
                        dp[i][j] = dp[i][j-1];//与上面不使用第j种硬币的情况一致
                    }
                }
            }
            return dp[amount][coins.length];
        }
        //简化思路：注意上面的状态转移方程 dp数组只和 dp[...][j]和dp[...][j-1] i维度的跨度大，不好压缩，但j维度只跨越了1，相当于当前维度的计算依赖同样维度的上一次的计算结果，这就是代码中覆盖局部变量以达到降维降低sc的技巧
        static int changeSimplify(int amount, int[] coins){
            int[] dp = new int[amount+1];
            dp[0]=1;//使用所有硬币凑出0元
            //降维时遍历的先后顺序有讲究，被去掉的维度应该先遍历
            //放在此场景的解释是：如果先对amount遍历，如amount=10考虑[1,2,5]三种硬币的[1,2]的组合方案数时，10-2=8, dp[8]是已经考虑了所有类型硬币的组合，是不能拿来使用的
            //也就是被降维的维度如果在dp数组生成时应该是固定的，如果还在变就不是不再考虑这一维度的问题（降维基于不变）
            for (int j = 0; j < coins.length; j++) {
                for (int i = 1; i <= amount; i++) {
                    if (i>=coins[j]){
                        dp[i] = dp[i-coins[j]] + dp[i];
                    }
                }
            }
            //上面的解法还可以再优化
            //for (int coin : coins){
            //    for (int i = coin; i <= amount; i++) {//amount小于此新coin的可以不考虑，反正用不上
            //        dp[i] = dp[i-coin]+dp[i];
            //    }
            //}
            return dp[amount];
        }
        public static void main(String[] args) {
            int[] coins = {1,2,5}, coins2 = {2}, coins3 = {2,4,6};
            int amount = 5, amount2 = 3, amount3 = 10;
            Assert.assertTrue(changeSimplify(amount, coins) == 4);
            Assert.assertTrue(changeSimplify(amount2,coins2) == 0);
            Assert.assertTrue(changeSimplify(amount3,coins3) == 5);//5＊2;2224;244;46;226;
        }
    }



/*-=-=-=-=-= 系列高楼扔鸡蛋问题 =-=-=-=--=-=*/


    /**
     # 72 编辑距离
     单词word1转换成word2的最小操作数，操作指 插入、删除、替换字符
     case1: horse - ros = 3 : h替换为r，删除r，删除e
     case2: intention - execution = 5: 删除t,i->e,n->x,n->c,插入u
     case3: rad - apple = 5: delete r,d->p,insert p,insert l,insert e

     # 分析
     此处分析认为是 s1（索引是i） 转换到 s2（索引是j），从后向前扫描（？？？） todo 向后扫描是不是新解法？
     对于s1[i]与s2[j]有4种操作
     if (s1[i] == s2[j]){
       i,j同时向前移动
     }else{
       三种操作选一：insert、delete、replace
     }
     */
    static class MinEditDistance{
        /*
         容易理解的递归解法
         性能比较弱，tasecase4 jackmarry后面加上none会跑地很吃力
         */
        static int minDistanceRecursively(String si, String sj){
            //return helper(si.toCharArray(),sj.toCharArray(),si.length()-1,sj.length()-1);
            //return helperPositiveSequence(si.toCharArray(),sj.toCharArray(),0,0);
            int[][] memo = new int[si.length()][sj.length()];
            return helperPositiveSequenceWithMemo(si.toCharArray(),sj.toCharArray(),0,0,memo);
        }
        static int helper(char[] si, char[] sj, int i, int j){
            //base case
            //如果一个字符串扫描完，则另一个字符串的剩余字符都要删掉
            //if (j == -1){//sj匹配完了
            //    if (i == -1){
            //        return 0;//两个同时匹配完
            //    }else {
            //        return i+1;//i=0时还剩一个字符
            //    }
            //}
            //if (i == -1){//si用完了，只能insert了
            //    return j+1;//看sj还剩几个字符，i=j=-1的情况上面考虑了
            //}
            //basecase可以简化成两行
            if (j == -1) return i+1;
            if (i == -1) return j+1;
            if (si[i] == sj[j]){
                return helper(si,sj,i-1,j-1);
            }else {
                 return Math.min(
                         helper(si,sj,i,j-1)+1, //si insert 则只需要j前移
                 Math.min(
                         helper(si,sj,i-1,j)+1, //si delete 则只需要i前移
                         helper(si,sj,i-1,j-1)+1 //si replace ij都需要前移
                 ));
            }
        }
        //以si为基准，从前向后扫描，额。。。也能跑通（所以选那种顺序，以谁为准没有区别，不影响递归的正确性）
        static int helperPositiveSequence(char[] si, char[] sj, int i, int j){
            if (i == si.length) return sj.length - j;
            if (j == sj.length) return si.length - i;
            if (si[i] == sj[j]){
                return helperPositiveSequence(si, sj, i+1, j+1);
            }else {
                return Math.min(
                      helperPositiveSequence(si, sj, i+1, j)+1, //create，向后扫描在前面插入元素，j不动
                  Math.min(
                      helperPositiveSequence(si, sj, i, j+1)+1, //delete
                      helperPositiveSequence(si, sj, i+1, j+1)+1 //update
                      )
                );
            }
        }

        /*
        ## 动态规划解法

        //si 源字符串动态
        int[] dp = new int[si.length];
        dp[0] = sj.length;
        dp[1] = ;//si[0,1) 到sj的编辑距离
        dp[2] = dp[1] - (dp[])

        //sj 目标字符串动态
        int[] dp = new int[sj.length];
        dp[0] = si.length;
        dp[1] = contain -1,否则 si.length;

        思路从重叠子问题的发现出发，使用备忘录dp数组(结果速度更慢？)
        memo是一个二维数据，dp数组也定为二维，并且dp数组的填充也要参考递归中的处理过程
         */
        static int helperPositiveSequenceWithMemo(char[] si, char[] sj, int i, int j, int[][] memo){
            if (i == si.length) return sj.length - j;
            if (j == sj.length) return si.length - i;
            if (si[i] == sj[j]){
                if (memo[i][j] != 0){
                    return memo[i][j];
                }
                memo[i][j] = helperPositiveSequence(si, sj, i+1, j+1);
                return memo[i][j];
            }else {
                memo[i][j] = Math.min(
                        helperPositiveSequence(si, sj, i+1, j)+1, //create，向后扫描在前面插入元素，j不动
                        Math.min(
                                helperPositiveSequence(si, sj, i, j+1)+1, //delete
                                helperPositiveSequence(si, sj, i+1, j+1)+1 //update
                        )
                );
                return memo[i][j];
            }
        }
        static int minDistanceDpSolution(String stri, String strj){
            char[] si = stri.toCharArray();
            char[] sj = strj.toCharArray();
            int[][] dp = new int[si.length+1][sj.length+1];
            for (int i = 0; i <= si.length; i++) {
                dp[i][0] = i;
            }
            for (int i = 1; i <= sj.length; i++) {
                dp[0][i] = i;
            }
            for (int i = 1; i <= si.length; i++) {
                for (int j = 1; j <= sj.length; j++) { //对j遍历时i有变化（i减1了），是不能使用状态机进行简化的
                    if (si[i-1] == sj[j-1]){
                        dp[i][j] = dp[i-1][j-1];
                    }else {
                        dp[i][j] = Math.min(
                               dp[i-1][j] + 1,
                            Math.min(
                               dp[i][j-1] + 1,
                               dp[i-1][j-1] + 1
                            )
                        );//显然，dp[i][j]的求解只需要其附近的3个元素即可
                    }
                }
            }
            return dp[si.length][sj.length];
        }

        /*
         * ERROR 做不了状态机化简，状态转移方程在二维遍历中两个维度均有变化
         */
        @Deprecated
        static int minDistanceDpSolutionPlusLowSC(String stri, String strj){
            char[] si = stri.toCharArray();
            char[] sj = strj.toCharArray();
            int left=1,up=1,leftup=0,current=0;
            for (int i = 1; i <= si.length; i++) {
                for (int j = 1; j <= sj.length; j++) {
                    if (si[i-1] == sj[j-1]){
                        current=leftup;
                    }else {
                        current = Math.min(left + 1, Math.min(up + 1, leftup+1));
                    }
                }
            }
            return current;
        }

        /*
         上述状态机SC简化方案不可性，但降维SC简化是可以的，SC = O(min(stri.length(),strj.length()))
         j维度遍历时i只变化了1，所以可以使SC将为O(min), ???如果i变化了2（有i,i-1,i-2）是否SC只能降为2*O(min)???
         对于 horse -> ros
             ""  r  o  s
         ""   0  1  2  3
         h    1  1  2  3
         o    2  2  1  2
         r    3  2  2  2
         s    4  3  3  2
         e    5  4  4  3

         实际写起来还是要两个一维数组记录上一轮操作，改进效果不大
         */
        static int minDistanceDpSolutionLowerSC(String stri, String strj){
            char[] si = stri.toCharArray();
            char[] sj = strj.toCharArray();
            int[] dp = new int[sj.length+1];
            int[] dp2 = new int[sj.length+1];
            for (int i = 0; i < dp2.length; i++) {
                dp2[i] = i;
            }
            for (int i = 1; i <= si.length; i++) {
                int left = i, leftup = dp2[0], up = dp2[1];
                dp[0] = i;
                for (int j = 1; j <= sj.length; j++) {
                    if (si[i-1] == sj[j-1]){
                        dp[j] = leftup;
                    }else {
                        dp[j] = Math.min(leftup+1,Math.min(up+1,left+1));
                    }
                    //error:下面的改值要放在if-else外面
                    left = dp[j];
                    up = j==sj.length ? 0 : dp2[j+1];
                    leftup = dp2[j];
                }
                dp2 = Arrays.copyOf(dp,dp.length);//还要拷贝
                //System.arraycopy(dp, 0, dp2, 0, sj.length + 1);
            }
            return dp[sj.length];
        }

        /**
         ## 算法应用
         如何将算法应用到程序中实现功能？
         将二维dp数组声明成Node(c,u,d,N)记录操作的具体内容，再根据
            替换去左上角  删除向上
            插入向左     什么都不做
         的方式回溯，可实现将字符串修改为目标字符串
         */

        public static void main(String[] args) {
            String si1 = "horse", sj1 = "ros";
            String si2 = "intention", sj2 = "execution";
            String si3 = "rad", sj3 = "apple";
            String si4 = "lucymarriedjack", sj4 = "jackmarrynone";
            long start = System.currentTimeMillis();
            Assert.assertTrue(minDistanceDpSolutionLowerSC(si1,sj1) == 3);
            Assert.assertTrue(minDistanceDpSolutionLowerSC(si2,sj2) == 5);
            Assert.assertTrue(minDistanceDpSolutionLowerSC(si3,sj3) == 5);
            Assert.assertTrue(minDistanceDpSolutionLowerSC(si4,sj4) == 10);//注意jack与lucy都有一个c，所以是10不是11
            System.out.println(System.currentTimeMillis()-start);
        }
    }

    /**
     # KMP字符串匹配算法
        KMP,Knuth-Morris-Pratt算法
     《算法4》一书中也有KMP的讲解（dfa 确定有限状态机）

     >题目描述
     在一个字符串中查找另一个字符串，如果找到返回出现的第一个位置
        "ll" 出现在 "hello" 的位置：2
        "bba" 出现在 "aaaaa" 的位置：-1
     */
    static class KMPAlgorithm{
        /*
         字串逐一比较/双指针比较 TC = O((N-L)L)，线性时间复杂度
         */
        static int strStr(String haystack, String needle) {
            if (needle == null || needle.length() == 0) return 0;
            if (haystack == null || haystack.length() == 0) return -1;
            int haystackLength = haystack.length();
            int needleLength = needle.length();
            for (int i = 0; i <= haystackLength - needleLength; i++) {
                if (haystack.charAt(i) == needle.charAt(0)){
                    int j = 0;
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
        /*
        Rabin Karp 常数复杂度 - hash码
        制定一个滑动窗口内字符串的hash生成策略，滑动窗口移动一次，出一个字符并进入一个字符
        由于只有小写字母，将字符串转成26进制数,窗口移动后新的hash值的计算可以依据旧值推算
        h1 = ( h0 - removedChar * a^(ptn.length - 1))*a + newChar * a^0; (进制数是a)
        化简得：
        h1 = h0*a - rmdChar * a^ptn.length + newChar
        hash可能会溢出，所以找一个很大的数进行取余，这个数取2^31, 此数的来源见 [线性同余生成器的wiki](https://en.wikipedia.org/wiki/Linear_congruential_generator#Parameters_in_common_use)
        TC = O(N)
        * */
        static int strStrHashCheck(String src, String ptn){
            char[] srcc = src.toCharArray();
            char[] ptnc = ptn.toCharArray();
            if(ptnc.length == 0) return 0;
            int a = 26;
            int upthreshold = (int) Math.pow(2,31);
            //计算ptn的hash
            int ptnHash = 0;
            for (int i = 0; i < ptnc.length; i++) {
                ptnHash = ptnHash * a + (ptnc[i]-'a');
            }
            ptnHash = ptnHash % upthreshold;
            for (int i = 0; i <= srcc.length - ptnc.length; i++) {
                int windowHash = 0;//滑动窗口的hash
                for (int j = 0; j < ptnc.length; j++) {
                    windowHash = windowHash * a + (srcc[i+j] - 'a');
                }
                windowHash = windowHash % upthreshold;
                if (windowHash == ptnHash) return i;
            }
            return -1;
        }
        /*
          上述算法对于src6 ptn6的情况效率很差，因为src指针没必要完全回溯，在两个字符串重复字符很多的情况下
            KMP算法可以免于完全回溯到起始点，并且对于src7的情况还能ptn整体继续向后滑动
          KMP算法不回退src的指针，不会重复扫描src，借助dp数组中存储的信息把pat移到正确的位置继续匹配
          确定有限状态自动机
          KMP中dp数组的计算仅与ptn有关，所以计算出的dp可以用于匹配不同的src
        * */
        static class KMPSolution{
            private int[][] dp;
            private String ptn;

            public KMPSolution(String ptn) {
                this.ptn = ptn;
                this.dp = buildbp(ptn);
            }

            /*
             KMP算法通过状态机的构造实现有穷的状态转移，用于在src中出现新的字符时作为激励，判断应该转移到哪个状态
                当前状态+激励 组成状态转移图
             将状态转移规则存入二维数组 dp[state][signal] = nextState
             */
            Map<Character,Integer> ptnMap = new HashMap<>();

            public int[][] buildbp(String ptn){
                char[] ptnChars = ptn.toCharArray();
                //先将所有的char映射到一个顺序编号中，目的是让dp数组第二维度小点，最大也只能达到256，可能不值得建立hash表,但256个长度调试比较麻烦
                for (int i = 0; i < ptnChars.length; i++) {
                    ptnMap.computeIfAbsent(ptnChars[i],nothing -> ptnMap.size());
                }
                dp = new int[ptnChars.length][ptnMap.size()];
                int counselorIndex = 0;//顾问状态/顾问字符/顾问结点，随ptn字符的扫描变化，也可以生成所有ptn字符的顾问点，这样就是又一个数组
                for (int i = 0; i < ptnChars.length; i++) {//i本身也表示状态，状态number与ptn的index一致
                    //这里是对ptn中的所有类型的字符进行一次状态转移演练，演练次数取决于ptn的长度
                    int currCharNo = ptnMap.get(ptn.charAt(i));
                    for (int j = 0; j < ptnMap.size(); j++) {
                        if (j == currCharNo){
                            dp[i][j] = i+1;//转移到下一个状态
                        }else {
                            //状态需要回退，如何回退是这个dp构造的难点
                            //状态在需要回退时，新加到尾部的字符如何转移状态，可通过前面具有相同前缀的状态来决定，如果没有就直接回到初始状态
                            dp[i][j] = dp[counselorIndex][j];
                        }
                    }
                    if (i != 0){
                        //顾问字符的更新就是让顾问字符也遇到ptn中的新字符
                        counselorIndex = dp[counselorIndex][currCharNo];
                        //counselorIndex要么回到前面，要么回到0，而i是递增的，counselorIndex<i
                    }
                }
                return dp;
            }
            //使用这个dp数组
            public int searchPtnInSrc(String src){
                char[] chars = src.toCharArray();
                int currentState = 0;
                for (int i = 0; i < chars.length; i++) {
                    if (!ptnMap.containsKey(chars[i])){
                        currentState = 0;
                    }else {
                        int charNo = ptnMap.get(chars[i]);
                        currentState = dp[currentState][charNo];
                    }
                    if (currentState == ptn.length()) return i - ptn.length()+1;
                }
                return -1;
            }
        }
        static class KMPSearchSolution{
            private int[][] dp;
            private String ptn;
            Map<Character,Integer> ptnMap = new HashMap<>();
            public KMPSearchSolution(String ptn){
                char[] ptnChars = ptn.toCharArray();
                for (int i = 0; i < ptnChars.length; i++) {
                    ptnMap.computeIfAbsent(ptnChars[i],nothing -> ptnMap.size());
                }
                this.ptn = ptn;
                int M = ptn.length();//M表示状态数
                dp = new int[M][ptnMap.size()];//char的取值范围?
                //状态0遇到第一个字符总是进入状态1，其他字符保持
                dp[0][0] = 1;
                int X = 0;//顾问状态点,i到达此状态就会询问X该怎么转
                for (int i = 1; i < M; i++) {
                    int charNo = ptnMap.get(ptn.charAt(i));
                    for (int j = 0; j < ptnMap.size(); j++) {
                        if (charNo == j){
                            dp[i][j] = i+1;
                        }else {
                            dp[i][j] = dp[X][j];
                        }
                    }
                    X = dp[X][charNo];
                }
            }
        }
        public static void main(String[] args) {
            String src1 = "hello", ptn1 = "ll";
            String src2 = "aaaaa", ptn2 = "bba";
            String src3 = "", ptn3 = "";
            String src4 = "mississippi", ptn4 = "issip";
            String src5 = "a", ptn5 = "a";
            String src6 = "aaacaaab", ptn6 = "aaab";
            String src7 = "aaaaaaab", ptn7 = "aaab";
            KMPSearchSolution kmpSearchSolution = new KMPSearchSolution("ababc");
            Assert.assertTrue(new KMPSolution(ptn1).searchPtnInSrc(src1) == 2);
            Assert.assertTrue(new KMPSolution(ptn2).searchPtnInSrc(src2) == -1);
            //Assert.assertTrue(new KMPSolution(ptn3).searchPtnInSrc(src3) == 0);
            Assert.assertTrue(new KMPSolution(ptn4).searchPtnInSrc(src4) == 4);
            Assert.assertTrue(new KMPSolution(ptn5).searchPtnInSrc(src5) == 0);
            Assert.assertTrue(new KMPSolution(ptn6).searchPtnInSrc(src6) == 4);
            Assert.assertTrue(new KMPSolution(ptn7).searchPtnInSrc(src7) == 4);
        }
    }





    /**
     # 494 目标和
     */
    static class TargetSumWays{

    }

}
