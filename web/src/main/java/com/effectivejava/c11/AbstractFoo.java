package com.effectivejava.c11;

import java.util.concurrent.atomic.AtomicReference;

//不可序列化的有状态类允许子类序列化
public abstract class AbstractFoo {
  private int x,y;
  //跟踪初始化状态的枚举
  private enum State{NEW,INITIALIZING,INITIALIZED};
  private final AtomicReference<State> init = new AtomicReference<>(State.NEW);
  public AbstractFoo(int x,int y){initialize(x,y);}
  //无参构造方法initialize方法允许子类的readObject方法初始化状态
  protected AbstractFoo(){}
  protected final void initialize(int x,int y){
    //compareAndSet是一个很好的线程安全状态机(thread-safe state machine)
    if (!init.compareAndSet(State.NEW, State.INITIALIZING))
      throw new IllegalStateException("Already initialized");
    this.x = x;this.y = y;//初始化操作
    init.set(State.INITIALIZED);
  }
  //读取内部状态，子类的writeObject方法可以手动序列化这些状态
  protected final int getX(){checkInit();return x;}
  protected final int getY(){checkInit();return y;}
  private void checkInit(){
    if (init.get() != State.INITIALIZED)
      throw new IllegalStateException("uninitialized");
  }
}
