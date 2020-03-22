package designpatternchan.c08factory.abstractfactory;

import designpatternchan.c08factory.ConcreteProduct1;
import designpatternchan.c08factory.ConcreteProduct2;
import designpatternchan.c08factory.Product;

import java.util.HashMap;
import java.util.Map;

public class ProductFactory {
    private static final Map<String, Product> productMap = new HashMap();

    public static synchronized  Product createProduct(String type) throws Exception{
        Product product =null;
        if(productMap.containsKey(type)){
            product = productMap.get(type);
        }else{
            if(type.equals("Product1")){
                product = new ConcreteProduct1();
            }else{
                product = new ConcreteProduct2();
            }
            productMap.put(type,product);
        }
        return product;
    }
}
