package com.ssmr.c11.c_xmlaop;

import com.ssmr.c11.Role;
import com.ssmr.c11.b_annotationaop.RoleVerifier;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:com/ssmr/c11/spring-aop.xml")
public class MainTest {
    private Role role;
    @Before
    public void initRole(){
        role = new Role();role.setId(12);role.setName("jack");
    }

    public static void main(String[] args) {
        String xmlLocation = "com/ssmr/c11/spring-aop.xml";
        ApplicationContext context = new ClassPathXmlApplicationContext(xmlLocation);
        RoleService roleService = context.getBean(RoleService.class);
        Role role = new Role();role.setId(12);role.setName("jack");
        roleService.printRole(role);
        /**
         * before ...
         * around 环绕通知执行前
         * 入参：id = 12, name = jack
         * around 环绕通知执行后
         * after-returning ...
         * after ...
         */
        //使用xml配置和使用注解配置的环绕通知行为（执行顺序）不同,@around可以替换@before+@after
        //FIXME xml配置的aop after在after-returning后执行？？？
        //xml配置的aop执行顺序是：@before->@around->方法执行->@around->@after-returning->@after
    }

    @Autowired
    private RoleService roleService;
    @Test
    public void testXmlAop2(){
        roleService.printRole2(role);
        /**
         * before ...role.name=jack
         * around 环绕通知执行前,role.name=jack
         * 第二个打印函数：id=12,name=jack
         * around 环绕通知执行后,role.name=jack
         * after ...role.name=jack
         */
    }
    @Test
    public void testXmlAop3(){
        roleService.printRole3(role);
        /**
         * around 环绕通知执行前,role.name=jack
         * 带有注解的的打印函数：role.name = Daniel.
         * around 环绕通知执行后,role.name=Daniel
         */
    }

    /**
     * 测试增强功能，引入新的功能
     * 将RoleVerifier接口下挂到RoleService中，可以强转
     */
    @Test
    public void testEnhance(){
        RoleVerifier roleVerifier = (RoleVerifier) roleService;
        //role = null;
        if (roleVerifier.verify(role)){
            roleService.printRole4(role);
        }
        /**
         * role实例不为空，可以打印
         * 打印数据：role.name = jack.
         */
    }

}
