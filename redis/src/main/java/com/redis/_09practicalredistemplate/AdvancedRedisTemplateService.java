package com.redis._09practicalredistemplate;

public interface AdvancedRedisTemplateService {
    //执行多个命令，使用SessionCallback接口实现多个命令在一个redis连接中执行
    void execMultiCommand();
    //执行redis事务，使用SessionCallback接口实现事务在一个redis连接中执行
    void execTransaction();
    //执行redis流水线,将多个命令一次性发送给redis服务器
    void execPipeline();
}
