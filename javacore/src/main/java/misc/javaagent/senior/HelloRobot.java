package misc.javaagent.senior;

import java.util.concurrent.TimeUnit;

public class HelloRobot {
    public static void main(String[] args) {
        HelloRobot helloRobot = new HelloRobot();

    }
    public void sayHello(){
        System.out.println("hi, people");
        try {
            TimeUnit.SECONDS.sleep((long)(Math.random()*200));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
