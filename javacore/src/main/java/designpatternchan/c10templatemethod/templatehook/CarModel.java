package designpatternchan.c10templatemethod.templatehook;

public abstract class CarModel {
    public abstract void start();
    public abstract void stop();
    public abstract void alarm();
    public abstract void engineBoom();
    //钩子方法,它的返回值将影响模板方法的执行结果
    protected boolean isAlarm(){
        return true;
    }
    //在模板方法中按照一定的规则和顺序调用基本方法
    public void run(){
        //先发动汽车
        this.start();

        //引擎开始轰鸣
        this.engineBoom();

        //然后就开始跑了，跑的过程中遇到一条狗挡路，就按喇叭
        this.alarm();

        //到达目的地就停车
        this.stop();
    }
}
