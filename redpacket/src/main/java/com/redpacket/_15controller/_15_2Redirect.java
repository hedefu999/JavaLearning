package com.redpacket._15controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller
public class _15_2Redirect {
    /**
     * 得到报错后回填的数据库主键，设置好model
     * 转到信息的jsp显示结果
     * springMVC的一个约定：返回的字符串带有redirect时，就会认为是一个重定向
     */
    @RequestMapping("/saveUser")
    public String saveRole(Model model,String roleName, String note){
        int saveResult = 0;//userService.insertData(new User("jack",12));
        model.addAttribute("roleName","...");
        model.addAttribute("id",saveResult);
        return "redirect:/showUserInfo";
    }
    /**
     * 通过返回视图和模型来实现重定向
     */
    @RequestMapping("/addUser")
    public ModelAndView addUser(ModelAndView mv, String userName,String note){
        mv.addObject("userName",userName);
        mv.setViewName("redirect:/showUserInfo");
        return mv;
    }
    /**
     * 在URL重定向时，并不能有效传递对象，HTTP的重定向参数是以字符串传递的
     * springMVC提供了一个RedirectAttribute数据模型
     * 使用addFlashAttribute方法后，springMVC会将数据保存到session中，在重定向后可以传递到下一个地址
     */
    @RequestMapping("/insertUser")
    public String insertUser(RedirectAttributes ra, Object object){
        ra.addFlashAttribute("user",object);
        return "redirect:/showUserInfo";
    }

    @RequestMapping("/testInterceptor")
    public String testInterceptor(){
        return "redirect:/index/main";
    }

}
