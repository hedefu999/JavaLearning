package com.ssmr.txpractice;

import org.junit.Test;

import java.time.LocalDateTime;

public class CommonTest {
    @Test
    public void testDateTime(){
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now.getSecond());
        System.out.println(now.getNano());
    }
}
