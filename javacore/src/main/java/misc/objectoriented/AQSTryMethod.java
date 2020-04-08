package misc.objectoriented;

/**
 * 为什么AQS中的tryXXX方法都是throw new UnsupportedException()却能正常被调用
 */
public class AQSTryMethod {
    static class MyAQS{
        public final void acquire(int arg) {
            if (tryAcquire(arg))
                System.out.println("尝试获取锁成功");
        }
        protected boolean tryAcquire(int arg) {
            throw new UnsupportedOperationException();
        }
    }
    abstract static class MySync extends MyAQS{
        abstract void lock();
    }
    static final class MyNonfairSync extends MySync {
        @Override
        void lock() {
            acquire(1);
        }
        @Override
        protected boolean tryAcquire(int arg) {
            System.out.println("调用子类中的tryAcquire方法");
            return true;
        }
    }
    interface MyLock{
        void lock();
    }
    static class MyReAcquireLock implements MyLock{
        private final MySync sync;
        public MyReAcquireLock(){
            sync = new MyNonfairSync();
        }
        public MyReAcquireLock(boolean fair){
            if (fair){
                sync = null;//略去公平锁的实现
            }else {
                sync = new MyNonfairSync();
            }
        }
        @Override
        public void lock() {
            sync.lock();
        }
    }
    public static void main(String[] args) {
        MyReAcquireLock lock = new MyReAcquireLock();
        lock.lock();
    }
    /*
     * 调用子类中的tryAcquire方法
     * 尝试获取锁成功
     */
}
