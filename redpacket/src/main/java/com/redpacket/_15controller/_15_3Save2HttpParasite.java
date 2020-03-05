package com.redpacket._15controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

/**
 * javaEE中将数据暂存在HTTP的request session中进行传递
 * springMVC相应地提供了3个注解予以支持：@RequestAttribute、@SessionAttribute、@SessionAttributes
 */
@Controller
public class _15_3Save2HttpParasite {
    /** 在JSP中需要这样准备数据
     * <% @ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
     * <body>
     *     <%
     *     request.setAttribute("id",2);
     *     request.getRequestDispatcher("/requestAttribute").forward(request,response);
     *     %>
     * </body>
     */
    @RequestMapping("/requestAttribute")
    public ModelAndView reqAttr(@RequestAttribute("id") Integer id){
        ModelAndView mv = new ModelAndView();
        mv.setView(new MappingJackson2JsonView());
        return mv;
    }
    /**
     * 在JSP中设置session，在Controller中读取
     */
    @RequestMapping("/sessionAttribute")
    public ModelAndView sessionAttr(@SessionAttribute("id") Integer id){
        ModelAndView mv = new ModelAndView();
        System.out.println("session中的ID = "+id);
        mv.addObject("user",new Object());
        return mv;
    }

    @RequestMapping("/getHeaderAndCookie")
    @ResponseBody
    public String getHeaderAndCookie(@RequestHeader(value = "User-Agent") String userAgent,
                                     @CookieValue(value = "JSESSIONID") String jsessionID){
        System.out.println(userAgent);
        System.out.println(jsessionID);
        return "";
    }
}
