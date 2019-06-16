package com.ssmr.c10.annoInject;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.ImportResource;


//@ComponentScans({@ComponentScan(basePackages = "com.ssmr.c09"),@ComponentScan(basePackageClasses = Role.class)})  多个@ComponentScan注解配置会导致Bean重复注入，不建议这样做
//@ComponentScan属性有basePackageClasses和basePackages
//@ComponentScan等同于<context:component-scan base-package=""/>
@ComponentScan(basePackageClasses = {RoleServiceImpl.class},basePackages = {"com.ssmr.c10.annoInject"})
@ImportResource({"classpath:com/ssmr/c10/spring-data.xml"})
public class ComponentScanDef {

}
