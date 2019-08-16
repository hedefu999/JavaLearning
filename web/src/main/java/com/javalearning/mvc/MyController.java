package com.javalearning.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/my")
public class MyController {
    @RequestMapping("/index")
    public ModelAndView index(){
        ModelAndView mv = new ModelAndView();
        //加上dispatcher-servlet.xml中的前缀配置 /WEB-INF/jsp/index.jsp是将要跳转到的jsp资源
        mv.setViewName("index");
        return mv;
    }
}
