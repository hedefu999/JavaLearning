package com.redis.mqimplementation;

import redis.clients.jedis.Jedis;

public class RedisListSolution {
    public static void main(String[] args) throws InterruptedException {
        // 启动一个线程作为消费者
        Thread thread = new Thread(() -> consumer2());
        thread.setName("RedisListMqSolution");thread.start();
        // 生产者
        producer();
        Thread.sleep(1000);
        producer();
    }
    public static void producer() {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        // 推送消息
        jedis.lpush("mq", "Hello, List.");
    }
    public static void consumer() {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        // 消费消息
        while (true) {
            // 获取消息
            String msg = jedis.rpop("mq");
            if (msg != null) {
                // 接收到了消息
                System.out.println("接收到消息：" + msg);
            }
        }
    }
    /*
     *【改进】
     以上消费者的实现是通过 while 无限循环来获取消息，但如果消息的空闲时间比较长，一直没有新任务，
     而 while 循环不会因此停止，它会一直执行循环的动作，这样就会白白浪费了系统的资源。
     可以借助 Redis 中的阻塞读来替代 rpop 的方法
     */
    public static void consumer2(){
        Jedis jedis = new Jedis("127.0.0.1",6379);
        while (true){
            //使用阻塞读 brpop
            for (String item : jedis.brpop(0,"mq")){
                System.out.println(item);
            }
        }
    }
}

