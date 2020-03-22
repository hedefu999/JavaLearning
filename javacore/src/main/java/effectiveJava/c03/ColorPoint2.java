package effectiveJava.c03;

public class ColorPoint2 {
    //依据复合优先于继承的原则，不再 extends Point,而将Point作为ColorPoint2的一个私有域
    private final Point point;
    private final String color;

    public ColorPoint2(int x, int y, String color) {
        if (color == null) throw new NullPointerException();
        this.point = new Point(x,y);
        this.color = color;
    }
    //一个公有视图方法，可以充分利用对象
    public Point asPoint(){
        return point;
    }
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ColorPoint)) return false;
        ColorPoint2 cp = (ColorPoint2) obj;
        return cp.point.equals(this.point) &&
                cp.color.equalsIgnoreCase(this.color);
    }
}
