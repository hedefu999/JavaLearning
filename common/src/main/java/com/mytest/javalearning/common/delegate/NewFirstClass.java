package com.mytest.javalearning.common.delegate;

/**
 * 使用闭包代理回调后的firstClass
 */
public class NewFirstClass {
    private SecondClass secondClass;

    public void beginRunSecondDelegateMethod(){
        if (this.secondClass == null){
            this.secondClass = new SecondClass(new CustomDelegate() {
                @Override
                public void setValue(String value) {
                    System.out.println("设置value2"+value);
                }
            });
        }
        this.secondClass.begin();
    }
}
