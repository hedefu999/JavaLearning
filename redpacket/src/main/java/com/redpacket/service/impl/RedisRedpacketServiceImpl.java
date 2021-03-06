package com.redpacket.service.impl;

import com.redpacket.model.RedpacketUser;
import com.redpacket.service.RedisRedpacketService;
import com.redpacket.service.RedpacketBasicService;
import com.utils.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * 这里有两个版本的保存方法
 */
@Service
public class RedisRedpacketServiceImpl implements RedisRedpacketService {
    private final Logger log = LoggerFactory.getLogger(RedisRedpacketServiceImpl.class);
    private static final String PREFIX = "red_packet_list_";
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //每次取出100条
    private static final int FETCH_SIZE = 100;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private DataSource dataSource;

    /**
     * 持久化数据需要保存的表是redpacket-user
     * 需要具体信息是
     * 红包id - 入参之一
     * userId - 从redis中取一个名称为 red_packet_list_+redpacketId的链表，链表存储了userId-grab_time这样的数据，按'-'分割即可
     * grab_time 同上
     * 每个红包的金额 - 入参之一
     */
    @Deprecated
    @Override
    @Async("taskExecutor") //开启新线程运行保存操作,设置要使用的线程池名是taskExecutor
    public void saveUserRedpacketByRedis(Integer redpacketId, BigDecimal unitAmount) {
        log.info("开始保存数据");
        long start = System.currentTimeMillis();
        BoundListOperations listOps = redisTemplate.boundListOps(PREFIX+redpacketId);
        long size = listOps.size();//列表的大小
        //计算需要取的次数
        long times = size%FETCH_SIZE == 0?size/FETCH_SIZE:size/FETCH_SIZE+1;
        int count = 0;
        List<RedpacketUser> redpacketUsers = new ArrayList<>(FETCH_SIZE);
        for (int i = 0; i < times; i++) {
            List userIdList = null;
            if (i == 0){
                //第一次取101条，以后都是每次100个，除非取到了零头
                userIdList = listOps.range(i*FETCH_SIZE,(i+1)*FETCH_SIZE);
            }else {
                userIdList = listOps.range(i*FETCH_SIZE+1,(i+1)*FETCH_SIZE);
            }
            redpacketUsers.clear();
            for (int j = 0; j < userIdList.size(); j++) {
                String args = userIdList.get(j).toString();
                //数据格式的设置见RedpacketUserServiceImpl
                String[] arr = args.split("-");
                String userIdStr = arr[0];
                String timeStr = arr[1];
                Integer userId = Integer.valueOf(userIdStr);
                long time = Long.parseLong(timeStr);

                RedpacketUser user = new RedpacketUser();
                user.setRedpacketId(redpacketId);
                user.setUserId(userId);
                user.setAmount(unitAmount);
                user.setGrabTime(new Timestamp(time));
                redpacketUsers.add(user);
            }
            //保存抢红包信息
            count += executeBatch(redpacketUsers);
        }
        redisTemplate.delete(PREFIX+redpacketId);
        long end = System.currentTimeMillis();
        log.info("保存数据耗时{}毫秒，共计{}条记录。",end-start,count);
    }

    /**
     * 书中使用JDBC批量处理保存redis缓存数据，返回保存数量
     * 不知道为啥要这样写
     */
    private int executeBatch(List<RedpacketUser> redpacketUsers) {
        Connection conn = null;
        Statement stmt = null;
        int[] count = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            for (RedpacketUser user : redpacketUsers){
                String decreaseStockSql = "update redpacket_record set stock = stock - 1 where id = "+user.getRedpacketId();
                String saveUserSql = "insert into redpacket_user(redpacket_id,user_id,amount,grab_time)values("+
                        user.getRedpacketId()+","+user.getUserId()+","+user.getAmount()+",'"+dateFormat.format(user.getGrabTime())+"')";
                stmt.addBatch(decreaseStockSql);
                stmt.addBatch(saveUserSql);
            }
            count = stmt.executeBatch();
            conn.commit();//提交事务
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (conn != null && !conn.isClosed()){
                    conn.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return count.length/2;//成功发放红包个数
    }

    /*-=-=-=-=-=-= 我的改进方法 =-=-=-=-=-=*/
    @Autowired
    private RedpacketBasicService redpacketBizService;
    /**
     * 异步方法任务可以使用@Async注解新开线程操作
     * 方法返回void时可以避免阻塞主线程，需要同步等待时就需要返回Future<T>,返回其他类型会框架报错：Null return value from advice does not match primitive return type for
     */
    @Async("taskExecutor")
    @Override
    public Future<Integer> asyncSaveRedpacketResult(Integer redpacketId, BigDecimal unitAmount){
        long start = System.currentTimeMillis();
        BoundListOperations<String,String> boundListOps = redisTemplate.boundListOps(PREFIX + redpacketId);
        int size = Math.toIntExact(boundListOps.size());
        log.info("需要保存{}条数据", size);
        PageHelper pageHelper = new PageHelper(size,FETCH_SIZE);
        int saveCount = 0;
        for (int i = 0; i < pageHelper.getPageCount(); i++) {
            //redisTemplate的range是包含两头的元素的
            List<String> list = boundListOps.range(pageHelper.getPageStart(i)-1, pageHelper.getPageEnd(i)-1);
            //为避免自调用导致事务失效，把saveRedpacketUsers放到另一个BizService
            saveCount += redpacketBizService.saveRedpacketUsers(redpacketId,unitAmount,list);
        }
        log.info("异步保存数据耗时：{}", System.currentTimeMillis() - start);//289ms
        Future<Integer> asyncResult = new AsyncResult<>(saveCount);
        //等待两秒验证主线程的反映：主线程等待了2000+上面的数据库操作耗时
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("延时等待2秒完成");
        return asyncResult;
    }

    @Transactional
    public int saveRedpacketUsers(Integer redpacketId, BigDecimal unitAmount, List<String> userIdTimestampList){
        //many codes
        return 0;
    }


}
