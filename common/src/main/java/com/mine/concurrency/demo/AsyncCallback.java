package com.mine.concurrency.demo;

/**
 * 使用同步锁解决异步回调的等待问题
 */
public class AsyncCallback {
    public static void main(String[] args) {
        AckHandler ackHandler = new AckHandler();
        long current = System.currentTimeMillis();
        //不使用匿名内部类，专门传入接口实现类的实例
        Processor.process(ackHandler);
        synchronized(ackHandler){
            if (ackHandler.getAck() == null){
                try {
                    ackHandler.wait(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        //初级码农的写法
        //for (int i = 0; i < 10 && (ackHandler.getAck()) == null; i++) {
        //    try {
        //        Thread.sleep(1000);
        //    } catch (InterruptedException e) {
        //        e.printStackTrace();
        //    }
        //}
        //TODO 线程里有一个定时任务，思路与上面的初级写法相同
        System.out.printf("等待时长：%d,获取到的结果：%s。",System.currentTimeMillis() - current, ackHandler.getAck());
    }
}
/**
 * 不可控外部
 */
class Processor{
    public static void process(IAck iAck){
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        iAck.callback("finished");
    }
}
/**
 * 回调接口，可以是双方共同定义的接口
 */
interface IAck{
    void callback(String result);
}
/**
 * 自己的回调接口实现类
 */
class AckHandler implements IAck{
    //TODO volatile使用的必要性
    private volatile String ack;
    public String getAck() {
        return ack;
    }
    public void setAck(String ack) {
        this.ack = ack;
    }
    //缺少synchronized关键字会报 java.lang.IllegalMonitorStateException
    @Override
    public synchronized void callback(String result){
        setAck(result);
        this.notifyAll();
    }
}
