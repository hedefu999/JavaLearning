package com.redpacket._15_5validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
public class OrderValidateController {
    private final Logger log = LoggerFactory.getLogger(OrderValidateController.class);

    @InitBinder
    public void initBinder(DataBinder binder){
        //数据绑定器加入验证器
        binder.setValidator(new OrderValidator());
    }

    /**
     * @Valid用于启动验证器
     * JSR303注解方式和spring验证器方式不能同时使用
     * 为接收参数还要加@RequestBody,使用paw调post，入参是
    {
    "price": 23.0,
    "discount": 3.0,
    "total": 20.0,
    "paid": 20.0,
    "owed": 0.0
    }
     */
    @RequestMapping("/validate")
    public ModelAndView validator(@Valid @RequestBody Order trx, Errors errors){
        log.info("进入验证！");
        if (errors.hasErrors()){
            List<FieldError> fieldErrors = errors.getFieldErrors();
            for (FieldError error : fieldErrors){
                System.out.println(error.getField()+error.getDefaultMessage());
            }
        }
        ModelAndView mv = new ModelAndView();
        mv.setViewName("index");
        return mv;
        /** 打印内容：
         * total应付金额+折扣!= 标价
         * price应付金额+折扣!= 标价
         * paid总价不等于已付+未付
         */
    }
}
