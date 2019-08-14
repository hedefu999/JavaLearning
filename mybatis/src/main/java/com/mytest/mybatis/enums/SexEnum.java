package com.mytest.mybatis.enums;

public enum SexEnum {
    FEMALE(-1,"女"),MALE(1,"男"),TRANSMALE(2,"法定男"),TRANSFEMALE(-2,"法定女");
    private Integer code;
    private String name;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    SexEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static SexEnum getByCode(Integer code){
        for (SexEnum sexEnum : SexEnum.values()){
            if (sexEnum.code.equals(code)){
                return sexEnum;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "code="+ code +",name="+name;
    }
}
