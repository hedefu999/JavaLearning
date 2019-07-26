package effectiveJava.c04;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestFinal {
    private final Integer[] nums = new Integer[]{1,2,3};
    public List<Integer> getArray(){
        return Collections.unmodifiableList(Arrays.asList(nums));
    }
    public Integer[] getArray2(){
        return nums.clone();
    }
    public Integer[] getArray0(){
        return nums;
    }
}
