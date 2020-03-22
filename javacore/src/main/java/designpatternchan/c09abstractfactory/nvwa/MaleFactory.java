package designpatternchan.c09abstractfactory.nvwa;

public class MaleFactory implements HumanFactory {
    @Override
    public Human createYelloHuman() {
        return new MaleYellowMan();
    }

    @Override
    public Human createWhiteHuman() {
        return new MaleWhiteMan();
    }
}
