package effectiveJava.c10;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class StopThread3 {
    private static volatile boolean stopRequested;

    public static void main(String[] args) throws InterruptedException {
        Thread bgThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (!stopRequested){
                    i++;
                    System.out.println(i);
                }
            }
        });
        bgThread.start();
        TimeUnit.SECONDS.sleep(1);
        stopRequested = true;
    }
    public static long time(Executor executor, int concurrency,final Runnable action) throws InterruptedException {
        final CountDownLatch ready = new CountDownLatch(concurrency);
        final CountDownLatch start = new CountDownLatch(1);
        final CountDownLatch done = new CountDownLatch(concurrency);
        for (int i = 0; i < concurrency; i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    ready.countDown();//tell timer we're ready
                    try {
                        start.wait();//wait till peers are ready
                        action.run();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                    } finally {
                        done.countDown();//tell timer we're done
                    }
                }
            });
        }
        ready.await();//wait for all workers to be ready
        long startNanos = System.nanoTime();
        start.countDown();//and they are off
        done.await();//wait for all workers to finish
        return 0;
    }
}
