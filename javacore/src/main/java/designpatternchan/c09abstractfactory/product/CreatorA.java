package designpatternchan.c09abstractfactory.product;

public class CreatorA extends AbstractCreator{
    @Override
    public AbstractProductA createProductA() {
        return new ProductA1();
    }
    @Override
    public AbstractProductB createProductB() {
        return new ProductB1();
    }
}
