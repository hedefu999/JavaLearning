package com.ssmr.c11.a_apiaop;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Client {
    public static void main(String[] args) {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("classpath:com/ssmr/c11/spring-apiaop.xml");
        Person person = context.getBean("person",Person.class);
        person.findPerson();
    }
}
