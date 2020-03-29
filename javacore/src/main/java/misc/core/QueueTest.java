package misc.core;

import org.junit.Test;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class QueueTest {
    @Test
    public void test5(){
        //LinkedList实现了Queue接口
        Queue<String> queue = new LinkedList<>();
        //Queue接口就定义了 add offer remove poll element peek 方法
        //只允许在表的前端进行删除，在后端进行添加操作（队列）
        //add()和remove()方法在失败的时候会抛出异常(不推荐)
        queue.offer("jack");
        queue.offer("lucy");
        queue.offer("daniel");
        Iterator<String> iterator = queue.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }
        System.out.println(queue.poll()+";");//poll拿出第一个元素 jack，还剩2个
        for (String item : queue){
            System.out.println(item);
        }
        System.out.println(queue.element());//返回第一个元素
        System.out.println(queue.size());//数量不变
        System.out.println(queue.peek());//返回第一个元素
        System.out.println(queue.size());//数量不变
        /**
         * offer，add 区别：
         *
         * 一些队列有大小限制，因此如果想在一个满的队列中加入一个新项，多出的项就会被拒绝。
         *
         * 这时新的 offer 方法就可以起作用了。它不是对调用 add() 方法抛出一个 unchecked 异常，而只是得到由 offer() 返回的 false。
         *
         * poll，remove 区别：
         *
         * remove() 和 poll() 方法都是从队列中删除第一个元素。remove() 的行为与 Collection 接口的版本相似， 但是新的 poll() 方法在用空集合调用时不是抛出异常，只是返回 null。因此新的方法更适合容易出现异常条件的情况。
         *
         * peek，element区别：
         *
         * element() 和 peek() 用于在队列的头部查询元素。与 remove() 方法类似，在队列为空时， element() 抛出一个异常，而 peek() 返回 null。
         */
    }
}
