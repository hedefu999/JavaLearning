package com.redpacket.controller;

import com.redpacket.service.RedpacketBizService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/manyfunc")
public class ManyFunctions {
    private final Logger log = LoggerFactory.getLogger(ManyFunctions.class);
    @Autowired
    private RedpacketBizService redpacketBizService;

    @RequestMapping("/testVoidAsyncImpact")
    public void testVoidAsyncImpact(){
        redpacketBizService.testVoidAsyncImpact();
    }

    @RequestMapping("/testNonVoidAsyncImpact")
    public void testNonVoidAsyncImpact(){
        redpacketBizService.testNonVoidAsyncImpact();
    }


    @RequestMapping("/testAsyncTxImpact")
    public String testAsyncTxImpact(ModelMap modelMap){
        redpacketBizService.testAsyncTxImpact();
        return "/target";
    }
}
