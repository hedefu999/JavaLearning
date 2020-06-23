package com.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DailyTest {
    @Data
    @AllArgsConstructor
    static class Student{
        private String name;
        private Integer age;
    }
    @Test
    public void test5(){
        int age = 10;
        Map<Student,String> map = new HashMap<>();
        map.put(new Student("jack",null),"jack");
        map.put(new Student("lucy",null),"jack");
        List<Student> students = new ArrayList<>(map.keySet());
        for (Student student : students){
            student.setAge(age++);
        }
        System.out.println(map.keySet());
    }
    @Test
    public void test34(){
        Map<String,String> map = new HashMap<>();
        map.put("12","jack");
        map.put("13","lucy");
        map.forEach((age,name)->{
            System.out.println(age+name);
        });
    }

    @Test
    public void test43() {
        //Math.ceil(x) = -Math.floor(-x)
        double basePrice = 567.887564D;
        int i = Double.valueOf(Math.ceil(basePrice)).intValue();
        System.out.println(i);

        Map<Integer,String> map = new HashMap<Integer, String>(4){{
            put(1,"jack");
        }};
    }

    @Test
    public void test56() {
        String str = "a,b,c,,"; String[] ary = str.split(",");
        System.out.println(Arrays.toString(ary));
    }


}
