package designpatternchan.c06;

public interface IComputerBook extends IBook {
    //书店类BookStore打算开卖计算机类书籍,并且计算机书籍有一个自己的接口，原接口保留，就可以通过接口扩展实现
    String getScope();
}
