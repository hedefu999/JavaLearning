package labuladong;

import common.TreeNode;

public class TestCasesAndUtils {
    //股票最大利润问题
    public static int[] prices0 = {3,2,5,6,10,9,7,11,14};//15:
    public static int[] prices1 = {7,1,5,3,6,4};//7:1-5,3-6
    public static int[] prices2 = {1,2,3,4};//4:1-5
    public static int[] prices3 = {7,6,4,3,1};//0
    public static int[] prices4 = {1,2,3,0,2};//3
    public static int[] prices5 = {2,1};//0
    public static int[] prices55 = {1,2};//1
    public static int[] prices6 = {2,1,4};//3
    public static int[] prices7 = {6,1,6,4,3,0,2};//7
    public static int[] prices8 = {1, 3, 2, 8, 4, 9};
    public static int[] prices9 = {1,2,4,2,5,7,2,4,9,0};

    public static TreeNode tree4FindDuplication;
    public static TreeNode tree4FindDuplicationAllZero;//逐行扫描代码是 [0,0,0,0,null,null,0,null,null,null,0]
    public static TreeNode tree4FindDuplicationPreOrder;//逐行扫描代码 [2,1,11,11,null,1]
    static {
        tree4FindDuplication = new TreeNode(1);
        TreeNode node2 = tree4FindDuplication.left = new TreeNode(2);
        TreeNode node3 = tree4FindDuplication.right = new TreeNode(3);
        node2.left = new TreeNode(4);
        TreeNode node21 = node3.left = new TreeNode(2);
        node3.right = new TreeNode(4);
        node21.left = new TreeNode(4);

        tree4FindDuplicationAllZero = new TreeNode(0);
        TreeNode nodeZero1 = tree4FindDuplicationAllZero.left = new TreeNode(0);
        TreeNode nodeZero2 = tree4FindDuplicationAllZero.right = new TreeNode(0);
        nodeZero1.left = new TreeNode(0);
        TreeNode nodeZero3 = nodeZero2.right = new TreeNode(0);
        nodeZero3.right = new TreeNode(0);

        tree4FindDuplicationPreOrder = new TreeNode(2);
        TreeNode nodePre1 = tree4FindDuplicationPreOrder.left = new TreeNode(1);
        TreeNode nodePre2 = tree4FindDuplicationPreOrder.right = new TreeNode(11);
        nodePre1.left = new TreeNode(11);
        nodePre2.left = new TreeNode(1);
    }
    static {}

    static void printMatrix(int[][] input){}

    //纵向x-i轴，向下递增，横向y-j轴，向右递增，打印二维数组
    static void printBoolMatrix(boolean[][] input, int itemlength){
        System.out.print("  ");
        for (int i = 0; input[0] != null && i < input[0].length; i++) {
            int spaces = i==0?1:(int) Math.log10(i);
            int before = (itemlength - (spaces+1))/2;
            int after = itemlength - before - spaces - 1;
            for (int j = 0; j < before; j++) {
                System.out.print(" ");
            }
            System.out.print(i);
            for (int j = 0; j < after; j++) {
                System.out.print(" ");
            }
        }
        System.out.println();
        for (int i = 0; i < input.length; i++) {
            System.out.print(i+" ");
            for (int j = 0; j < input[i].length; j++) {
                boolean b = input[i][j];
                if (b){
                    System.out.print(b+"  ");
                }else {
                    System.out.print(b+" ");
                }

            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        boolean[][] input = {{true,  true,  true,true},
                             {false, true,  false,true},
                             {true,  false, false,true}};
        System.out.println(input.length);
        printBoolMatrix(input,6);
    }
}