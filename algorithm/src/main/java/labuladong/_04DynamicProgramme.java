package labuladong;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class _04DynamicProgramme {
    private static final Logger log = LoggerFactory.getLogger(_04DynamicProgramme.class);

    /**
     # 651 VIP 四键问题
     对于打印一堆A到屏幕上，多个操作（N）可以选择一直按A，也可以选择拷贝后不断粘贴
     在有限的操作次数内，何种操作顺序得到的字母A最多
     操作A - 打印一个A；操作B - 选中全部的A；操作C - copy；操作D - 粘贴；
     case: N=3,可以得到3个A: A A A，操作序列是: AAA
     case: N=7,最多可以得到9个A，操作序列是: AAABCDD 3*(2+1)>4*2
     case: N=8 AAAABCDD=12 AAABCDDD=12
     case: N=9 AAAABCDDD=16 AAABCDBCD
     case: N=10 AAAABCDBCD=16 AAAABCDDDD=20
     case: N=11 AAAAABCDBCD=20  AAAABCDBCDD=24 AAABCDBCDDD=24 AAAAABCDDDD=25 最大是27
     */
    static class FourKeysProblem{
        /*
         N次操作，穷举共4^N种情形
         +1消耗1，*2消耗3，+n消耗1
         在N次消耗完之前，(+) *2 *(+) 的最大计算结果
         x+3y+z = N; [x*(2^yy+zz)]*(2^yyy+zzz) 的最大值
         ERROR: 此算法在N=11时错误，错误地认为得到最多的A就一定是 AAA...ABCDD...D 这样的操作序列，显然AA..ABCDD..DBCD..D会有更多可能
         */
        static int myMathmaticsSolution(int N){
            int maxVal = 0;
            for (int i = 1; i <= N; i++) {
                for (int j = 0; j <= (N-i)/3; j++) {
                    int left = N-i-j*3;
                    int current = 0;
                    if (j!=0){
                        current = i*((int)Math.pow(2,j)+left);
                    }else {
                        current = i;
                    }
                    maxVal = Math.max(maxVal, current);
                }
            }
            return maxVal;
        }

        /*
         一种类似编辑距离的动态规划解法
         动态规划的思路首先是找状态：剩余操作数i、当前A字符的数量j、剪贴板中A字符的数量k，则dp(i-1,j+1,k)表示按下A，dp(i-1,j+k,k)表示粘贴，dp(i-2,j,j)表示按下选择拷贝
         则这三者的最大值就是应进行的操作
        */
        static int mindistanceSoluiton(int N){
            return mindistanceRecursiveHelper(N,0,0);
        }
        static int mindistanceRecursiveHelper(int leftOpCount, int ACount, int acount){
            if (leftOpCount <= 0) return ACount;
            return Math.max(
                mindistanceRecursiveHelper(leftOpCount-1, ACount+1, acount)
                        ,Math.max(
                mindistanceRecursiveHelper(leftOpCount-1, ACount+acount, acount),
                mindistanceRecursiveHelper(leftOpCount-2, ACount, ACount)
                    ));
        }//这种递归解法无脑拷贝粘贴，很多子问题是低效的

        /*
        一种类似最大递增子序列的动态规划解法
        回到四种操作 ABCD, 正常的操作都是 AAA..BCD..BCDDDD,这样可以从后向前i，i表示后面全是D的取值范围，则i-2范围内的最大字符数进行计算即可，缩小了问题规模
        */
        static int subsequenceSolution2(int N){
            int[] dp = new int[N+1];
            //从后向前缩小了问题规模，但小规模的问题还未求解，所以就有问题从0开始逐步改变问题总规模从后向前的求解，这样就是二维数组了
            for (int i = 1; i <= N; i++) { //i递增表示问题总规模逐步扩大
                int cvcount = 0;//从后向前逐步进行cvv，扩大v的规模可得到的数量
                for (int j = i; j >=4 ; j--) {//在问题规模极小时恰好就是拷贝粘贴不如单击A结果多的情形
                    cvcount = Math.max(cvcount, dp[j-3]*(i-j+2));//j=i -2倍，i-j=1-3倍 i-j=2~4倍
                }
                dp[i] = Math.max(i,cvcount);//需要跟全部输入A的操作进行比较，仅在4-6时用得到，i从7起步可以进一步简化代码
            }
            return dp[N];
        }
        /*
         在动态规划中收集每个步骤,ops打断点可观察到具体操作序列
         N <= 6       全A
         N =  7 - 9   AAABCDD
         N =  8 - 12  AAABCDDD
         N =  9 - 16  AAAABCDDD
         N = 10 - 20  AAAABCDDDD
         N = 11 - 27  AAAAABCDDDD - 25, AAABCDDBCDD 9*3=27
         */
        static int subsequenceSolution3(int N){
            int[] dp = new int[N+1];
            List<List<String>> ops = new ArrayList<>(N+1);
            for (int i = 0; i < N+1; i++) {
                ops.add(new ArrayList<>(i));
            }
            for (int i = 1; i<=N && i < 7; i++) {
                dp[i] = i;
                for (int j = 0; j < i; j++) {
                    ops.get(i).add("A");
                }
            }
            for (int i = 7; i <= N; i++) {
                int firstDIndexInLastGroup = 0;
                for (int j = i; j >=4 ; j--) {
                    int cvcount = dp[j - 3] * (i - j + 2);
                    if (cvcount>dp[i]){
                        dp[i] = cvcount;
                        firstDIndexInLastGroup = j;
                    }
                }
                //firstDIndexInLastGroup及之后为全DD，前面有BC，拷贝firstDIndexInLastGroup-3的数据进来
                List<String> currentops = ops.get(i);
                currentops.addAll(ops.get(firstDIndexInLastGroup-3));
                currentops.add("B");currentops.add("C");
                for (int j = firstDIndexInLastGroup; j <= i; j++) {
                    currentops.add("D");
                }
            }
            return dp[N];
        }
        static int subsequenceSolution(int N){
            int[] dp = new int[N + 1];
            dp[0] = 0;
            for (int i = 1; i <= N; i++) {
                // 按 A 键
                dp[i] = dp[i - 1] + 1;
                for (int j = 2; j < i; j++) {
                    // 全选 & 复制 dp[j-2]，连续粘贴 i - j 次
                    // 屏幕上共 dp[j - 2] * (i - j + 1) 个 A
                    dp[i] = Math.max(dp[i], dp[j - 2] * (i - j + 1));
                }
            }
            return dp[N];
        }
        /*
         定义dp[i][j]表示拷贝粘贴操作的起始和数量，则i+j*3<=length
         j>=0,i∈[1,length-3] 以i为主，j为次遍历，，，似乎还是上面的
         是否可以总结成三种不同权重的操作，成为背包问题，但放置顺序有要求
         */
        public static void main(String[] args) {
            for (int i = 1; i < 20; i++) {
                log.info("N={}, mindistance - {}, subsequence - {}, mine - {}",
                        i,mindistanceSoluiton(i),subsequenceSolution(i),myMathmaticsSolution(i));
            }

        }
    }



    /**
     # 494 目标和
     */
    static class TargetSumWays{

    }
}
