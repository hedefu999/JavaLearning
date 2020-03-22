package effectiveJava.c10;

import java.util.concurrent.TimeUnit;

public class StopThread2 {
    private static boolean stopRequested;
    private static synchronized void requestStop(){
        stopRequested = true;
    }
    private static synchronized boolean stopRequested(){
        return stopRequested;
    }
    public static void main(String[] args) throws InterruptedException {
        Thread bgThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (!stopRequested()) {
                    i++;
                }
            }
        });
        bgThread.start();
        TimeUnit.SECONDS.sleep(1);
        requestStop();
    }
}
