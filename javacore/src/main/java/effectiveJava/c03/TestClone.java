package effectiveJava.c03;

import lombok.Data;

@Data
public class TestClone implements Cloneable{
    private int id;
    private String name;
    private Point point;

    public TestClone() {
    }

    public TestClone(int id, String name, Point point) {
        this.id = id;
        this.name = name;
        this.point = point;
    }
    public String fetchName(){
        return name+".";
    }

    @Override
    protected TestClone clone() {
        TestClone copy = null;
        try {
            copy = (TestClone) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
        copy.setName(fetchName());
        //尽量使用clone而非构造函数实现深度拷贝
        copy.setPoint((Point) point.clone());
        return copy;
    }

    public static void testDeepCopy(){
        Point point = new Point(3,4);
        TestClone testClone = new TestClone(1,"first", point);

        System.out.println(testClone.getPoint().getX());
        TestClone alias = testClone.clone();
        alias.getPoint().setX(12);
        System.out.println(testClone.getPoint().getX());
        System.out.println(alias.getName());
    }


    public static void main(String[] args) throws Exception{
        testDeepCopy();
    }
}
