package com.concurrency.starter;

//1.7.3 能停止的线程--异常法
public class StopByExpThread extends Thread{
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            if (this.isInterrupted()){
                System.out.println("已经是中断状态，即将退出");
                break;
            }

            System.out.println("i = "+i);
        }
    }

}