package com.newjavaapi;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class JDK8CollectionApiTest {
    private final Logger log = LoggerFactory.getLogger(JDK8CollectionApiTest.class);
    @Test
    public void test5(){
        Map<String,String> map = new HashMap<>();
        String name = "jack";
        map.computeIfAbsent(name,s ->{
            log.info("s = {}",s);
            return getAddress(name,12);});
        //map.computeIfAbsent(name,s ->getAddress(name,12));
        log.info(map.toString());

    }
    private String getAddress(String name,Integer num){
        switch (name){
            case "jack":
                return "shanghai";
            case "lucy":
                return "jiangsu";
                default:
                    return "zhejiang";
        }
    }
}
