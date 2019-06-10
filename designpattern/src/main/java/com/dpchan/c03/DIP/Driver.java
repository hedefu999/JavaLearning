package com.dpchan.c03.DIP;

public class Driver implements IDriver {
    @Override
    public void drive(ICar car) {
        car.run();
    }
}
