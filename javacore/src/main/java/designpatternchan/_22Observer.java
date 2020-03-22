package designpatternchan;

public class _22Observer {
    static class Observer {
        static class Politician{
            private Integer state = 0;

            public Integer getState() {
                return state;
            }

            public void setState(Integer state) {
                this.state = state;
            }
        }
        static class Spy extends Thread{
            private Politician target;
            public Spy(Politician target){
                this.target = target;
            }
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {}
                    if (this.target.getState() == 1){
                        System.out.println("立即行动");
                    }
                }
            }
        }

        public static void main(String[] args) throws InterruptedException {
            Politician man = new Politician();
            Spy spy = new Spy(man);
            spy.start();
            Thread.sleep(2000);
            man.setState(1);
            Thread.sleep(3000);
            man.setState(0);
        }
    }
}
