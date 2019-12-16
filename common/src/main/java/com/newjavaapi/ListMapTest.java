package com.newjavaapi;

import org.junit.Test;

import java.util.*;

public class ListMapTest {
    @Test
    public void test(){
        Map<String, List<Integer>> data = new HashMap<>();
        data.computeIfAbsent("aaa",key -> new LinkedList<>());
        data.get("aaa").add(12);
        data.get("aaa").addAll(Arrays.asList(23,24,45));
        System.out.println(data);
    }

}
