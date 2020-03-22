package designpatternchan.c11builder;

import java.util.ArrayList;

public abstract class CarBuilder {
    //设置组装顺序
    public abstract void setSequence(ArrayList<String> sequence);
    //按照设置的顺序得到车辆模型
    public abstract CarModel getCarModel();
}
