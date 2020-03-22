package effectiveJava.c03;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.UUID;

public class ChapterTest {
    @Test
    public void test8_1(){
        Point p = new Point(1,2);
        ColorPoint cp = new ColorPoint(1,2,"RED");
        System.out.println(p.equals(cp));
        System.out.println(cp.equals(p));

    }
    @Test
    public void test12_1(){
        //BigDecimal的compareTo与equals结果不一致，导致依赖compareTo的TreeSet、依赖equals的HashSet中元素数量不一致
        BigDecimal bigDecimal1 = new BigDecimal("1.0");
        BigDecimal bigDecimal2 = new BigDecimal("1.00");
        HashSet<BigDecimal> hashSet = new HashSet<>();
        hashSet.add(bigDecimal1);hashSet.add(bigDecimal2);
        System.out.println(hashSet.size());// 2
        TreeSet<BigDecimal> treeSet = new TreeSet<>();
        treeSet.add(bigDecimal1);treeSet.add(bigDecimal2);
        System.out.println(treeSet.size());// 1
    }
    @Test
    public void testworkNumInfoNo(){
        System.out.println(UUID.randomUUID());
    }
}
