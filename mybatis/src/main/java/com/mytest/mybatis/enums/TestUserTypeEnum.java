package com.mytest.mybatis.enums;

public enum TestUserTypeEnum {
    HIGH("H","高级用户"),MIDDLE("M","中级用户"),PREMIUM("P","付费用户");
    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    TestUserTypeEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return "TestUserTypeEnum{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
