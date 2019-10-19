package com.redpacket._16;

import org.apache.http.client.utils.DateUtils;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/advice")
public class MyAdviceController {
    @RequestMapping("/test")
    @ResponseBody
    //date在@initBinder绑定的方法有注册格式，见CommonControllerAdvice
    //@ModelAttribute方法会先于请求方法运行
    public Map<String,Object> testAdvice(Date date,
                                         @NumberFormat(pattern = "##,###.00")BigDecimal amount,
                                         Model model){
        Map<String,Object> map = new HashMap<>();
        map.put("project_name",model.asMap().get("projectName"));
        map.put("date", DateUtils.formatDate(date,"yyyy-MM-dd"));
        map.put("amount",amount);
        return map;
        /**
         * 访问地址 ${host}/advice/test?date=2019-10-19&amount=12,000.78
         * 即可返回
         * {
         * "date": "2019-10-18",
         * "amount": 12000.78,
         * "project_name": "javaLearning"
         * }
         */
    }
    @RequestMapping("/exception")
    public void exeception(){
        throw new RuntimeException("测试异常跳转");
    }

    /**
     * 一旦@ModelAttribute注解标注的方法里有入参，则其他的Controller方法要访问都要带上这里的name入参
     */
    @ModelAttribute("loadedUser")
    public User initUser(@RequestParam("name") String name){
        User user = new User();
        user.setName(name);
        user.setAge(12);
        return user;
    }
    @RequestMapping("/getInitUser")
    @ResponseBody
    public User getUserFromModelAttribute(@ModelAttribute("loadedUser") User loadedUser){
        return loadedUser;
        /**
         * 上述两个方法的访问方法：http://localhost:8903/advice/getInitUser?name=jack
         * {
         * "name": "jack",
         * "age": 12
         * }
         */
    }
}
