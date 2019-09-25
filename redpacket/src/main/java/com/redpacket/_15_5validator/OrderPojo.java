package com.redpacket._15_5validator;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Date;

/**
 * 对订单对象字段数据进行注解式验证
 */
public class OrderPojo {
    @NotNull
    private String name;
    @DecimalMin("18")
    @DecimalMax("100")
    private Integer age;
    @Future //只能是将来日期
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
    @Min(2)
    @Max(5)
    private Integer count;
    @Pattern(regexp = "^[a-zA-Z0-9]*@\\w+\\.(com|org|cn)",message = "邮箱格式不合法")
    private String email;
    @Size(min = 0,max = 255)//备注字符长度限制
    private String note;
}
@Controller
class ValidateController{
    @RequestMapping("/")
    public ModelAndView annotationValidate(@Valid OrderPojo orderPojo, Errors errors){
        if (errors.hasErrors()){
            for (FieldError error : errors.getFieldErrors()){
                System.out.println("出错信息："+error.getField()+"\n msg:"+error.getDefaultMessage());
            }
        }
        ModelAndView mv = new ModelAndView();
        mv.setViewName("index");
        return mv;
    }
}