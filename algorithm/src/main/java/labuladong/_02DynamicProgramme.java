package labuladong;

import common.TreeNode;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class _02DynamicProgramme {
    static final Logger log = LoggerFactory.getLogger(_02DynamicProgramme.class);

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

    /**
      系列打家劫舍问题
     # 198
     光顾一个数组内的房子，每个房子都有一定现金，相邻房屋装有防盗系统：如果两间相邻的房子在同一晚上被盗，将报警
     房屋现金 money = {1,2,3,1} 计算不触动警报的情况下，一次收集到的最高金额

     case 1: {1,2,3,1} - 4, 去1号和3号房子
     case 2: {2,7,9,3,1} - 12,去1 3 5号房
        纵向为房子数量
          2  7  9  3  1
     1-2  2  -  -  -  -
     2-7  2  7  -  -  -
     3-9  2  7 11  -  -
     4-3  2  7 11  10 -
     5-1  2  7 11  10 12
    解法类似 最长递增子序列
     */
    static class HouseRobber{
        /**
          光顾的最后一个房子，房子数量 两个状态
          思考顺序正过来是递归，倒过来是动态规划
          递归思路：profits[i] = max(profit[i+1], money[i]+profit[i+2])
         */
        static int dpSolutionForI(int[] money){
            int length = money.length;
            int[] dp = new int[length];
            //i表示房子数量，j表示最后光顾的房子，dp[i][j]表示收入
            int result = 0;
            for (int i = 0; i < length; i++) {
                int max = 0;
                for (int k = i-2; k >=0 ; k--) {
                    max = Math.max(max, dp[k]);
                }
                dp[i] = money[i] + max;
                result = Math.max(result, dp[i]);
            }
            return result;
        }
        //上述思路是最后一个房子一定盗，但如果仅考虑房子数量，最后一个不一定盗就可以少一层遍历
        //根本的不同是对于dp数组的定义：dp[i]有前i个房子考虑如何采花
        static int dpSolution2ForI(int[] money){
            int length = money.length;
            int[] dp = new int[length];
            for (int i = 0; i < length; i++) {
                int stealLast = money[i],stealPre = 0;
                if (i>=1){
                    stealPre = dp[i-1];
                }
                if (i>=2){
                    stealLast += dp[i-2];
                }
                dp[i] = Math.max(stealPre,stealLast);//dp[i]依赖dp[i-1] dp[i-2]可以优化SC
            }
            return dp[length-1];
        }
        /**
         窃格瓦拉 发现房屋数组首尾相连成了一个环，作何解？
         case 0: {} 0
         case 1: {2,3,2} 3
         case 2: {1,2,3,1} 4
         case 3: {1,2,3,4,2,1} 7
         ERROR - 第i个房子一定盗窃，则从i-2...到2判断最大的profit 这种算法无法发现第end个房子不盗窃有更大收入的机会

         关键思路：数组里最后一个房子到底去不去 不去-变成问题1 [0,length-2] 去-变成问题1 [1,length-3]
         */
        static int dpSolutionForII(int[] money){
            int length = money.length;
            if (length < 4){
                int max = 0;
                for (int i = 0; i < length; i++) {
                    max = Math.max(max, money[i]);
                }
                return max;
            }
            int stealLast = dpSolutionForIIHelper(money,1,length-3);
            int notstealLast = dpSolutionForIIHelper(money,0,length-2);
            return Math.max(stealLast+money[length-1],notstealLast);
        }
        static int dpSolutionForIIHelper(int[] money, int start, int end){
            int length = end - start +1;
            int[] dp = new int[length];
            for (int i = start; i <= end; i++) {
                int stealLast = money[i], stealPre = 0;
                if (i-1 >= start){
                    stealPre = dp[i-start-1];
                }
                if (i-2 >= start){
                    stealLast += dp[i-start-2];
                }
                dp[i-start] = Math.max(stealPre, stealLast);
            }
            return dp[length-1];
        }
        /**
          时间来到2020年，窃格瓦拉出狱了，然而狱中修炼的他这次决定出国做高技术觅香工作，毕竟人往高处走
         北欧的一个富人区，房子呈二叉树排列，同样两个相连的房屋被盗会报警
         case1: {3,2,3,null,3,null,1}   3+3+1=7
             3
            / \
           2   3
            \   \
             3   1
         case2: {3,4,5,1,3,null,1}    4+5=9
             3
            / \
           4   5
          / \   \
         1   3   1
                   0
             1           2
          3    4      5    6
         7 8  9 10  11 12      一个房子下面的序号是2n+1,2n+2这意味着
         一个房子上面的房子是 ceil(i/2)-1
         似乎不好动态规划
         case3 {4,1,null,2,null,null,null,3}
              4
             1
           2
         3
         既然树相邻的房子不能偷，那就像石头游戏，我隔层偷求和是否可以，比较偶数层与奇数层的和的大小
         ERROR: case3就是对这种思路的致命一击，偷1 4 两层比 其他类型的两层收获多的多。。。
         */
        static void buildBinaryTree(int[] money, TreeNode root, int rootIndex){
            int length = money.length;
            int leftIndex = rootIndex * 2 + 1;
            if (leftIndex < length && money[leftIndex] != 0){
                TreeNode left = new TreeNode(money[leftIndex]);
                root.left = left;
                buildBinaryTree(money, left, leftIndex);
            }
            int rightIndex = rootIndex * 2 + 2;
            if (rightIndex < length && money[rightIndex] != 0){
                TreeNode right = new TreeNode(money[rightIndex]);
                root.right = right;
                buildBinaryTree(money, right, rightIndex);
            }
        }
        static int recursiveSolutionForIIIHelper(TreeNode root){
            if (root == null){
                return 0;
            }
            int stealRoot = root.val +
                    (root.left == null?0: recursiveSolutionForIIIHelper(root.left.left)) +
                    (root.left == null?0: recursiveSolutionForIIIHelper(root.left.right)) +
                    (root.right == null?0: recursiveSolutionForIIIHelper(root.right.left)) +
                    (root.right == null?0: recursiveSolutionForIIIHelper(root.right.right));
            int keepRoot = recursiveSolutionForIIIHelper(root.left) + recursiveSolutionForIIIHelper(root.right);
            return Math.max(stealRoot,keepRoot);
        }
        static int recursiveSolutionForIII(int[] money){
            TreeNode root = new TreeNode(money[0]);
            buildBinaryTree(money,root,0);
            return recursiveSolutionForIIIHelper(root);
        }
        //先让递归进到树的树叶上，但思考问题还是自上而下
        //递归返回的结果是一个元组
        static Pair<Integer,Integer> recursiveSolutionForIII2Helper(TreeNode root){
            if (root == null){
                return Pair.of(0,0);
            }
            Pair<Integer,Integer> left = recursiveSolutionForIII2Helper(root.left);
            Pair<Integer,Integer> right = recursiveSolutionForIII2Helper(root.right);

            int stealRoot = root.val + left.getLeft() + right.getLeft();
            //root不偷，考虑偷left不偷right的组合大小？应该是left偷和不偷两种情况作为左侧分支的考虑结果，这里容易出错
            int keepRoot = Math.max(left.getLeft(), left.getRight())+
                            Math.max(right.getLeft(), right.getRight());
            return Pair.of(keepRoot, stealRoot);//左侧数据是不偷，右侧是偷
        }
        static int recursiveSolutionForIII2(int[] money){
            TreeNode root = new TreeNode(money[0]);
            buildBinaryTree(money,root,0);
            Pair<Integer, Integer> result = recursiveSolutionForIII2Helper(root);
            return Math.max(result.getLeft(), result.getRight());
        }
        //除了递归遍历树，暂未发现有啥更好的算法
        public static void main(String[] args) {
            int[] case1 = {1,2,3,1};
            int[] case2 = {2,7,9,3,1};
            //Assert.assertTrue(dpSolution2ForI(case1) == 4);
            //Assert.assertTrue(dpSolution2ForI(case2) == 12);
            int[] case20 = {};
            int[] case21 = {2,3,2};
            int[] case22 = {1,2,3,1};
            int[] case23 = {1,2,3,4,2,1};
            Assert.assertTrue(dpSolutionForII(case20) == 0);
            Assert.assertTrue(dpSolutionForII(case21) == 3);
            Assert.assertTrue(dpSolutionForII(case22) == 4);
            Assert.assertTrue(dpSolutionForII(case23) == 7);
            int[] case30 = {3,2,3,0,3,0,1};//7
            int[] case31 = {3,4,5,1,3,0,1};//9
            System.out.println(recursiveSolutionForIII2(case31));
        }
    }

/*-=-=-=-=-= 系列高楼扔鸡蛋问题 =-=-=-=--=-=*/

//todo 力扣上说的后效性是啥？


}
