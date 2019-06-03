package com.effectivejava.c11.primary;

import org.junit.Test;

import java.io.*;

public class SerializeTest {
  private final String RELATIVE_PATH = "src/main/java/com/effectivejava/c11/primary/";
  private final Task TASK_LIST = new Task("soda",new Task("wash",new Task("clean",null)));
  public static void serializeObject(Object object,String fileName){
    try{
      //确定相对路径的根目录的方法：new File("").getCanonicalPath(); getPath会返回File创建时传入的路径
      //IDEA配置代理后JVM通过代理连接，相对路径会发生变化，相差一个web
      File dir = new File("src/main/java/com/effectivejava/c11/primary");
      if (!dir.exists()){
        throw new RuntimeException("目录不存在");//EmployeeSerialization.data
      }
      //getPath获取的是相对路径
      File file = new File(dir.getPath()+"/"+fileName);
      FileOutputStream fileOut = new FileOutputStream(file);
      ObjectOutputStream out = new ObjectOutputStream(fileOut);
      out.writeObject(object);
      out.close();
      fileOut.close();
      System.out.println("Serialization finished");
    }catch(IOException e){
      e.printStackTrace();
    }
  }

  public static <T> T deserializeObject(String relativePath,Class<T> clazz){
    T object = null;
    try {
      String absolutePath = new File("").getCanonicalPath()+"/"+relativePath;
      File serialData = new File(absolutePath);
      object = clazz.newInstance();
      FileInputStream fis = new FileInputStream(serialData);
      ObjectInputStream ois = new ObjectInputStream(fis);
      object = (T) ois.readObject();
      ois.close();
      fis.close();
    }catch (Exception e){
      System.out.println(e.getMessage());
    }
    return object;
  }

  @Test
  public void testSimpleEmp() throws IOException {
    SimpleEmployee employee = new SimpleEmployee("jack",12);
    employee.setName("jack");
    employee.setAge(12);
    serializeObject(employee,"SimpleEmployeeSerialData");
    String relativePath = "src/main/java/com/effectivejava/c11/primary/SimpleEmployeeSerialData";
    SimpleEmployee emp = deserializeObject(relativePath,SimpleEmployee.class);
    System.out.println(emp == employee); //false
    System.out.println(emp.hashCode() == employee.hashCode());//true
  }
  @Test
  public void serializeSimEmp(){
    SimpleEmployee employee = new SimpleEmployee("jack",12);
    employee.setName("jack");
    employee.setAge(12);
    serializeObject(employee,"SimEmpSerialData");
  }
  @Test
  public void deserializeSimEmp(){
    //在反序列化时才添加serialVersionUID，可以发现 JVM自动生成的UID与IDEA生成的不一致
    //com.effectivejava.c11.primary.SimpleEmployee; local class incompatible:
    // stream classdesc serialVersionUID = 4193275551286636731, local class serialVersionUID = -3187784505501326206
    //此时对象的各字段为空
    //反序列化到另一个包里的SimpleEmployee会报ClassCastException
    String relativePath = "src/main/java/com/effectivejava/c11/primary/SimEmpSerialData";
    SimpleEmployee emp = deserializeObject(relativePath,SimpleEmployee.class);
    System.out.println(emp);
  }
  @Test
  public void serializeSubSimEmp(){
    SubSimpleEmployee sse = new SubSimpleEmployee();
    sse.setName("lucy");
    sse.setAge(13);
    sse.setAddress("sh");
    serializeObject(sse,"SubSimEmp");
    String relativePath = "src/main/java/com/effectivejava/c11/primary/SubSimEmp";
    SubSimpleEmployee sse2 = deserializeObject(relativePath,SubSimpleEmployee.class);
    System.out.println(sse2);
  }

  /**
   * 在ObjectStreamClass的构造方法里会通过泛型寻找序列化类的writeObject readObject readResolve writeReplace readObjectNoData方法
   * readResolve方法在readObject方法之后被调用，且会覆盖先前的操作，readResolve在单例模式里可以防止第二个对象被创建出来（实际上是立即丢弃被回收了）
   * Externalizable extends Serializable,并且定义了分别对应writeObject和readObject的writeExternal和readExternal方法
   */

  @Test
  public void testEmpPassEncryption(){
    EmployeeTasks2 empTask2 = new EmployeeTasks2();
    empTask2.setName("daniel");
    empTask2.setAge(13);
    empTask2.setPassword("123321");
    empTask2.setTask(TASK_LIST);
    serializeObject(empTask2,"EmpTask2");
    EmployeeTasks2 empTask = deserializeObject(RELATIVE_PATH+"EmpTask2",EmployeeTasks2.class);
    System.out.println(empTask);
  }
  /**
   * https://www.ibm.com/developerworks/cn/java/j-lo-serial/index.html
   * Java 序列化机制为了节省磁盘空间，具有特定的存储规则，当写入文件的为同一对象时，并不会再将对象的内容进行存储，而只是再次存储一份引用，上面增加的 5 字节的存储空间就是新增引用和一些控制信息的空间。反序列化时，恢复引用关系，使得清单 3 中的 t1 和 t2 指向唯一的对象，二者相等，输出 true。该存储规则极大的节省了存储空间。
   *
   */
  @Test
  public void testTwiceSerialize() throws Exception {
    EmployeeTasks2 empTask2 = new EmployeeTasks2();
    empTask2.setName("daniel");
    empTask2.setAge(13);
    empTask2.setPassword("123321");
    empTask2.setTask(TASK_LIST);
    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(RELATIVE_PATH+"TWICESERIALIZEOBJ"));
    oos.writeObject(empTask2);
    oos.flush();
    System.out.println(new File(RELATIVE_PATH+"TWICESERIALIZEOBJ").length());//386
    oos.writeObject(empTask2);
    oos.close();
    System.out.println(new File(RELATIVE_PATH+"TWICESERIALIZEOBJ").length());//391
    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(RELATIVE_PATH+"TWICESERIALIZEOBJ"));
    EmployeeTasks2 empTask3 = (EmployeeTasks2) ois.readObject();
    EmployeeTasks2 empTask4 = (EmployeeTasks2) ois.readObject();
    System.out.println(empTask3 == empTask4); //true
  }

  /**
   * EmployeeTasks与EmployeeTasks2是两种自定义序列化的方式
   */
  @Test
  public void serializeEmpTasks(){
    EmployeeTasks employeeTasks = new EmployeeTasks();
    employeeTasks.setName("jack");
    employeeTasks.setAge(12);
    employeeTasks.setTask(TASK_LIST);
    System.out.println(employeeTasks);
    serializeObject(employeeTasks,"EmployeeTasksSerialData");
    String relativePath = "src/main/java/com/effectivejava/c11/primary/EmployeeTasksSerialData";
    EmployeeTasks empTasks = deserializeObject(relativePath,EmployeeTasks.class);
    System.out.println(empTasks);
  }
  @Test
  public void deserializeEmpTasks(){

  }

}
