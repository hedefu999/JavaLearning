package misc.core;

import lombok.Lombok;
import org.junit.Test;

import java.util.Date;

/**
 * 流式编程API案例展示
 */
public class StreamProgrammingAPI {
    @l
    public static class Container{
        private String name;
        private Integer number;
        private boolean whether;
        private Date date;
        Lombok
    }
    @Test
    public void test8() {
        int orderSpecialTagNumber = orderExtendRecords.stream()
                .filter(item -> OrderExtendType.order_tags.equals(item.getExtendType()))
                .mapToInt(item -> item.getExtendValue()==null?0:Integer.parseInt(item.getExtendValue())).sum();

    }
}
