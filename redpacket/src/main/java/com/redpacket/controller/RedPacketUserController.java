package com.redpacket.controller;

import com.redpacket.FileUtils;
import com.redpacket.service.RedpacketUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

    /**
     * 上述测试数据是200个红包，耗时计算存在问题
     * 使用redis服务
     * 抢2000个红包浏览器端显示耗时7464ms  触发独立线程保存数据耗时1295ms
     * 优化内容：
     *   同步等待@Async方法的返回结果
     *   并发读取lua脚本SHA冲突问题
     */
    @RequestMapping("/grab-red-packet_with-redis")
    @ResponseBody
    public Map<String,Object> grabRedpacketByRedis(Integer redpacketId,Integer userId){
        /**
         * hset red_packet_5 stock 200
         * hset red_packet_5 unit_amount 100
         *
         * HashOperations hashOps = redisTemplate.opsForHash();
         * hashOps.put("red_packet_1","stock",200);
         * hashOps.put("red_packet_1","unit_amount",100);
         */
        long result = redpacketUserService.grabRedpacketByRedis(redpacketId, userId);
        Map<String,Object> map = new HashMap<>();
        map.put("userId",userId);
        map.put("success",result > 0);
        map.put("message",result>0?"抢红包成功":"抢红包失败");
        return map;
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
        log.info("当前线程名称：{}", Thread.currentThread().getName());
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
    @RequestMapping(value = "/test-file-reading",method = RequestMethod.GET)
    @ResponseBody
    public void testFileReading(String filepath){
        try {
            File file = ResourceUtils.getFile("classpath*:lua/grab_red_packet.lua");

            System.out.println("1"+new FileInputStream(file).available());
        } catch (IOException e) {
        }
        try {
            File file = ResourceUtils.getFile("classpath:lua/grab_red_packet.lua");
            System.out.println("2"+file.length());

        }catch (Exception e){

        }
        try {
            File file = ResourceUtils.getFile("lua/grab_red_packet.lua");
            System.out.println("4"+new FileInputStream(file).available());
        }catch (Exception e){

        }
        //在mvc web app下获取resources下的文件的可用方法，上述spring的ResourceUtils写法都报错
        try {
            //ClassPathResource resource2 = new ClassPathResource("lua/grab_red_packet.lua");
            //InputStream inputStream2 = resource2.getInputStream();
            //System.out.println("3"+inputStream2.available());
            String result = FileUtils.readClassPathFileToString("lua/grab_red_packet.lua");
            System.out.println(result);
        }catch (Exception e){

        }
    }

}
