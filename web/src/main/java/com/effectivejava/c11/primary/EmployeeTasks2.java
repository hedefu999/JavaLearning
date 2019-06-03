package com.effectivejava.c11.primary;

import lombok.Data;

import java.io.Serializable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
@Data
public class EmployeeTasks2 implements Serializable {
  private static final long serialVersionUID = -6680357509935362657L;
  private String name;
  private String password;
  private Integer age;
  private Task task;
  //定制序列化内容，直接存储对象图占用资源性能
  private void writeObject(ObjectOutputStream oos) throws IOException {
    ObjectOutputStream.PutField fieldMap = oos.putFields();
    fieldMap.put("password","PassWordUtil.encrypt(password)");
    fieldMap.put("name",name);
    fieldMap.put("age",age);
    //默认的序列化会遍历对象图，把Task单向链表全部写入磁盘
    //只保存链表头结点的code
    fieldMap.put("task",new Task(task.getCode(),null));
    oos.writeFields();
  }

  private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
    ObjectInputStream.GetField fields = ois.readFields();
    String pass = fields.get("password","").toString();
    System.out.println("要解密的密码：" + pass);
    password = pass;
    name = fields.get("name"," ").toString();
    System.out.println(name);
    age = (Integer) fields.get("age","");
    task = (Task) fields.get("task",null);
  }


}
