package effectiveJava.c04;

public class Suber extends Super {
    private final String info;
    public Suber(){info = "suber";}
    @Override
    public void overrideMe() {
        System.out.println("info in suber = "+info);
        //System.out.println(info.length());
    }
    public static void main(String[] args) {
        Suber suber = new Suber();
        suber.overrideMe();
    }
}
