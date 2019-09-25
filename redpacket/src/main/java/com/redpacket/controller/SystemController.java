package com.redpacket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/index")
public class SystemController {
    /**
     * 在dispatcher-servlet.xml中配置<mvc:view-controller path="/" view-name="index">
     *     也可以达到配置默认主页的目的
     */
    @RequestMapping(value = "/main")
    public String index(){
        return "index";
    }



}
