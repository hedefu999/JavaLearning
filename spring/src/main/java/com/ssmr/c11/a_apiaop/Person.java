package com.ssmr.c11.a_apiaop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    public Person findPerson() {
        Person person = new Person(1, "JDK");
        System.out.println("findPerson 被执行");
        return person;
    }
    private Integer id;
    private String name;
}
