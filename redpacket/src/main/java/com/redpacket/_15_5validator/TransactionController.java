package com.redpacket._15_5validator;

import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

public class TransactionController {
    @InitBinder
    public void initBinder(DataBinder binder){
        //数据绑定器加入验证器
        binder.setValidator(new TransactionValidator());
    }
    //@Valid用于启动验证器
    //JSR303注解方式和spring验证器方式不能同时使用
    @RequestMapping("/validate")
    public ModelAndView validator(@Valid Transaction trx, Errors errors){
        if (errors.hasErrors()){
            List<FieldError> fieldErrors = errors.getFieldErrors();
            for (FieldError error : fieldErrors){
                System.out.println(error.getField()+error.getDefaultMessage());
            }
        }
        ModelAndView mv = new ModelAndView();
        mv.setViewName("index");
        return mv;
    }
}
