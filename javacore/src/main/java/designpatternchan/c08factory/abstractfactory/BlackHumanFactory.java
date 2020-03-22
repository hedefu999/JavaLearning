package designpatternchan.c08factory.abstractfactory;

import designpatternchan.c08factory.Human;

public class BlackHumanFactory extends AbstractHumanFactory {
    @Override
    public Human createHuman() {
        return null;//new Blackman();
    }
}
