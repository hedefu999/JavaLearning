package com.dpchan.c08factory.abstractfactory;

import com.dpchan.c08factory.Human;

public class BlackHumanFactory extends AbstractHumanFactory {
    @Override
    public Human createHuman() {
        return null;//new Blackman();
    }
}
