package designpatternchan.c08factory;

public class ConcreteCreator extends Creator {
    @Override
    public <T extends Product> T createProduct(Class<T> clazz) {
        Product product = null;
        try {
            product = (Product) Class.forName(clazz.getName()).newInstance();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return (T) product;
    }
}
