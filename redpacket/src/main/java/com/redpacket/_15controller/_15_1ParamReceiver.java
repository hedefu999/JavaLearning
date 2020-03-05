package com.redpacket._15controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.List;

@Controller  //代码未测试,本类仅用于展示参数接收，返回和链接配置可能是错的
public class _15_1ParamReceiver {
    @RequestMapping("/requestParam")
    @ResponseBody
    public String requestParam(@RequestParam("name")String name){
        return "requestParam: "+name;
    }
    //验证通过
    @RequestMapping("/testJSONReturn")
    public ModelAndView testJSONReturn(){
        ModelAndView mv = new ModelAndView();
        mv.addObject("num",3);
        mv.setView(new MappingJackson2JsonView());
        return mv;
    }
    //这个在WebAppInitializer中似乎配置不上，必须要用通配符
    @RequestMapping("/testPathVariable/{var}")
    @ResponseBody
    public String testPathVar(@PathVariable("var") String name){
        return "index";
    }

    /** 前端的写法
     * $(document).ready(function(){
     *     var data = {
     *         userId:12,
     *         userName:"jack",
     *         pageParam:{
     *             pageIndex:1,
     *             pageSize:10
     *         }
     *     };
     *     $.post({
     *         url:"/receiveJSON",
     * //说明此次请求传递的参数类型，不可缺少
     *         contentType:"application/json",
     * //将JSON数据转为字符串，不可直接传JSON?
     *         data:JSON.stringify(data),
     *         success:function(result){
     *
     *         }
     *     });
     * });
     */
    @RequestMapping("/receiveJSON")
    @ResponseBody
    public String testJSONReceive(@RequestBody RequestPojo requestPojo){
        return "";
    }
    class RequestPojo{
        private Integer userId;
        private String userName;
        private PageParam pageParam;
    }
    class PageParam{
        private Integer pageIndex;
        private Integer pageSize;
    }

    /**
     * var idArray = [1,2,3];
     * $.post({
     *      contentType:"application/json",
     *     data:JSON.stringify(idArray);
     * });
     */
    @RequestMapping("/testArrayReceive")
    public String testArrayReceive(@RequestBody List<String> ids){
        return "testArrayReceive: "+ids.size();
    }

    /**
     * var userList = [
     *  {id:12,name:'jack'},
     *  {id:13,name:'lucy'}
     * ];
     * $.post({
     *     contentType:'application/json',
     *     data:JSON.stringify(userList),
     *     success:function(result){
     *     }
     * });
     * 直接返回个String BadRequest,重定向看下一个试验
     */
    @RequestMapping("/testListReceive")
    public String testListReceive(@RequestBody List<User> userList){
        return "index";
    }
    class User{
        private Integer id;
        private String name;
    }
    /**前端表单的序列化
     * <form id="form">
     *     <table>...</table>
     * </form>
     * $.post({
     *     data: $("form").serialize(),
     * });
     *
     */
}
