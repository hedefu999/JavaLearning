package com.concurrency;

import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class DateUtil {

    /**
     * 锁对象
     */
    private static final Object lockObj = new Object();

    /**
     * 存放不同的日期模板格式的sdf的Map
     */
    private static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<String, ThreadLocal<SimpleDateFormat>>();


    /**
     * 返回一个ThreadLocal的sdf,每个线程只会new一次sdf
     *
     * @param pattern
     * @return
     */
    private static SimpleDateFormat getSdf2(final String pattern) {
        ThreadLocal<SimpleDateFormat> tl = sdfMap.get(pattern);

        // 此处的双重判断和同步是为了防止sdfMap这个单例被多次put重复的sdf
        if (tl == null) {
            synchronized (lockObj) {
                tl = sdfMap.get(pattern);
                if (tl == null) {
                    // 只有Map中还没有这个pattern的sdf才会生成新的sdf并放入map
                    System.out.println("put new sdf of pattern " + pattern + " to map");

                    // 这里是关键,使用ThreadLocal<SimpleDateFormat>替代原来直接new SimpleDateFormat
                    tl = new ThreadLocal<SimpleDateFormat>() {

                        @Override
                        protected SimpleDateFormat initialValue() {
                            System.out.println("thread: " + Thread.currentThread() + " init pattern: " + pattern);
                            return new SimpleDateFormat(pattern);
                        }
                    };
                    sdfMap.put(pattern, tl);
                }
            }
        }

        return tl.get();
    }
    private static SimpleDateFormat getSdf(final String pattern) {
        return new SimpleDateFormat(pattern);
    }


    /**
     * 使用ThreadLocal<SimpleDateFormat>来获取SimpleDateFormat,这样每个线程只会有一个SimpleDateFormat
     * 如果新的线程中没有SimpleDateFormat，才会new一个
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern) {
        return getSdf(pattern).format(date);
    }

    public static Date parse(String dateStr, String pattern) throws ParseException {
        return getSdf(pattern).parse(dateStr);
    }

    public static void main2(String[] args) {
        String a = null;
        Optional<String> b = Optional.empty();
        try {
            System.out.print(a.length());
            System.out.print(b.orElse("").length());
        }
        catch (Exception ex) {
            System.out.print(a);
        }
        finally {
            a = "String";
            System.out.print(a.length());
            b = Optional.ofNullable("");
            System.out.println(b.get().length());
        }
    }
    static class Parent {
        protected static int count = 0;
        public Parent() { count++; }
        static int getCount() { return count; }
    }

    static class Child extends Parent {
        public Child() { count++; }
        public static void main(String [] args) {
            System.out.println("Count = " + getCount());
            Child obj = new Child();
            System.out.println("Count = " + getCount());
        }
    }

    static class Helper {
        private int data = 5;
        public void bump(int inc) {
            inc++;
            data = data + inc;
        }
    }

    public static void main3(String []args) {
        //Helper h = new Helper();
        //int data = 2;
        //h.bump(data);
        //System.out.println(h.data + " " + data);
        Supplier<String> i = () -> "Car";
        Consumer<String> c = x -> System.out.print(x.toLowerCase());
        Consumer<String> d = x -> System.out.print(x.toUpperCase());
        c.andThen(d).accept(i.get());
        System.out.println();
    }

    public static void main4(String[] args) {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("nashorn");
        try {
            engine.eval(
                    "var i=0;"
                            + "i++;"
                            + "var String = Java.type(\"java.lang.String\");"
                            + "var str = new String(\"Java\");"
                            + "print(str);"
                            + "print(i);"
            );
        } catch (ScriptException se) { System.out.println("Script Exception"); }
    }

    public static void main5(String[] args) {
        Date aDate = null;
        try {
            aDate = new SimpleDateFormat("yyyy-mm-dd").parse("2012-01-15");
            Calendar aCalendar = Calendar.getInstance();
            aCalendar.setTime(aDate);
            System.out.print(aCalendar.get(aCalendar.DAY_OF_MONTH)+"," +  aCalendar.get(aCalendar.MONTH));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate bDate = LocalDate.parse("2012-01-15", formatter);
            System.out.print(" " + bDate.getDayOfMonth()+"," +  bDate.getMonthValue());

        } catch (ParseException ex) {System.out.println("ParseException " + ex);
        } catch (DateTimeParseException ex) {System.out.println(" DateTimeParseException " + ex);
        }
    }

    //interface Account{
    //    BigDecimal balance = new BigDecimal(0.00);
    //}
    //static class SavingsAccount implements Account {
    //    public SavingsAccount(BigDecimal initialValue) {
    //        balance = initialValue;
    //    }
    //    public String toString() {
    //        return balance.toString();
    //    }
    //    public static void main(String args[]) {
    //        SavingsAccount instance = new SavingsAccount(new BigDecimal(50.00));
    //        System.out.println(instance);
    //    }
    //}

    static class MyClass {
        public static void main(String args[]) {
            MyClass myClass = new MyClass();
            Class c = myClass.getClass();
            try {
                System.out.println(c.getMethod("getNumber", null).toString());
                System.out.println(c.getDeclaredMethod("setNumber", null).toString());
            } catch (NoSuchMethodException | SecurityException e) {}
        }
        public Integer getNumber() {
            return 2;
        }
        public void setNumber(Integer n) {
        }
    }

    public static void main6(String[] args) throws Exception {
        File file = new File("Data.txt");
        FileWriter output = new FileWriter(file);
        //for (int i = 0; i < 5; i++)
        //    output.write(String.valueOf(i));

        //output.write(new char[] {'0', '1', '2', '3', '4'});


        PrintWriter p = new PrintWriter(output);
        Stream.of('0','1','2','3','4').forEach(p::write);
        output.flush();
    }

    public static void main7(String[] args) {
        Integer i = 4000, j = 4000;
        System.out.println(i == j);
        Integer k = 50, n = 50;
        System.out.println(k == n);
    }

    public static void main8(String[] args) {
        String str1 = "My String";
        String str2 = new String ("My String");
        System.out.println(str1.matches(str2));
        //str1.equals(str2);
        //System.out.println(String.parse(str1) == str2);
        System.out.println(str1.hashCode() == str2.hashCode());

    }

    public static void main(String[] args) {
        int x = -1;
        x = x>>>1;
        //x=x>>32;--
        //x = x>>>0;--
        //x = x >> 1; --
        //x= x>>>32;
        System.out.println(x);
        System.out.println(2^14);
    }

    public static void main10(String[] args) {
        Locale locale = new Locale("USA") ;
        System.out.println("Country: " + locale.getCountry());
    }
    static class Employee implements Serializable {
        transient int id = 1;
        String name = "XYZ";
        static double salary = 9999.99;
    }
    public static void main11(String[] args) {
        Employee emp = new Employee();
        try {
            ObjectOutputStream out = new ObjectOutputStream(
                    new FileOutputStream("Company.txt"));
            out.writeObject(emp);  out.close();
            System.out.print(++emp.salary + " ");

            ObjectInputStream in = new ObjectInputStream(
                    new FileInputStream("Company.txt"));
            Employee empCopy = (Employee)in.readObject();
            in.close();
            System.out.println(String.join(" ",String.valueOf(empCopy.id),empCopy.name,
                    String.valueOf(empCopy.salary)));
        } catch (Exception x) {System.out.println("Error"); }
    }


}
