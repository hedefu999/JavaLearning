package com.ssmr.mine.exceptionaop;

import org.springframework.stereotype.Service;

@Service("iTest")
public class TestImpl implements ITest {
    @Override
    public int process(String name) {
        System.out.println(name);
        throw new ServerException("hello world");
    }
}
