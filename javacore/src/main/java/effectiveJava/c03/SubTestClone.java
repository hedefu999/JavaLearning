package effectiveJava.c03;

public class SubTestClone extends TestClone {
    public SubTestClone(int id, String name, Point point) {
        super(id, name, point);
    }

    @Override
    public String fetchName() {
        return super.fetchName()+",";
    }

    //@Override
    //protected TestClone clone() throws CloneNotSupportedException {
    //    return super.clone();
    //}
    public static void main(String[] args){
        Point p = new Point(2,6);
        SubTestClone sub = new SubTestClone(1,"df",p);
        SubTestClone alias = (SubTestClone) sub.clone();
        System.out.println(alias.getName());
    }
}
