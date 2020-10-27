package labuladong;

import org.junit.Assert;

public class _04DynamicProgramme {
    /**
     对于打印一堆A到屏幕上，多个操作（N）可以选择一直按A，也可以选择拷贝后不断粘贴
     在有限的操作次数内，何种操作顺序得到的字母A最多
     操作A - 打印一个A；操作B - 选中全部的A；操作C - copy；操作D - 粘贴；
     case: N=3,可以得到3个A: A A A，操作序列是: AAA
     case: N=7,最多可以得到9个A，操作序列是: AAABCDD 3*(2+1)>4*2
     case: N=8 AAAABCDD=12 AAABCDDD=12
     case: N=9 AAAABCDDD=16 AAABCDBCD
     case: N=10 AAAABCDBCD=16 AAAABCDDDD=20
     case: N=11 AAAAABCDBCD=20  AAAABCDBCDD=24 AAABCDBCDDD=24 AAAAABCDDDD=25 AABCDAABCDD
     */
    static class FourKeysProblem{
        /*
         N次操作，穷举共4^N种情形
         +1消耗1，*2消耗3，+n消耗1
         在N次消耗完之前，(+) *2 *(+) 的最大计算结果
         x+3y+z = N; [x*(2^yy+zz)]*(2^yyy+zzz) 的最大值
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

         定义dp[i][j]表示拷贝粘贴操作的起始和数量，则i+j*3<=length
         j>=0,i∈[1,length-3] 以i为主，j为次遍历，，，似乎还是上面的

         可以总结成三种不同权重的操作，成为背包问题，但放置顺序有要求

         回到四种操作 ABCD, 正常的操作都是 AAA..BCD..BCDDDD,这样可以从后向前i，i表示后面全是D的取值范围，则i-2范围内的最大字符数进行计算即可，缩小了问题规模，是典型的最大递增子序列问题D

         动态规划的思路首先是找状态：剩余操作数i、当前A字符的数量j、剪贴板中A字符的数量k，则dp(i-1,j+1,k)表示按下A，dp(i-1,j*2,k)表示粘贴，dp(i-2,j,j)表示按下选择拷贝
         则这三者的最大值就是答案，这样这个动态规划就变成了典型的编辑距离问题

         */
        static int dpSolution(int N){
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
            // N 次按键之后最多有几个 A？
            return dp[N];
        }

        public static void main(String[] args) {
            //todo N=11时解法与dp不一致，需要翻译出第一种解法，打印出具体操作序列
            System.out.println(dpSolution(11));
            System.out.println(myMathmaticsSolution(11));
            for (int i = 3; i < 50; i++) {
                Assert.assertTrue(dpSolution(i) == myMathmaticsSolution(i));
            }

        }
    }
    /**
     # 494 目标和
     */
    static class TargetSumWays{

    }
}
