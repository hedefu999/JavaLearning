package labuladong;

import org.junit.Assert;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class _03DynamicProgramme {
    /**
     # 1143 最长公共子序列 Longest Common Subsequence,LCS,与最小编辑距离解题思路接近
     字符串长度限制1000，只含有小写英文字母
     babcde 与 ace 的 LCS 是 ace
     sghregsa - gegad = 4
     sghregsa - goiuiybhn = 2
     abc - abc = 3
     abc - def = 0
        "" s g h r e g s a
     "" ?  0 0 0 0 0 0 0 0
     g  0  0 1 1 1 1 1 1 1
     e  0  0 1 1 1 2 2 2 2
     g  0  0 1 1 1 2 3 3 3
     a  0
     d  0
     sghj+b gha+j result = calc(2,) = 3
     sghre+g - ge+g = 2+1(后面新增加的g是否在前面字符串匹配的ge之后，str.substring(str.lastIndexof(e)).contains(g)? )
     dp[i][j] = dp[i-1][j] + str.substring(str.lastIndexof(e)).contains(g)?1:0;
     上面这种查找的方法在 s+g - ge+g 情形下不适用

     正确的思路是：sghj+b gha+j result = b跟j相等吗，相等的话sghj跟gha计算的结果+1，不相等取 （sghjb与gha，sghj与ghaj，sghj与gha）的值，取最大（由于sghj与gha的LCS肯定不会查过其他两个，所以可以不参与计算）
        这样 dp[i][j] 并不是从 dp[i-1][j] 上 +1 的
     字符串类的动态规划问题，通常都是跟前面三个角上的数据进行计算获得
     dp[i-1][j-1] dp[i-1][j]
     dp[i][j-1]   dp[i][j]
     */
    static class LongestCommonSubSequence{
        static int LCSDpSolution(String s1,String s2){
            char[] chars1 = s1.toCharArray();
            char[] chars2 = s2.toCharArray();
            int[][] dp = new int[chars2.length+1][chars1.length+1];
            for (int i = 0; i <= chars2.length; i++) {
                dp[i][0] = 0;
            }
            for (int i = 0; i <= chars1.length; i++) {
                dp[0][i] = 0;
            }
            for (int i = 1; i <= chars2.length; i++) {
                for (int j = 1; j <= chars1.length; j++) {
                    //s+g - ge+g 情况下会重复计算字符串，而且查找已经混乱了
                    int plusOne = 0;
                    char preChar = i>2?chars2[i-2]:0;
                    for (int k = j-1; k>=0 && chars1[k]!=preChar; k--) {
                        if (chars1[k] == chars2[i-1]){
                            plusOne = 1;
                            break;
                        }
                    }
                    dp[i][j] = dp[i-1][j]+plusOne;
                }
            }
            return dp[chars2.length][chars1.length];
        }
        static int LCSDpSolution2(String s1,String s2){
            char[] chars1 = s1.toCharArray();
            char[] chars2 = s2.toCharArray();
            StringBuilder lcs = new StringBuilder();
            int[][] dp = new int[chars2.length+1][chars1.length+1];
            for (int i = 0; i <= chars2.length; i++) {
                dp[i][0] = 0;
            }
            for (int i = 0; i <= chars1.length; i++) {
                dp[0][i] = 0;
            }
            int current = 0;
            for (int i = 1; i <= chars2.length; i++) {
                for (int j = 1; j <= chars1.length; j++) {
                    if (chars1[j-1] == chars2[i-1]){
                        dp[i][j] = dp[i-1][j-1] + 1;
                        if (dp[i][j] > current){ //如何把lcs打印出来，跟最小编辑距离的编辑路径的寻找很相似
                            lcs.append(chars1[j-1]);
                            current+=1;
                        }
                    }else {
                        dp[i][j] = Math.max(dp[i-1][j],dp[i][j-1]);
                    }
                }
            }
            System.out.println("lcs = "+lcs);
            return dp[chars2.length][chars1.length];
        }

        public static void main(String[] args) {
            String s1 = "babcde", s2 = "ace";
            String t1 = "sghregsa", t2 = "gegad";
            Assert.assertTrue(LCSDpSolution2(s1,s2) == 3);
            Assert.assertTrue(LCSDpSolution2(t1,t2) == 4);
        }
    }


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
                if (ptnChars.length == 0) return null;
                //先将所有的char映射到一个顺序编号中，目的是让dp数组第二维度小点，最大也只能达到256，可能不值得建立hash表,但256个长度调试比较麻烦
                for (int i = 0; i < ptnChars.length; i++) {
                    ptnMap.computeIfAbsent(ptnChars[i],nothing -> ptnMap.size());
                }
                dp = new int[ptnChars.length][ptnMap.size()];
                int counselorIndex = 0;//顾问状态/顾问字符/顾问结点，随ptn字符的扫描变化，也可以生成所有ptn字符的顾问点，这样就是又一个数组
                // dp[0][ptnMap.get(ptnChars[0])] = 1;//dp[0][]是确定的，只有一个1，其他都是0
                for (int i = 0; i < ptnChars.length; i++) {//i本身也表示状态，状态number与ptn的index一致
                    //这里是对ptn中的所有类型的字符进行一次状态转移演练，演练次数取决于ptn的长度
                    int currCharNo = ptnMap.get(ptnChars[i]);
                    for (int j = 0; j < ptnMap.size(); j++) {
                        if (j == currCharNo){
                            dp[i][j] = i+1;//转移到下一个状态
                        }else {
                            //状态需要回退，如何回退是这个dp构造的难点
                            //状态在需要回退时，新加到尾部的字符如何转移状态，可通过前面具有相同前缀的顾问状态来决定，如果没有就直接回到初始状态
                            dp[i][j] = dp[counselorIndex][j];
                        }
                    }
                    /*
                    顾问index更新的秘密
                    状态0依次遇到ptn中的字符一定会递增状态，i就是这样不断向前，counselorIndex也是接受当前的字符信号进行状态转移的，就是下面的dp[][currCharNo]
                    counselorIndex是跟随i的，但它在第0步没有跟随，否则就跟i没有区别，它相当于对 ptn(1,ptn.length) 的进行从0状态开始的状态转移，丢掉了开头的第一个字符
                    其效果就是不断等待i走过的相同的前缀在再次出现，从而达到跟踪到相同前缀结尾的目的
                    * */
                    if (i > 0){//i可以从1开始，这样这个判断就可以去掉，但为理解 counselorIndex从1开始进行状态转移 这一重要内容，还是要体现出来
                        counselorIndex = dp[counselorIndex][currCharNo];
                    }
                    //dp数组内可以存有一连串的顾问状态，如 abcabcabcedf
                }
                return dp;
            }
            //使用这个dp数组
            public int searchPtnInSrc(String src){
                if (dp == null || dp.length == 0) return 0;
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
        /*
        化简过的KMP性能还是比不上上面的hash码的解法，可能要在其他场景才能显现KMP的威力
        * */
        public static int KMPSimplify(String haystack,String needle){
            char[] ptnChars = needle.toCharArray();
            if (ptnChars.length == 0) return 0;
            int[][] dp = new int[ptnChars.length][256];
            int counselorIndex = 0;
            dp[0][ptnChars[0]] = 1;
            for (int i = 1; i < ptnChars.length; i++) {
                for (int j = 0; j < 256; j++) {
                    if (j == ptnChars[i]){
                        dp[i][j] = i+1;
                    }else {
                        dp[i][j] = dp[counselorIndex][j];
                    }
                }
                counselorIndex = dp[counselorIndex][ptnChars[i]];
            }
            char[] chars = haystack.toCharArray();
            int currentState = 0;
            for (int i = 0; i < chars.length; i++) {
                currentState = dp[currentState][chars[i]];
                if (currentState == ptnChars.length) return i - ptnChars.length+1;
            }
            return -1;
        }
        public static void main(String[] args) {
            String src1 = "hello", ptn1 = "ll";
            String src2 = "aaaaa", ptn2 = "bba";
            String src3 = "", ptn3 = "";
            String src4 = "mississippi", ptn4 = "issip";
            String src5 = "a", ptn5 = "a";
            String src6 = "aaacaaab", ptn6 = "aaab";
            String src7 = "aaaaaaab", ptn7 = "aaab";
            KMPSolution kmpSolution = new KMPSolution("ababc");
            Assert.assertTrue(new KMPSolution(ptn1).searchPtnInSrc(src1) == 2);
            Assert.assertTrue(new KMPSolution(ptn2).searchPtnInSrc(src2) == -1);
            Assert.assertTrue(new KMPSolution(ptn3).searchPtnInSrc(src3) == 0);
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
