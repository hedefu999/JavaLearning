package com.effectivejava.c11;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
//带有合理的序列化形式的类 StringList
public final class StringList implements Serializable {
  private static final long serialVersionUID = 115669143047559778L;
  private transient int size = 0;
  private transient Entry head = null;
  //不再序列化这个内部类
  private static class Entry{
    String data;
    Entry next;
    Entry previous;
  }
  //为链表添加内容，内部逻辑省略
  public final void add(String s){}
  /**
   * 序列化StringList实例({@code StringList})
   * @serialData 双向链表的大小({@code int}) + 每个结点的数据域({@code String})
   * @param oos
   * @throws IOException
   */
  //虽然此方法是私有的，但仍值得添加文档注释，告知Javadoc工具，显示此类的序列化形式
  private void writeObject(ObjectOutputStream oos) throws IOException {
    //虽然StringList的所有域都是瞬态的，但仍推荐调用一次defaultWriteObject，改善序列化形式
    //这样得到的序列化形式允许在以后的发行版本中增加非transient的实例域，保持向前或向后兼容性：
    //先前序列化的数据在新代码中进行反序列化时，新代码中增加的域会被忽略。此时如果旧版本的代码没有调用defaultWriteObject，在新代码上反序列化会失败StreamCorruptedException
    oos.defaultWriteObject();
    oos.writeInt(size);
    //有次序地写入所有元素
    for (Entry e = head; e!=null; e=e.next){
      oos.writeObject(e.data);
    }
  }
  public void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
    ois.defaultReadObject();
    int numElements = ois.readInt();
    //读入所有元素，插入到列表中
    for(int i=0;i<numElements;i++){
      add((String) ois.readObject());
    }
  }

}