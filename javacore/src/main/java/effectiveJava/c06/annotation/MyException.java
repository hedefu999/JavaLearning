package effectiveJava.c06.annotation;

/**
 * @author hedefu
 */
public class MyException extends RuntimeException {
    private static final long serialVersionUID = -7118712501247277817L;

    @Override
    public String getMessage() {
        return "自定义异常";
    }
}
