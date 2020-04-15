package misc.mqdemo;

import lombok.Getter;
import lombok.Setter;

import java.text.DateFormat;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * DelayQueue延时队列有一个延时获取元素的功能，可以拿来做消息队列
 */
public class CustomerDelayQueue {
    //延迟消息队列
    private static DelayQueue delayQueue = new DelayQueue();
    //生产者
    public static void producer(){
        delayQueue.put(new MyDelay(2000,"你好，世界"));
        delayQueue.put(new MyDelay(0,"开天辟地"));
    }
    //消费者
    public static void consumer() throws InterruptedException {
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        String format = dateFormat.format(new Date());
        System.out.println("开始时间："+format);
        while (!delayQueue.isEmpty()){
            Delayed take = delayQueue.take();
            System.out.println(take.toString());
        }
        System.out.println("结束时间：" + System.currentTimeMillis());
    }

    /**
     * 自定义延迟队列
     */
    static class MyDelay implements Delayed{
        //延迟截止时间
        long delayTime = System.currentTimeMillis();
        @Getter
        @Setter
        private String message;
        //构造函数初始化 设置延迟执行时间
        public MyDelay(long delayTime, String message){
            this.delayTime = (this.delayTime + delayTime);
            this.message = message;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            long convert = unit.convert(delayTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
            return convert;
        }
        //队列里元素的排序依据
        @Override
        public int compareTo(Delayed o) {
            if (this.getDelay(TimeUnit.MILLISECONDS) > o.getDelay(TimeUnit.MILLISECONDS)){
                return 1;
            }else if (this.getDelay(TimeUnit.MILLISECONDS) < o.getDelay(TimeUnit.MILLISECONDS)){
                return -1;
            }else {
                return 0;
            }
        }

        @Override
        public String toString() {
            return this.message;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        producer();
        consumer();
    }

    /**
     * DelayQueue如何使用
     */
    static class AboutDelayQueue{
        public static void main(String[] args) throws InterruptedException {
            DelayQueue queue = new DelayQueue();
            queue.put(new Delayed() {
                @Override
                public String toString() {
                    return "test";
                }

                @Override
                public long getDelay(TimeUnit unit) {
                    return 2;
                }

                @Override
                public int compareTo(Delayed o) {
                    return 0;
                }
            });
            Delayed take = queue.take();
            System.out.println(take);
        }
    }
}
