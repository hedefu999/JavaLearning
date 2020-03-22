package effectiveJava.c03;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Point implements Cloneable{
    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        //obj如果是Point的子类，instanceof返回为true
        //if (!(obj instanceof Point)){
        //    return false;
        //}
        //Point p = (Point) obj;
        //return p.x == x && p.y == y;

        //使用getClass代替instanceof判断
        if (obj == null || obj.getClass() != getClass()) return false;
        Point p = (Point) obj;
        return p.getX() == x && p.getY() == y;
        //这种屏蔽子类的写法的确能解决instanceof的不对称问题，但它违反了里氏替换原则
    }

    @Override
    protected Object clone(){
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
