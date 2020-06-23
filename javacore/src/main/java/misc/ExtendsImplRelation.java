package misc;

public class ExtendsImplRelation {
    static class Parent{
        private void methodA(){}
        protected void methodC(){}
        void methodD(){}
        public void methodB(){}
    }
    static class Child extends Parent{}
    static class Deceiver {
        public static void main(String[] args) {
            Child child = new Child();
            // child.methodA();
            child.methodB();
            child.methodC();
        }
    }
}
