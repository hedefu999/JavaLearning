package designpatternchan.c09abstractfactory.nvwa;

public class FemaleFactory implements HumanFactory {
    @Override
    public Human createYelloHuman() {
        return new FemaleYellowMan();
    }

    @Override
    public Human createWhiteHuman() {
        return new FemaleWhiteMan();
    }
}
