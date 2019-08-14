package com.mytest.javalearning.common.delegate;

public class FirstClass implements CustomDelegate {
    private SecondClass secondClass;

    public void beginRunSecondDelegateMethod(){
        if (this.secondClass == null){
            this.secondClass = new SecondClass(this::setValue);
        }
    }

    @Override
    public void setValue(String value) {
        System.out.println("设置value"+value);
    }
}
