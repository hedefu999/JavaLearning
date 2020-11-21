package labuladong;

import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class _05DataStructure {
    /*
     # 380 常数时间插入、删除和获取随机元素

     ## 分析
     数组可以实现O(1)地获取随机元素（等概率获取元素）但不能O(1)地删除元素
     hash表可以O(1)地获取、删除元素，但不能O(1)地等概率获取元素
     将上述特性结合：
     使用数组存储元素的值
     使用HashMap存储元素的值与数组索引的映射关系

     这样添加元素时在array末尾添加（可能要扩容），并在map里加映射
     随机取元素时就计算一个随机Index 直接到array里取
     删除元素时map去掉一个记录，数组可能会留下大量中间空隙，所以每次删除时把末尾的元素挪过来

     */
    static class RandomizedSet {
        private int init_size = 16;
        int[] valArray;
        int size = 0;
        Map<Integer, Integer> itemValueIndexMap;

        public RandomizedSet() {
            valArray = new int[init_size];
            itemValueIndexMap = new HashMap<>();
        }

        public boolean insert(int val) {
            if (itemValueIndexMap.containsKey(val)) return false;
            //将元素添加到数组末尾
            itemValueIndexMap.put(val, size);
            if (size >= valArray.length){
                int[] tempArray = new int[valArray.length*2];
                System.arraycopy(valArray,0,tempArray,0,valArray.length);
                valArray = tempArray;
            }
            valArray[size] = val;
            size++;
            return true;
        }

        public boolean remove(int val) {
            Integer index = itemValueIndexMap.get(val);
            if (index == null) return false;
            itemValueIndexMap.remove(val);
            if (index == size-1){ //移除的恰好是最后一个元素，同时只剩一个元素时也能兼顾
                size --;
                return true;
            }
            //将最后一个元素搬过来
            int lastVal = valArray[size - 1];
            valArray[index] = lastVal;
            itemValueIndexMap.put(lastVal, index);
            size--;
            return true;
        }

        public int getRandom() {
            int randomIndex = (int) (size * Math.random());
            return valArray[randomIndex];
        }

        public static void main(String[] args) {
            RandomizedSet rdSet = new RandomizedSet();
            /*

            test case 1:
            rdSet.insert(1);
            rdSet.remove(2);
            rdSet.insert(2);
            System.out.println(rdSet.getRandom());
            rdSet.remove(1);
            rdSet.insert(2);
            System.out.println(rdSet.getRandom());

            test case 2:
             */
            rdSet.remove(0);
            rdSet.remove(0);
            rdSet.insert(0);
            rdSet.getRandom();
            rdSet.remove(0);
            System.out.println(rdSet.insert(0));

        }
    }

    /*
     # 710 黑名单外的随机数
        给定一个包含 [0，n) 中独特的整数的黑名单 B，写一个函数从 [0，n) 中返回一个不在 B 中的随机整数。

     思路： N-B个数如何映射到N个
         0 1 2 [3] 4 [5] [6] 7 8
         0 1 2     3         4 5
     */
    static class RandomInBlackList{
        //维护一个白名单，直接从map中取。会超时，在N很大时
        static class Solution {
            private int N;
            private int realSize;
            Map<Integer,Integer> map = new HashMap<>();
            public Solution(int N, int[] blacklist) {
                this.N = N;
                this.realSize = N-blacklist.length;
                //int[]转ArrayList<Integer>咋就这么难,主要为了用一下
                //List<Integer> blackCollect = Arrays.stream(blacklist).boxed().collect(Collectors.toList());
                //Arrays.stream(blacklist).collect(ArrayList::new, (collect,item)-> collect.add(item) ,ArrayList::addAll);
                Map<Integer,Object> blackMap = Arrays.stream(blacklist).boxed().collect(HashMap::new,(map,item) -> map.put(item,null), HashMap::putAll);
                int index = 0;
                for (int i = 0; i < N; i++) {
                    if (blackMap.containsKey(i)) continue;
                    map.put(index++,i);
                }
            }
            public int pick() {
                int midIndex = (int) (Math.random() * realSize);
                return map.get(midIndex);
            }
        }
        /*
        二分查找

        示例分析：
               3     4   4       6   6          -- 某个B[i]前面有多少个W[i]: B-b
               0     1   2       3   4          -- 黑名单index - b
        0 1 2 [3] 4 [5] [6] 7 8 [9] [10] 11 12  -- 黑名单B和白名单W,原始数据O index - k
        0 1 2     3         4 5           6  7  -- 白名单index - w

        w = 3, w + 1 找<=3的最大index
        w = 4, w + 3
        w = 5, w + 3
        w = 6, w + 5
        可以总结规律，W[w] = w + (B-b中所有小于等于w的数量)

         */
        static class Solution2{
            private int N;
            //纯数组操作，性能爆表
            private int[] sortedBs;
            //private int[] sortedWs;
            public Solution2(int N, int[] blacklist){ //N=9,B={3,5,6},W={0,1,2,4,7,8}
                Arrays.sort(blacklist);
                sortedBs = blacklist;
                //int wlength = N - blacklist.length;
                this.N = N;
            }
            public int pick(){
                int w = (int) (Math.random() * (N - sortedBs.length));//白名单中的索引，随机到的数必须是白名单中的索引
                //知道w如何求值W[w],这个： W[w]在黑名单为空时 W[w] = w; 黑名单不为空时 W[w]>=w, 因为有些元素要跳过去；黑名单不为空时也有可能W[w]=w,发生在B(min)>w时
                if (sortedBs.length == 0 || w < sortedBs[0]) return w;
                //下面是比较难的情形
                //可以将sortedBs转成 B-b，注意多次pick会重复使用sortedBs
                //在寻找第一个大于w的元素的sortedBs[i]-i的i值
                int b = binFindFirstGreater(sortedBs, w);
                while (b < sortedBs.length && sortedBs[b]-b == w){
                    b++;
                }
                return w+b;
                //如何优雅地使用二分查找法寻找第一个大于w的元素（w在input中可能是重复的）
            }
            //答案在这里，使用二分查找法寻找第一个大于key的元素，LeCo显示性能提升很多
            static int findFirstGeaterIndex(int[] input, int key){
                int low = 0;
                int high = input.length - 1;
                while (low < high){
                    int internal = (low + high + 1)/2;
                    if (input[internal] > key) high = internal - 1;
                    else low = internal;
                }//最后low总是 = high
                return low + 1;
            }
        }

        static int simpleSearchFirstGreat(int[] input, int key){
            for (int i = 0; i < input.length; i++) {
                if (key < input[i] - i){
                    return i;
                }
            }
            return input.length;
        }
        static int binFindFirstGreater(int[] input, int key){
            int low = 0;
            int high = input.length - 1;
            int mid = 0;
            while (low <= high){
                mid = (low + high)/2;
                int curr = input[mid] - mid;
                if (curr < key){
                    low = mid + 1;
                }else if (curr > key){
                    high = mid - 1;
                }else {
                    return mid;
                }
            }
            return low;
        }//对返回结果再进行+++找到第一个较大的，而不是相等的

        public static void main(String[] args) {
            int[] black = {}; //black = {2,3}; array initializer is not allowed here
            int N = 100;

            //black = new int[]{};
            //N=1;

            //black = new int[]{};
            //N=2;

            //black = new int[]{1};
            //N=3;

            black = new int[]{3,5,6};
            N = 9;

            Solution2 solution = new Solution2(N, black);

            System.out.println(solution.pick());
            System.out.println(solution.pick());
            System.out.println(solution.pick());
            System.out.println(solution.pick());
        }


        //二分查找法写哭了也不对。。。。
        static void binSearch1(){
            int[] sortedBs = {2,4,6,8,12,13,66};
            int w = 7;
            int low = 0;
            int high = sortedBs.length-1;
            int mid = 0;
            while (low < high){ //如果这里把=号带上，会发现low始终是3
                mid = (low + high)/2;
                if (sortedBs[mid] < w){
                    low = mid + 1;
                }else if (sortedBs[mid] > w){
                    high = mid - 1;
                }else {
                    low = high = mid;
                }
            }
            //随着后部元素的减少，收集low/mid/high的情况，如何能得到正确的2 ???
            //2,4,6,8,12,13,66  2,1,2
            //2,4,6,8,12,13  3,4,3
            //2,4,6,8,12  3,3,2
            //2,4,6,8  3,2,3
            System.out.println(low);
        }

        //看看JVM是怎么写二分查找的
        private static int binarySearch0(int[] a, int fromIndex, int toIndex, int key) {
            int low = fromIndex;
            int high = toIndex - 1;
            while (low <= high) { //多加一个=号可以让low = high时让mid与这两个数保持一致而不是上一步计算的值
                int mid = (low + high) >>> 1;//除以2取整
                if (a[mid] < key)
                    low = mid + 1;
                else if (a[mid] > key)
                    high = mid - 1;
                else
                    return mid; //找到元素
            }
            return -(low + 1);  //找不到元素时会发现low始终是大于key的最小index
        }

        /*
        还有一种技巧：
               0     1   2       3   4          -- 黑名单index - b
        0 1 2 [3] 4 [5] [6] 7 8 [9] [10] 11 12  -- 黑名单B和白名单W,原始数据O index - k
        0 1 2     3         4 5           6  7  -- 白名单index - w
        N = 12，黑名单长度5，白名单长度8，可以发现8之前的黑名单数量与8之后的白名单数量相等
        这样把 >= 8的白名单映射到黑名单，这样就可以直接对[0,8)做随机取数了
        这样就只需要存储一些修改过的映射到map里，进一步节省空间，因为其他没改过的就是返回key，上述为例，map的内容就是[<3,8><5,11><6,12>]
         */
        static class Solution3{
            static void lastSolution(int n, int[] black){
                int wl = n - black.length;
                Map<Integer,Integer> map = new HashMap<>();
                Set<Integer> w = new HashSet<>();
                //收集后部的白名单数字
                for (int i = wl; i < n; i++) {
                    w.add(i);
                }
                for (int blackItem : black){
                    w.remove(blackItem);
                }
                Iterator<Integer> iterator = w.iterator();
                for (int blackItem : black){
                    if(blackItem < wl){
                        map.put(blackItem,iterator.next());
                    }
                }
                return;
            }//自己又实现了一个，结果超时了，因为里面的hashset太大了，官方写法还是不可替代的。。。我崩溃了
            static void lastSolution2(int N, int[] blacklist){
                int wl = N - blacklist.length;
                int left = wl-1, right = wl;
                Map<Integer,Integer> map = new HashMap<>();
                Set<Integer> blackSet = new HashSet<>();
                for (int item : blacklist) {
                    blackSet.add(item);
                }

                while (left >= 0 && right < N){
                    while (left >= 0 && !blackSet.contains(left)){
                        left--;
                    }
                    while (right < N && blackSet.contains(right)){
                        right ++;
                    }
                    //这里会把-1，11两个极端的值放进去，但不影响功能
                    map.put(left--,right++);
                }
                return;
            }

            public static void main(String[] args) {
                int[] a = {3,4,4};//2 2
                int[] b = {3,4,7,9,10};//11
                int[] c = {2,3,4,5,7,9};//22
                int[] d = {2,3,5,7,10};//11
                int[] e = {1,2,3};//22

                int[] b1 = {3,5,6};//9 5-7,3-8
                int[] b2 = {3,5,6,9,10};//11 5-7,3-8
                int[] b3 = {3,5,6,9,10};//13 3-12,5-11,6-8

                //lastSolution2(9,b1);
                lastSolution2(11,b2);
                //lastSolution2(13,b3);

                //lastSolution(9,b1);
                //lastSolution(11,b2);
                //lastSolution(13,b3);


                //System.out.println(whatsthis(a,4));
            }
            //最后的解法性能最好
            static class Solution {
                Map<Integer,Integer> map;
                int wl;
            public Solution(int N, int[] blacklist) {
                //this.wl = N - blacklist.length;
                //this.map = new HashMap<>();
                //Set<Integer> w = new HashSet<>();
                ////收集后部的白名单数字
                //for (int i = this.wl; i < N; i++) {
                //    w.add(i);
                //}
                //for (int blackItem : blacklist){
                //    w.remove(blackItem);
                //}
                //Iterator<Integer> iterator = w.iterator();
                //for (int blackItem : blacklist){
                //    if(blackItem < this.wl){
                //        this.map.put(blackItem,iterator.next());
                //    }
                //}
                this.wl = N - blacklist.length;
                int left = this.wl-1, right = this.wl;
                this.map = new HashMap<>();
                Set<Integer> blackSet = new HashSet<>();
                for (int item : blacklist) {
                    blackSet.add(item);
                }

                while (left >= 0 && right < N){
                    while (left >= 0 && !blackSet.contains(left)){
                        left--;
                    }
                    while (right < N && blackSet.contains(right)){
                        right ++;
                    }
                    //这里会把-1，11两个极端的值放进去，但不影响功能
                    this.map.put(left--,right++);
                }
            }

                public int pick(){
                    int key = (int) (Math.random() * this.wl);
                    if (map.containsKey(key))
                        return map.get(key);
                    else
                        return key;
                }

            }
        }

    }

    /*
    水塘抽样算法？？
     */

}
