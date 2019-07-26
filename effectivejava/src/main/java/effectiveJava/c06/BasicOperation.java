package effectiveJava.c06;

import java.util.HashMap;
import java.util.Map;

public enum BasicOperation implements Operation{
    //枚举类型中的抽象方法必须被它所有常量中的具体方法所覆盖
    //策略模式的延伸 - 策略枚举
    PLUS("+"){
        @Override
        public double apply(double x, double y) { return x+y; }
    },MINUS("-"){
        @Override
        public double apply(double x, double y) { return x-y; }
    };
    private final String symbol;
    BasicOperation(String symbol) { this.symbol = symbol; }
    @Override
    public String toString() { return this.symbol; }

    //---- 定义一个getBySymbol方法，入参是 "+"/"-"
    private static final Map<String, BasicOperation> operatorMap = new HashMap<>();
    static {
        for (BasicOperation op : values()){
            operatorMap.put(op.toString(),op);
        }
    }
    public static BasicOperation getBySymbol(String symbol){
        return operatorMap.get(symbol);
    }
    //获取反运算
    public static BasicOperation getInverseOp(BasicOperation op){
        switch (op){
            case PLUS: return BasicOperation.MINUS;
            case MINUS: return BasicOperation.PLUS;
            default: return null;
        }
    }






    public static void main(String[] args) {
        System.out.printf("%f %s %f = %f\n",12.35,PLUS,3.47,PLUS.apply(12.35,3.47));
        System.out.println(MINUS.ordinal());
    }
}
