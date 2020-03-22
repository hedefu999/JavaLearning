package effectiveJava.c03;

import java.util.HashSet;
import java.util.Set;

public class UnitCircle {
    public static final Set<Point> unitCircle;
    static {
        unitCircle = new HashSet<>();
        unitCircle.add(new Point(1,0));
        unitCircle.add(new Point(0,1));
        unitCircle.add(new Point(-1,0));
        unitCircle.add(new Point(0,-1));
    }
    public static boolean onUnitCircle(Point p){
        return unitCircle.contains(p);
    }
}
