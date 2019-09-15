package com.redpacket.controller;

import com.redpacket.service.RedpacketUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/redpacket-user")
public class RedPacketUserController {
    private final Logger log = LoggerFactory.getLogger(RedPacketUserController.class);

    @Autowired
    private RedpacketUserService redpacketUserService;

    private Map<String,Object> createResult(Integer userId, Integer result){
        //log.info("service返回的抢红包结果是：{}", result);
        Map<String,Object> retMap = new HashMap<>();
        String msg = result == 0?"抢红包失败":result == 1?"抢红包成功":"抢红包成功但保存记录失败";
        retMap.put("userId",userId);
        retMap.put("success",result);
        retMap.put("message",msg);
        return retMap;
    }

    /*-=-=-=-=-=下面是四种抢红包的方案，实用性逐步提升-=-=-=-=-=*/
    /**
     * 让250人抢200个红包，在本机就能出现超发的情况：redpacket_user出现201条记录，redpacket_record最终stock为-1
     * 如果业务处理耗时较长则红包超发现象更严重，如在RedpacketUserServiceImpl#grabRedpacket添加Thread.sleep(500)则可观测到stock最终为-4
     * 共耗时1000ms
     */
    @RequestMapping("/grab-red-packet")
    @ResponseBody//由于使用了@ResponseBody，最终会转变为一个JSON返回回来
    public Map<String,Object> grabRedPacket(Integer redPacketId,Integer userId){
        int result = redpacketUserService.grabRedpacket(redPacketId,userId);
        return createResult(userId, result);
    }
    /**
     * 抢红包失败后的处理方案
     * 1. 直接返回用户红包争抢失败，在并发时version不一致时
     * 测试时发现只有68位用户抢到了红包,耗时1000ms
     */
    @RequestMapping("/grab-red-packet_with-version")
    @ResponseBody//由于使用了@ResponseBody，最终会转变为一个JSON返回回来
    public Map<String,Object> grabRedPacketWithVersion(Integer redPacketId,Integer userId){
        int result = redpacketUserService.grabRedpacketWithVersion(redPacketId,userId);
        if (result == 0){
            log.info("红包抢占失败");
        }
        return createResult(userId, result);
    }
    /**
     * 2. 定时重试机制
     * 红包无超发少发现象，耗时11.98s
     */
    @RequestMapping("/grab-red-packet_retry-fixed-duration")
    @ResponseBody
    public Map<String,Object> grabRetryByFixedDuration(Integer redPacketId,Integer userId){
        int result = redpacketUserService.retryGrabByDuration(redPacketId,userId);
        if (result == 0){
            log.info("限时100ms没抢到");
        }
        return createResult(userId, result);
    }
    /**
     * 3. 固定次数重试
     * BUGFIX 每次只能发出一个红包- 误将userId作为redpacketId传入
     * 红包无超发漏发现象，耗时2.82s
     */
    @RequestMapping("/grab-red-packet_retry-fixed-times")
    @ResponseBody
    public Map<String,Object> grabRetryByFixedTimes(Integer redPacketId,Integer userId){
        int result = redpacketUserService.retryGrabByTimes(redPacketId,userId);
        if (result == 0){
            log.info("可以重试3次也没抢到");
        }
        return createResult(userId, result);
    }



    @RequestMapping(value = "/grab",method = RequestMethod.GET)
    public ModelAndView grab(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("grab");
        return modelAndView;
    }
    @RequestMapping(value = "/test-return",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> testReturn(){
        Map<String,Object> map = new HashMap<>();
        map.put("success",true);
        map.put("data",new UserInfo("E001",12));
        map.put("code","");
        map.put("msg","");
        return map;
    }
    class UserInfo{
        private String userId;
        private Integer amount;

        public UserInfo(String userId, Integer amount) {
            this.userId = userId;
            this.amount = amount;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public Integer getAmount() {
            return amount;
        }

        public void setAmount(Integer amount) {
            this.amount = amount;
        }
    }

}
