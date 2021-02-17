package labuladong;

import org.junit.Assert;
import org.junit.Test;

/**
 * 日常测试
 * 0822 默写fibnacci数列的递归、备忘录解法
 *      硬币问题的递归、带备忘录的递归和动态规划解法
 *      最长递增子序列的
 */
public class _01DPRecite {
    //fibonacci
    int fibonacci(int k){
        if (k == 1 || k == 2) return 1;
        int f1 = 1,f2 = 1;
        int result = 0;
        for (int i = 3; i <= k; i++) {
            result = f1 + f2;
            f1 = f2; f2 = result;
        }
        return result;
    }
    int fibonacciWithArray(int k){
        if (k == 1 || k == 2) return 1;
        int[] result = new int[k];
        result[0] = result[1] = 1;
        for (int i = 2; i <= k-1; i++) {
            result[i] = result[i-1] + result[i-2];
        }
        return result[k-1];
    }
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
        Assert.assertEquals(fibonacciWithArray(2), fibnacci3(2));
        Assert.assertEquals(fibonacciWithArray(3), fibnacci3(3));
        Assert.assertEquals(fibonacciWithArray(4), fibnacci3(4));
        Assert.assertEquals(fibonacciWithArray(5), fibnacci3(5));
        Assert.assertEquals(fibonacciWithArray(6), fibnacci3(6));
        Assert.assertEquals(fibonacciWithArray(7), fibnacci3(7));
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
    //股票问题I - 7,1,5,3,6,4 交易一次的最大利润,固定卖出时间求解
    int stock1Max(int[] prices){
        int min = prices[0];
        int maxProfit = 0;
        int[] dpTable = new int[prices.length];
        for (int i = 0; i < prices.length; i++) {
            int curr = prices[i];
            dpTable[i] = curr - min;//记录maxProfit为最大值，dpTable都不需要
            if (min > curr) min = curr;
        }
        return dpTable[3];
    }
    //股票问题II - 多次交易累积最大收益
    int stock2Max(int[] prices){
        int sum = 0;
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] > prices[i-1]){
                sum += (prices[i] - prices[i-1]);
            }
        }
        return sum;
    }
    //股票问题II - 暴力穷举的递归解法
    /*-=-=-=-=- ERROR -=-=-=-=-=-=-=*/
    int stock2MaxRecursively(int[] prices){
        return helperInStock2MaxRecursively(prices,0);
    }
    int helperInStock2MaxRecursively(int[] prices, int buy){
        if (buy >= prices.length){
            return 0;
        }
        int max = Integer.MIN_VALUE, lastsell = buy+1;
        for (int i = buy; i < prices.length; i++) {
            for (int j = i+1; j < prices.length; j++) {
                int profit = prices[j] - prices[i];
                if (max < profit) {
                    max = profit;lastsell = j;
                }
            }
        }//!!!!!!!!!!!!!!!!!  把递归调用写在了两层for循环外面。。。
        return max + helperInStock2MaxRecursively(prices,lastsell);
    }
    int helperInStock2MaxRecursively2(int[] prices, int buy){
        int max = 0;
        for (int i = buy; i < prices.length; i++) {
            for (int j = i+1; j < prices.length; j++) {
                int bottomProfit = helperInStock2MaxRecursively2(prices, j);
                int currProfit = bottomProfit + prices[j] - prices[i];
                if (max < currProfit) {
                    max = currProfit;
                }
            }
        }
        return max;
    }
    private static int[] huge_test_case = {397,6621,4997,7506,8918,1662,9187,3278,3890,514,18,9305,93,5508,3031,2692,6019,1134,1691,4949,5071,799,8953,7882,4273,302,6753,4657,8368,3942,1982,5117,563,3332,2623,9482,4994,8163,9112,5236,5029,5483,4542,1474,991,3925,4166,3362,5059,5857,4663,6482,3008,3616,4365,3634,270,1118,8291,4990,1413,273,107,1976,9957,9083,7810,4952,7246,3275,6540,2275,8758,7434,3750,6101,1359,4268,5815,2771,126,478,9253,9486,446,3618,3120,7068,1089,1411,2058,2502,8037,2165,830,7994,1248,4993,9298,4846,8268,2191,3474,3378,9625,7224,9479,985,1492,1646,3756,7970,8476,3009,7457,8922,2980,577,2342,4069,8341,4400,2923,2730,2917,105,724,518,5098,6375,5364,3366,8566,8838,3096,8191,2414,2575,5528,259,573,5636,4581,9049,4998,2038,4323,7978,8968,6665,8399,7309,7417,1322,6391,335,1427,7115,853,2878,9842,2569,2596,4760,7760,5693,9304,6526,8268,4832,6785,5194,6821,1367,4243,1819,9757,4919,6149,8725,7936,4548,2386,5354,2222,8777,2041,1,2245,9246,2879,8439,1815,5476,3200,5927,7521,2504,2454,5789,3688,9239,7335,6861,6958,7931,8680,3068,2850,1181,1793,7138,2081,532,2492,4303,5661,885,657,4258,131,9888,9050,1947,1716,2250,4226,9237,1106,6680,1379,1146,2272,8714,8008,9230,6645,3040,2298,5847,4222,444,2986,2655,7328,1830,6959,9341,2716,3968,9952,2847,3856,9002,1146,5573,1252,5373,1162,8710,2053,2541,9856,677,1256,4216,9908,4253,3609,8558,6453,4183,5354,9439,6838,2682,7621,149,8376,337,4117,8328,9537,4326,7330,683,9899,4934,2408,7413,9996,814,9955,9852,1491,7563,421,7751,1816,4030,2662,8269,8213,8016,4060,5051,7051,1682,5201,5427,8371,5670,3755,7908,9996,7437,4944,9895,2371,7352,3661,2367,4518,3616,8571,6010,1179,5344,113,9347,9374,2775,3969,3939,792,4381,8991,7843,2415,544,3270,787,6214,3377,8695,6211,814,9991,2458,9537,7344,6119,1904,8214,6087,6827,4224,7266,2172,690,2966,7898,3465,3287,1838,609,7668,829,8452,84,7725,8074,871,3939,7803,5918,6502,4969,5910,5313,4506,9606,1432,2762,7820,3872,9590,8397,1138,8114,9087,456,6012,8904,3743,7850,9514,7764,5031,4318,7848,9108,8745,5071,9400,2900,7341,5902,7870,3251,7567,2376,9209,9000,1491,7030,2872,7433,1779,362,5547,7218,7171,7911,2474,914,2114,8340,8678,3497,2659,2878,2606,7756,7949,2006,656,5291,4260,8526,4894,1828,7255,456,7180,8746,3838,6404,6179,5617,3118,8078,9187,289,5989,1661,1204,8103,2,6234,7953,9013,5465,559,6769,9766,2565,7425,1409,3177,2304,6304,5005,9559,6760,2185,4657,598,8589,836,2567,1708,5266,1754,8349,1255,9767,5905,5711,9769,8492,3664,5134,3957,575,1903,3723,3140,5681,5133,6317,4337,7789,7675,3896,4549,6212,8553,1499,1154,5741,418,9214,1007,2172,7563,8614,8291,3469,677,4413,1961,4341,9547,5918,4916,7803,9641,4408,3484,1126,7078,7821,8915,1105,8069,9816,7317,2974,1315,8471,8715,1733,7685,6074,257,5249,4688,8549,5070,5366,2962,7031,6059,8861,9301,7328,6664,5294,8088,6500,6421,1518,4321,5336,2623,8742,1505,9941,1716,2820,4764,6783,906,2450,2857,7515,4051,7546,2416,9121,9264,1730,6152,1675,592,1805,9003,7256,7099,3444,3757,9872,4962,4430,1561,7586,3173,3066,3879,1241,2238,8643,8025,3144,7445,882,7012,1496,4780,9428,617,396,1159,3121,2072,1751,4926,7427,5359,8378,871,5468,8250,5834,9899,9811,9772,9424,2877,3651,7017,5116,8646,5042,4612,6092,2277,1624,7588,3409,1053,8206,3806,8564,7679,2230,6667,8958,6009,2026,7336,6881,3847,5586,9067,98,1750,8839,9522,4627,8842,2891,6095,7488,7934,708,3580,6563,8684,7521,9972,6089,2079,130,4653,9758,2360,1320,8716,8370,9699,6052,1603,3546,7991,670,3644,6093,9509,9518,7072,4703,2409,3168,2191,6695,228,2124,3258,5264,9645,9583,1354,1724,9713,2359,1482,8426,3680,6551,3148,9731,8955,4751,9629,6946,5421,9625,9391,1282,5495,6464,5985,4256,5984,4528,952,6212,6652,562,1476,6297,145,9182,8021,6211,1542,5856,4637,1574,2407,7785,1305,1362,2536,934,4661,4309,559,4052,1943,2406,516,4280,6662,2852,8808,7614,9064,1813,4529,6893,8110,4674,2427,2484,7237,3969,8340,1874,5543,7099,6011,3200,8461,8547,486,9474,9208,7397,9879,7503,9803,6747,1783,6466,9600,6944,432,8664,8757,4961,1909,6867,5988,4337,5703,3225,4658,4043,1452,6554,1142,7463,9754,5956,2363,241,1782,7923,7638,1661,5427,3794,8409,7210,260,8009,4154,692,3025,9263,2006,4935,2483,7994,5624,8186,7571,282,8582,9023,6836,6076,6487,6591,2032,8850,3184,3815,3125,7174,5476,8552,968,3885,2115,7580,8246,2621,4625,1272,1885,6631,6207,4368,4625,8183,2554,8548,8465,1136,7572,1654,7213,411,4597,5597,5613,7781,5764,8738,1307,7593,7291,8628,7830,9406,6208,6077,2027,833,7349,3912,7464,9908,4632,8441,8091,7187,6990,2908,4675,914,4562,8240,1325,9159,190,6938,3292,5954,2028,4600,9899,9319,3228,7730,5077,9436,159,7105,6622,7508,7369,4086,3768,2002,8880,8211,5541,2222,1119,216,3136,5682,4809,813,1193,4999,4103,4486,7305,6131,9086,7205,5451,2314,1287,528,8102,1446,3985,4724,5306,1355,5163,9074,9709,4043,7285,5250,2617,4756,1818,2105,6790,6627,2918,7984,7978,7021,2470,1636,3152,7908,8841,4955,222,6480,5484,4676,7926,5821,9401,3232,7176,916,8658,3237,1311,5943,8487,3928,7051,306,6033,3842,3285,8951,1826,7616,2324,648,9252,5476,8556,4445,6784};
    //来个备忘录
    int helperInStock2MaxRecursively3(int[] prices, int buy){
        int max = 0;
        int[] memo = new int[prices.length];//备忘录，从某个index处购买所得的maxProfit
        for (int i = buy; i < prices.length; i++) {
            for (int j = i+1; j < prices.length; j++) {
                int bottomProfit = 0;
                if (memo[j] == 0){
                    bottomProfit = helperInStock2MaxRecursively3(prices, j);
                    memo[j] = bottomProfit;
                }else {
                    bottomProfit = memo[j];
                }
                int currProfit = bottomProfit + prices[j] - prices[i];
                if (max < currProfit) {
                    max = currProfit;
                }
            }
        }
        return max;
    }

    @Test
    public void test186() {
        int[] prices = {7,1,5,3,6,4};
        long start = System.currentTimeMillis();
        System.out.println(stock2Max(huge_test_case));
        // System.out.println(helperInStock2MaxRecursively3(huge_test_case,0)); 永远都无法运行完
        System.out.println(System.currentTimeMillis() - start);
    }

    /**
     * 系列股票问题，由问题II引出通用解法
     */
    static class StockMaxProfit{
        static int[] prices0 = {3,2,5,6,10,9,7,11,14};//15:
        static int[] prices1 = {7,1,5,3,6,4};//7:1-5,3-6
        static int[] prices2 = {1,2,3,4,5};//4:1-5
        static int[] prices3 = {7,6,4,3,1};//0
        static int[] prices4 = {1,2,3,0,2};//3
        static int[] prices5 = {2,1};//0
        static int[] prices6 = {2,1,4};//3
        static int[] prices7 = {6,1,6,4,3,0,2};//7
        static int[] prices8 = {1, 3, 2, 8, 4, 9};

        //问题 II 由问题II引出股票问题的通用解题框架
        static class StockMaxProfitII{
            static int caller(int[] prices){
                int[] memo = new int[prices.length];
                for (int i = 0; i < prices.length; i++) {
                    memo[i] = -1;
                }
                return helper(prices,memo,0);
            }
            static int helper(int[] prices, int[] memo, int start2buy){
                if (start2buy >= prices.length-1) return 0;
                //明确备忘录的作用：记录start2buy对应的maxprofit
                if (memo[start2buy] != -1) return memo[start2buy];
                int lastMaxProfit = 0;
                int currMaxProfit = 0;
                for (int buy = start2buy; buy < prices.length; buy++) {
                    int currMinPrice = prices[buy];
                    for (int sell = buy+1; sell < prices.length; sell++) {
                        //ERROR:需要递归到下一次交易之前，显然只有一次交易，而计算一次交易的最大profit是很容易的| min可能记录的是先前的一笔交易里的最小值
                        //currMinPrice = Math.min(currMinPrice, prices[sell]);
                        int currProfit = prices[sell] - prices[buy];
                        if (currProfit < 0) continue;
                        int bottomMaxProfit = helper(prices, memo, sell);
                        currMaxProfit = Math.max(currMaxProfit, bottomMaxProfit + currProfit);//由最低价确定最高利润还要再一次Math
                    }
                    memo[start2buy] = currMaxProfit;
                    //不需要记录lastMax,备忘录里存的就是
                    //lastMaxProfit = Math.max(lastMaxProfit, currMaxProfit);
                }
                return memo[0];
            }

            /**
             [a1 a2 a3 a4]: a1-a2,       a1-a3,a1-a4
                            a2-a3, a2-a4,
                            a3-a4
             注意在暴力递归中，不需要对buy的外层遍历做最大值判断，因为在sell-buy<0进行continue时就达到了这个效果
             而currMinPrice是用在固定卖出时间情况下的
             */
            /**

             股票问题的通用解法框架（最优递归,居然可以通过leetcode的最大规模测试）

             股票问题的通用解法 在于 固定卖出时刻进行递归计算，从start2buy到sell只有一次交易。这种解法还少一层遍历
             */
            static int stockCommonFramework(int[] prices){
                int[] memo = new int[prices.length];
                for (int i = 0; i < prices.length; i++) {
                    memo[i] = -1;
                }
                return commonHelper(prices,memo,0);
            }
            static int commonHelper(int[] prices, int[] memo, int start2buy){
                if (start2buy >= prices.length-1) return 0;
                //备忘录定义：start2buy买入时的最大利润
                if (memo[start2buy] != -1) return memo[start2buy];//【优化点】
                int currMinPrice = prices[start2buy],currMaxProfit = 0;
                for (int sell = start2buy + 1; sell < prices.length; sell++) {
                    currMinPrice = Math.min(currMinPrice, prices[sell]);//【优化点】
                    int currProfit = prices[sell] - currMinPrice;//这里的currProfit就已经带有currMaxProfit的含义
                    if (currProfit < 0) continue;//【优化点】
                    int bottomMaxProfit = commonHelper(prices, memo, sell);
                    currMaxProfit = Math.max(currMaxProfit, currProfit + bottomMaxProfit);
                }
                //似乎在方法返回之前memo只会写一次，但是递归的调用使得memo被填充
                memo[start2buy] = currMaxProfit;
                return currMaxProfit;
            }
            public static void main(String[] args) {
                System.out.println(stockCommonFramework(huge_test_case));
            }

        }//StockMaxProfitII

        static class StockMaxProfitIII{
            static int stockCommonFramework(int[] prices, int times){
                int[] memo = new int[prices.length];
                for (int i = 0; i < prices.length; i++) {
                    memo[i] = -1;
                }
                return commonHelper(prices,memo,0, times);
            }
            static int commonHelper(int[] prices, int[] memo, int start2buy, int times){
                if (times <= 0) return 0;
                if (start2buy >= prices.length-1) return 0;
                //备忘录定义：start2buy买入时的最大利润
                if (memo[start2buy] != -1) return memo[start2buy];//【优化点】
                int currMinPrice = prices[start2buy],currMaxProfit = 0;
                for (int sell = start2buy + 1; sell < prices.length; sell++) {
                    currMinPrice = Math.min(currMinPrice, prices[sell]);//【优化点】
                    int currProfit = prices[sell] - currMinPrice;//这里的currProfit就已经带有currMaxProfit的含义
                    if (currProfit < 0) continue;//【优化点】
                    int bottomMaxProfit = commonHelper(prices, memo, sell, times - 1);
                    currMaxProfit = Math.max(currMaxProfit, currProfit + bottomMaxProfit);
                }
                //似乎在方法返回之前memo只会写一次，但是递归的调用使得memo被填充
                memo[start2buy] = currMaxProfit;
                return currMaxProfit;
            }
            public static void main(String[] args) {
                //leetcode超时
                System.out.println(stockCommonFramework(huge_test_case,2));
            }
        }//StockMaxProfitIII

        static class StockMaxProfitIV{
            static int stockCommonFramework(int[] prices){
                int[] memo = new int[prices.length];
                for (int i = 0; i < prices.length; i++) {
                    memo[i] = -1;
                }
                return commonHelper(prices,memo,0,1);
            }
            static int commonHelper(int[] prices, int[] memo, int start2buy, int frozen){
                if (start2buy >= prices.length - 1) return 0;
                if (memo[start2buy] != -1) return memo[start2buy];
                int currMinPrice = prices[start2buy];
                int currStartMaxProfit = 0;
                for (int sell = start2buy + 1; sell < prices.length; sell++) {
                    currMinPrice = Math.min(currMinPrice, prices[sell]);
                    int currMaxProfit = prices[sell] - currMinPrice;
                    if (currMaxProfit < 0) continue;
                    int bottomMaxProfit = commonHelper(prices, memo, sell+1+frozen, frozen);
                    currStartMaxProfit = Math.max(currStartMaxProfit, bottomMaxProfit+currMaxProfit);
                }
                memo[start2buy] = currStartMaxProfit;
                return currStartMaxProfit;
            }
            public static void main(String[] args) {
                System.out.println(stockCommonFramework(prices7));
            }
        }//StockMaxProfitIV

        static class StockMaxProfitV{
            static int stockCommonFramework(int[] prices){
                int[] memo = new int[prices.length];
                for (int i = 0; i < prices.length; i++) {
                    memo[i] = -1;
                }
                return commonHelper(prices,memo,0,2);
            }
            static int commonHelper(int[] prices, int[] memo, int start2buy, int fee){
                if (start2buy >= prices.length - 1) return 0;
                if (memo[start2buy] != -1) return memo[start2buy];
                int currMinPrice = prices[start2buy];
                int currMaxProfit = 0;
                for (int sell = start2buy + 1; sell < prices.length; sell++) {
                    currMinPrice = Math.min(currMinPrice, prices[sell]);
                    int currProfit = prices[sell] - currMinPrice;
                    if (currMaxProfit < 0) continue;
                    int bottomProfit = commonHelper(prices, memo, sell, fee);
                    currMaxProfit = Math.max(currMaxProfit, bottomProfit+currProfit-fee);
                }
                memo[start2buy] = currMaxProfit;
                return currMaxProfit;
            }
            public static void main(String[] args) {
                //leetcode超出时间限制
                System.out.println(stockCommonFramework(prices8));
            }
        }//StockMaxProfitV
    }


}
