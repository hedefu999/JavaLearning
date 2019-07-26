package effectiveJava.c06;

import java.util.EnumMap;
import java.util.Map;

public enum Phase {
    //--- 后来又增加了电离态PLASMA
    PLASMA,
    SOLID,LIQUID,GAS;
    public enum Transition{
        //--- 添加两种状态转化不需要改代码
        IONIZE(GAS,PLASMA),DEIONIZE(PLASMA,GAS),

        MELT(SOLID,LIQUID),FREEZE(LIQUID,SOLID),
        BOIL(LIQUID,GAS),CONDENSE(GAS,LIQUID),
        SUBLIME(SOLID,GAS),DEPOSIT(GAS,SOLID);
        private final Phase src;
        private final Phase dst;
        Transition(Phase src,Phase dst){
            this.src = src;
            this.dst = dst;
        }
        private static final Map<Phase, Map<Phase,Transition>> m =
                new EnumMap<Phase, Map<Phase, Transition>>(Phase.class);
        //初始化上面的枚举数据到Map
        static {
            for (Phase p:Phase.values())
                m.put(p, new EnumMap<Phase,Transition>(Phase.class));
            for (Transition trans : Transition.values())
                m.get(trans.src).put(trans.dst,trans);
        }
        public static Transition from(Phase src, Phase dst){
            return m.get(src).get(dst);
        }
    }
    public static void main(String[] args) {
        System.out.println(Transition.from(Phase.GAS,Phase.PLASMA));
    }
}
