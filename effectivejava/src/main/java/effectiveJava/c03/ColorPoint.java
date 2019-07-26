package effectiveJava.c03;


public class ColorPoint extends Point {
    private final String color;
    public ColorPoint(int x, int y, String color) {
        super(x, y);
        this.color = color;
    }
    @Override
    public boolean equals(Object obj) {
        //这种写法不满足对称性
        if (!(obj instanceof ColorPoint)){
            return false;
        }
        return super.equals(obj) && color.equalsIgnoreCase(((ColorPoint) obj).color);

        //这种写法不满足传递性
        //if (!(obj instanceof Point)) return false;
        //obj是一个普通的Point，忽略颜色信息
        //if (!(obj instanceof ColorPoint)) return obj.equals(this);
        //obj是一个ColorPoint，进行完整比较
        //return super.equals(obj) && ((ColorPoint) obj).color.equalsIgnoreCase(color);
        //无法在扩展可实例化类的同时，既增加新的值组件，同时又保留equals约定
    }
}
