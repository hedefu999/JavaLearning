package designpatternchan.c06;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BookStore {
    public final static List<IBook> books = new ArrayList<>();
    static {
        books.add(new NovelBook("java",new BigDecimal("12.34"),"gosling"));
        books.add(new NovelBook("C++",new BigDecimal("35.23"),"committee"));
    }

    public static void main(String[] args) {
        books.forEach(book -> System.out.println(book.getCurrentPrice()));
    }
}
