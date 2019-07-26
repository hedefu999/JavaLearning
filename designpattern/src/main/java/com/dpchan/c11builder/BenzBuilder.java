package com.dpchan.c11builder;

import com.dpchan.c03.nonDIP.Benz;

import java.util.ArrayList;
import java.util.List;

public class BenzBuilder extends CarBuilder {
    private BenzModel benz = new BenzModel();
    @Override
    public void setSequence(ArrayList<String> sequence) {
        this.benz.setSequence(sequence);
    }

    @Override
    public CarModel getCarModel() {
        return this.benz;
    }
}
