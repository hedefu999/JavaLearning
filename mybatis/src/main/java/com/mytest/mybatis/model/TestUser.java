package com.mytest.mybatis.model;

import com.mytest.mybatis.enums.SexEnum;
import com.mytest.mybatis.enums.TestUserTypeEnum;
import org.apache.ibatis.type.Alias;

//@Alias("testUser")
public class TestUser {
    private Integer id;
    private String name;
    private TestUserTypeEnum type;
    private SexEnum sex;
    private int age;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TestUserTypeEnum getType() {
        return type;
    }

    public void setType(TestUserTypeEnum type) {
        this.type = type;
    }

    public SexEnum getSex() {
        return sex;
    }

    public void setSex(SexEnum sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "TestUser{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", sex=" + sex +
                ", age=" + age +
                '}';
    }
}
