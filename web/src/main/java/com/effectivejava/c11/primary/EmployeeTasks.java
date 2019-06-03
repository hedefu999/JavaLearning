package com.effectivejava.c11.primary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeTasks implements Externalizable {
  private static final long serialVersionUID = 5549238357317297964L;
  private String name;
  private Integer age;
  //带有对象域Task需要实现Serializable，并且还是个链表的结构
  private Task task;

  @Override
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeUTF(name);
    out.writeInt(age);
    out.writeUTF(task.getCode());
  }

  @Override
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    //ObjectInput里没有defaultReadObject()方法，但这个方法是effective java里推荐的动作
    // Externalizable较Serializable更少被人所知，虽然是Serializable的子接口
    name = in.readUTF();
    age = in.readInt();
    task = new Task(in.readUTF(),null);
  }
}
