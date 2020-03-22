package designpatternchan.c07singlet;

/**
 * 懒汉模式要考虑线程安全问题
 */
public class IDEADefault2 {
    private static IDEADefault2 ourInstance = null;

    public synchronized static IDEADefault2 getInstance() {
        if (ourInstance == null){
            ourInstance = new IDEADefault2();
        }
        return ourInstance;
    }

    private IDEADefault2() {
    }
}
