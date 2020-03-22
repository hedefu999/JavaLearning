package designpatternchan.c08factory;

public abstract class Creator {
    public abstract <T extends Product> T createProduct(Class<T> clazz);
}
