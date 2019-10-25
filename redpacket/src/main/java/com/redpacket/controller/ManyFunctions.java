package com.redpacket.controller;

import com.redpacket.service.RedpacketBizService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/manyfunc")
@ResponseBody
public class ManyFunctions {
    private final Logger log = LoggerFactory.getLogger(ManyFunctions.class);
    @Autowired
    private RedpacketBizService redpacketBizService;

    @RequestMapping("/testVoidAsyncImpact")
    public void testVoidAsyncImpact(){
        redpacketBizService.testVoidAsyncImpact();
/**
 * 外层方法执行线程名：qtp902830499-38 start = 1571985670416,end = 1571985670416, 总耗时 = 0
 *         内部方法执行在线程redpacket-taskExecutor-2上，start = 1571985670417 end = 1571985671744 执行耗时 = 1327
 *         这表明独立线程方法不影响顶层调用方法立即返回
 */
    }

    @RequestMapping("/testNonVoidAsyncImpact")
    public void testNonVoidAsyncImpact(){
        redpacketBizService.testNonVoidAsyncImpact();
/**
 * --- 当futureResult在1000ms内等待并返回时
 * 外层方法执行线程名：qtp902830499-36 开始时间 = 1571986271817,调用完成时间点：1571986271817, 总耗时 = 0
 * 等待耗时方法执行，未能及时返回结果：java.util.concurrent.TimeoutException，返回时间点：1571986272818,最终耗时：1001
 * 内层方法执行在线程redpacket-taskExecutor-3上，start = 1571986271817 end = 1571986273143 执行耗时 = 1326
 *
 * --- 当futureResult在2000ms内等待并返回时
 * 外层方法执行线程名：qtp902830499-114 开始时间 = 1571986462309,调用完成时间点：1571986462309, 总耗时 = 0
 * 内层方法执行在线程redpacket-taskExecutor-4上，start = 1571986462309 end = 1571986463638 执行耗时 = 1329
 * 最终返回结果：success, 执行完成时间点：1571986463638，总耗时：1329
 */
    }


    @RequestMapping("/testAsyncTxImpact")
    public String testAsyncTxImpact(ModelMap modelMap){
        redpacketBizService.processRedpacketBizLogic();
        return "success";
/**
 * 对方法
 * com.redpacket.service.impl.RedpacketServiceImpl#processCommonBizLogic(java.lang.Integer)
 * 进行一些修改，可以得到不同的数据库操作效果
 *
 * 在没有@Async注解和内部异常抛出时
 * 当前操作所处线程：com.redpacket.service.impl.RedpacketBizServiceImpl.processRedpacketBizLogic, 保存操作影响的行数：1，返回的主键 = 6,将默认扣减一个红包作为平台分成！
 * 当前所处事务的名字：com.redpacket.service.impl.RedpacketBizServiceImpl.processRedpacketBizLogic, 扣减红包操作数据库记录行数：1
 * 仅加入内部异常时不会插入任何记录，更无法扣减
 *
 * 仅打开@Async注解时
 * 当前操作所处线程：com.redpacket.service.impl.RedpacketBizServiceImpl.processRedpacketBizLogic, 保存操作影响的行数：1，返回的主键 = 8,将默认扣减一个红包作为平台分成！
 * 当前所处事务的名字：com.redpacket.service.impl.RedpacketServiceImpl.processCommonBizLogic, 扣减红包操作数据库记录行数：1
 * 数据操作结果与没有@Async一致，但processCommonBizLogic方法所处的事务不是从processRedpacketBizLogic传递来的
 *
 * 同时打开@Async和异常抛出时
 * 当前操作所处线程：com.redpacket.service.impl.RedpacketBizServiceImpl.processRedpacketBizLogic, 保存操作影响的行数：1，返回的主键 = 9,将默认扣减一个红包作为平台分成！
 * 当前所处事务的名字：com.redpacket.service.impl.RedpacketServiceImpl.processCommonBizLogic, 扣减红包操作数据库记录行数：1
 * 运行日志中没有异常打印出来，红包记录保存成功，但没有扣减成功，独立线程的方法在新的事务中把数据库操作回滚掉了
 * 跨线程外层事务将失效！
 *
 * 如果@Async方法与调用方法位于同一个类
 * 虽然被调方法也能在独立线程中运行，但因为自调用事务无法新建，同样会回滚调用方法，所以应注意方法在不同的类中的分布
  */
    }
}
