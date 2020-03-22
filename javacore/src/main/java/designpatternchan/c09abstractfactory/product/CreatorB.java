package designpatternchan.c09abstractfactory.product;

public class CreatorB extends AbstractCreator {
    @Override
    AbstractProductA createProductA() {
        return new ProductA2();
    }

    @Override
    AbstractProductB createProductB() {
        return new ProductB2();
    }
}
