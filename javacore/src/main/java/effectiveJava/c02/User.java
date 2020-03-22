package effectiveJava.c02;

public enum User {
    JACK("jack");
    private String name;
    //Getter and Setters
    User(String name) {
        this.name = name;
    }
}
