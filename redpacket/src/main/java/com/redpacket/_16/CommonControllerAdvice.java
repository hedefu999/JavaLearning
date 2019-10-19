package com.redpacket._16;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.WebRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * 与springAop一样，springMVC也可以给Controller加入通知，涉及4个注解
 * @ControllerAdvice 作用于类，标识全局性的控制器的拦截器
 * @InitBinder 为HTTP入参指定格式化规则，在registerCustomEditor方法中指明参数的类型
 * @ExceptionHandler 当控制器发生注册的异常时就会跳转到它所标注的方法上
 * @ModelAttribute 针对数据模型的注解，先于控制器方法运行，当所标注的方法返回对象时，返回的内容会被保存到数据模型中
 * 上面三个注解也可以用于@Controller标注的类，@ControllerAdvice 的效果相当于把多个Controller相同的标注给提取了出来
 */
@ControllerAdvice(basePackages = {"com.redpacket._16"})
public class CommonControllerAdvice {
    private final Logger log = LoggerFactory.getLogger(CommonControllerAdvice.class);

    //定义HTTP对应参数处理规则
    @InitBinder
    public void initBinder(WebDataBinder binder){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //针对日期类型的格式化，其中CustomerDateEditor是客户自定义编辑器
        //boolean参数表示是否允许为空
        CustomDateEditor customDateEditor = new CustomDateEditor(dateFormat, false);
        binder.registerCustomEditor(Date.class,customDateEditor);
    }
    @ModelAttribute
    public void populateModel(Model model){
        model.addAttribute("projectName","javaLearning");
    }


    //捕获任何异常，使用统一的视图响应
    //只能有一个标注有@ExceptionHandler的方法
    //将WebRequest入参替换为ModelMap，ExceptionHandler标注会失效
    @ExceptionHandler(Exception.class) //捕获所有异常
    //@ExceptionHandler(UserException.class) 仅处理自定义的User查不到的异常
    public String exception(Exception e, WebRequest webRequest){
        Iterator<String> paramNames = webRequest.getParameterNames();
        while (paramNames.hasNext()){
            String paramName = paramNames.next();
            log.info("请求参数：{} - {}", paramName,webRequest.getParameter(paramName));
        }
        log.info("发生异常：{}", e.getMessage());
        return "exception";
    }

    //@ExceptionHandler(Exception.class)
    //public String exception2(Exception e, ModelMap modelMap){
    //    modelMap.put("异常原因", e.getCause());//在返回页面展示
    //    log.info("项目 {}",modelMap.get("projectName"));
    //    return "exception";
    //}
}
