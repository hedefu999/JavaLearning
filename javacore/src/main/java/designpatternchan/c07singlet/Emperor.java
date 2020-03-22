package designpatternchan.c07singlet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 有固定实例数量的"单例类"  -- 有上限的多例模式
 *
 */
public class Emperor {
    private static int maxNumOfEmperor = 5;
    private static List<String> nameList = new ArrayList<>();
    private static List<Emperor> emperors = new ArrayList<>();
    private static final Emperor emperor = new Emperor();
    private static int countNumOfEmperor = 0;
    static {
        for (int i = 0; i < maxNumOfEmperor; i++) {
            emperors.add(new Emperor(i+"皇"));
        }
    }
    private Emperor(){}
    private Emperor(String name){
        nameList.add(name);
    }
    public static Emperor getInstance(){
        Random random = new Random();
        countNumOfEmperor = random.nextInt(maxNumOfEmperor);
        return emperors.get(countNumOfEmperor);
    }
    public void say(){
        System.out.println(nameList.get(countNumOfEmperor));
    }

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            Emperor.getInstance().say();
        }
    }
}
