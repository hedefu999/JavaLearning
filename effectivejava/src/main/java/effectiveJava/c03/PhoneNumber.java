package effectiveJava.c03;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class PhoneNumber {
    //如果一个类的hashCode计算成本高或是常用于散列键，则值得将hashCode缓存起来
    //在第一次计算hashcode时初始化这个字段
    private volatile int hashCode;
    private String area;
    private boolean isVirtual;
    private Integer number;
    private Date activeDate;

    //effectiveJava推荐的写法
    //@Override
    //public boolean equals(Object obj) {
    //    if (obj == this) return true;
    //    if (!(obj instanceof PhoneNumber)) return false;
    //    PhoneNumber pn = (PhoneNumber) obj;
    //    后面相似
    //}

    //以下是IDEA自动生成的写法
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneNumber that = (PhoneNumber) o;
        return isVirtual == that.isVirtual &&
                area.equals(that.area) &&
                number.equals(that.number) &&
                Objects.equals(activeDate, that.activeDate);
    }

    //java7之后可以使用Objects提供的方法，效果等同于下面的写法
    @Override
    public int hashCode() {
        return Objects.hash(area, isVirtual, number, activeDate);
    }
    //使用31的原因：可以使用移位操作和减法运算替代乘法：31 * i = (i<<5)-i
    //@Override
    //public int hashCode() {
    //    int result = area != null ? area.hashCode() : 0;
    //    result = 31 * result + (isVirtual ? 1 : 0);
    //    result = 31 * result + (number != null ? number.hashCode() : 0);
    //    result = 31 * result + (activeDate != null ? activeDate.hashCode() : 0);
    //    return result;
    //}

    public static void main(String[] args) {
        HashMap<PhoneNumber,String> phonebook = new HashMap<>();

    }
}
