package com.ssmr.c11.d_multipointcut;

import org.springframework.stereotype.Service;

@Service
public class MultiAspectsImpl implements MultiAspects {
    @Override
    public void testMultiAspects() {
        System.out.println("testMultiAspects...");
    }
}
