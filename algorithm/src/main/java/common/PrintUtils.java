package common;

public class PrintUtils {
    public static void printListNodes(ListNode l1){
        while (l1!=null){
            System.out.print(l1.val+"->");
            l1 = l1.next;
        }
        System.out.println();
    }
}
