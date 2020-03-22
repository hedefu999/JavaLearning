package effectiveJava.c05;

import java.util.List;

public interface GenericTestI<T> {
    T getAny(String name);
    T[] toArray(List<T> list);
}
