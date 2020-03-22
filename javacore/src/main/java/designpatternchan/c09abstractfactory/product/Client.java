package designpatternchan.c09abstractfactory.product;

public class Client {
    public static void main(String[] args) {
        AbstractCreator creator1 = new CreatorA();
        AbstractCreator creator2 = new CreatorA();
        AbstractProductA a1 =  creator1.createProductA();
        AbstractProductA a2 = creator2.createProductA();
        AbstractProductB b1 = creator1.createProductB();
        AbstractProductB b2 = creator2.createProductB();
    }
}
