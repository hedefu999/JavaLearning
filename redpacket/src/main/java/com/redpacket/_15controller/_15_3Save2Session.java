package com.redpacket._15controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 * 注解@SessionAttributes只能用于类
 */
//@Controller
@SessionAttributes(names = {"id","user"},types = {String.class,Object.class})
public class _15_3Save2Session {
    @RequestMapping("/sessionAttr")
    public ModelAndView sessionAttr(Integer id){
        ModelAndView mv = new ModelAndView();
        mv.addObject("user",new Object());
        return mv;
    /**在JSP页面中获取session中的内容
     * <%@ page language="java" import="com.learning.User"
     * contentType="text/html;charset=UTF-8"%>
     * <%
     * User user = (User)session.getAttribute("user");
     * out.println(user.getId());
     * %>
     *
     */
    }

}
