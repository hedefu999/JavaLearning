package designpatternchan.c06;

import java.math.BigDecimal;

public interface IBook {
    //可以对价格进行折扣并返回
    BigDecimal getCurrentPrice();
    //如果折扣的业务发生变化，可以在这里新增接口
    //getDiscountForEnterprise();
    //但这样NovelBook也需要修改，因为接口是比较重要的抽象，应当保持稳定
    //按照开闭原则，应当新写IBook实现类，覆写getCurrentPrice()实现老代码不动，只新增类
}
