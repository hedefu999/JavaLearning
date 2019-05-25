package com.ssmr.c09;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
@Getter
@Setter
public class JuiceMaker2 implements BeanNameAware, BeanFactoryAware, ApplicationContextAware, InitializingBean {
    private String beverageShop;
    private JuiceSource source;

    private String getClassName(){
        return this.getClass().getSimpleName();
    }
    public String makeJuice(){
        return "制作果汁：" + source.toString();
    }
    /*
    * 下述各初始化方法的调用顺序
    * BeanNameAware.setBeanName() -->  BeanFactoryAware.setBeanFactory() -->
    * ApplicationContextAware.setApplicationContext()  --->  InitializingBean.afterPropertiesSet() -->
    * JuiceMaker2.init() ...  --> JuiceMaker.destroy()
    * */
    //自定义到init方法在配置文件中指明，由框架调用
    public void init(){
        System.out.printf("%s中的自定义初始化方法。\n", getClassName());
    }

    public void destroy(){
        System.out.printf("%s中的自定义销毁方法。\n", getClassName());
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.printf("%s 调用到接口 %s 的 %s 方法\n",
                getClassName(),"BeanFactoryAware",Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Override
    public void setBeanName(String name) {
        System.out.printf("%s 调用到接口 %s 的 %s 方法\n",
                getClassName(),"BeanNameAware",Thread.currentThread().getStackTrace()[1].getMethodName());
        //name是配置文件中设置的bean id = juniceMaker
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.printf("%s 调用到接口 %s 的 %s 方法\n",
                getClassName(),"InitializingBean",Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.printf("%s 调用到接口 %s 的 %s 方法\n",
                getClassName(),"ApplicationContextAware",Thread.currentThread().getStackTrace()[1].getMethodName());
    }

}
