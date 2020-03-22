package designpatternchan.c03;

import designpatternchan.c03.DIP.Fararry;
import designpatternchan.c03.DIP.IDriver;
import designpatternchan.c03.nonDIP.Benz;
import designpatternchan.c03.nonDIP.Driver;
import org.junit.Test;

public class Client {
    @Test
    public void testBenzDriver(){
        Driver zhangsan = new Driver();
        Benz benz = new Benz();
        //只能传Benz不能传Farrary，紧耦合不能适应变化的业务场景
        zhangsan.drive(benz);
    }
    @Test
    public void testDIPCarDriver(){
        //Client属于高层的业务逻辑，对于低层模块的依赖都建立在抽象（接口）上
        IDriver driver = new designpatternchan.c03.DIP.Driver();
        Fararry fararry = new Fararry();
        driver.drive(fararry);
    }
}
