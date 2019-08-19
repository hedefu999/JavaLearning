package com.redis;

import java.io.Serializable;

public class Role implements Serializable {
    private static final long serialVersionUID = -5254950692374222007L;

    private Integer id;
    private String name;
    private Integer age;
    private String note;

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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", note='" + note + '\'' +
                '}';
    }
}
