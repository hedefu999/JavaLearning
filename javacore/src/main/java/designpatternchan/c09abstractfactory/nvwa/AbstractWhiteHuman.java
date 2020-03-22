package designpatternchan.c09abstractfactory.nvwa;

public abstract class AbstractWhiteHuman implements Human {
    @Override
    public void getColor() {
        System.out.println("white");
    }

    @Override
    public void talk() {
        System.out.println("hello");
    }
}
