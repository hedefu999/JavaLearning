package com.redis._08springredis;

import java.io.Serializable;

/**
 * POJO类在使用缓存时必须实现serializable
 * 否则报Failed to serialize object using DefaultSerializer; DefaultSerializer requires a Serializable payload but received an object of type
 */
public class User implements Serializable {
    private static final long serialVersionUID = 4159142917796047025L;
    private Integer id;
    private String phone;
    private String name;
    private Integer age;

    public User() {
    }

    public User(Integer id,String phone, String name, Integer age) {
        this(phone, name, age);
        this.id = id;
    }
    public User(String phone, String name, Integer age) {
        this.phone = phone;
        this.name = name;
        this.age = age;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
