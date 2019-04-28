package com.hedefu.mybatis.model;

public class Role {
    private final Long id;
    private final String roleName;
    private final String note;

    //使用建造者模式后缺少全参数构造器，mybatis进行对象映射时报
    // No constructor found in com.hedefu.mybatis.Role matching [java.lang.Long, java.lang.String, java.lang.String]
    public Role(Long id, String roleName, String note) {
        this.id = id;
        this.roleName = roleName;
        this.note = note;
    }

    public static class Builder{
        private Long id;
        private String roleName;
        private String note;
        public Builder buildId(Long id){
            this.id = id;
            return this;
        }
        public Builder buildRoleName(String roleName){
            this.roleName = roleName;
            return this;
        }
        public Builder buildNote(String note){
            this.note = note;
            return this;
        }
        public Role build(){
            return new Role(this);
        }
    }

    public Role(Builder builder) {
        this.id = builder.id;
        this.roleName = builder.roleName;
        this.note = builder.note;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", roleName='" + roleName + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
