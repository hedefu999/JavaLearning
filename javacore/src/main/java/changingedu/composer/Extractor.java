package changingedu.composer;

/**
 * 字段提取器，用于对象集合转换。见于无法使用流式编程的场景下
 */
public interface Extractor<T,V> {
    T getTargetField(V object);
}
