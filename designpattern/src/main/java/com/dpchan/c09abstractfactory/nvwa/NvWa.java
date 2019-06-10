package com.dpchan.c09abstractfactory.nvwa;

public class NvWa {
    public static void main(String[] args) {
        HumanFactory maleFactory = new MaleFactory();
        HumanFactory femaleFactory = new FemaleFactory();
        maleFactory.createWhiteHuman();//生产欧洲男性
        femaleFactory.createYelloHuman();//生产亚洲女性
    }
}
