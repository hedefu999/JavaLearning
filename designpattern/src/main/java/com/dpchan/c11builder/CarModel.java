package com.dpchan.c11builder;

import java.util.ArrayList;

public abstract class CarModel {
    //基本方法执行的顺序
    private ArrayList<String> sequence = new ArrayList<String>();

    protected abstract void start();

    protected abstract void stop();

    protected abstract void alarm();

    protected abstract void engineBoom();

    final public void run() {
        for(int i=0;i<this.sequence.size();i++){
            String actionName = this.sequence.get(i);
            if(actionName.equalsIgnoreCase("start")){
                this.start();
            }else if(actionName.equalsIgnoreCase("stop")){ //如果是stop关键字
                this.stop();
            }else if(actionName.equalsIgnoreCase("alarm")){ //如果是alarm关键字
                this.alarm(); //喇叭开始叫了
            }else if(actionName.equalsIgnoreCase("engine boom")){  //如果是engine boom关键字
                this.engineBoom();  //引擎开始轰鸣
            }

        }

    }

    //把传递过来的值传递到类内
    final public void setSequence(ArrayList<String> sequence){
        this.sequence = sequence;
    }
}
