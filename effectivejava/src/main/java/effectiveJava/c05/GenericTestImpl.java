package effectiveJava.c05;

public abstract class GenericTestImpl<V,T> implements GenericTestI<T> {
    @Override
    public T getAny(String name) {
        return null;
    }

    public V modifyObj(V v){

        return v;
    }
}
