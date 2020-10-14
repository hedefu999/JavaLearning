package com.changingedu.share;

import com.qingqing.common.domain.order.OrderType;
import com.qingqing.common.intf.Composer;
import com.qingqing.common.util.CollectionsUtil;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Java8StreamAPI {

    interface GroupSubOrderService{
        List<GroupSubOrder> getByStudentIdAndOrderType(Long studentId, OrderType orderType);
    }
    interface QingQingPaymentService{
        List<QingQingPayment> getByRelatedOrderIds(Collection<Long> orderIds);
    }
    interface CourseSvcManager{
        List<OrderCourseStatistics> getStatisticsByOrderIds(Collection<Long> orderIds);
    }
    @Data
    static class GroupSubOrder{
        public static Composer<Long,GroupSubOrder> ID_COMPOSER = new Composer<Long, GroupSubOrder>() {
            @Override
            public Long getComposerId(GroupSubOrder order) {
                return order.getOrderId();
            }
        };
        private Long orderId;
        private String orderName;
    }
    @Data
    static class QingQingPayment{
        public static Composer<Long,QingQingPayment> ID_COMPOSER = payment -> payment.getRelatedOrderId();
        private Long relatedOrderId;
        private BigDecimal price;
    }
    @Data
    static class OrderCourseStatistics{
        public static Composer<Long,OrderCourseStatistics> ID_COMPOSER = orderCourseStat -> orderCourseStat.getOrderId();
        private Long orderId;
        private Integer total;
        private Integer left;
    }
    @Data
    static class OrderDto{
        private Long orderId;
        private String orderName;
        private Double price;
        private Integer totalCourseNum;
        private Integer leftCourseNum;
    }
    private static GroupSubOrderService groupSubOrderService;
    private static QingQingPaymentService paymentService;
    private static CourseSvcManager courseSvcManager;
    /**
     * #001-代码对比
     * 业务开发中的一个常见操作：
     * 给定一个学生id
     * 从订单表t_group_sub_order表里查到数据
     * 还需要从coursesvc查询订单内的课程总数和剩余课程数量
     * 从t_payment表补上对应的支付数据
     * 最后得到一个DTO
     */
    static class APIComparison{
        static class OrderConverter{
            public static OrderDto toOrderDto(GroupSubOrder order, Map<Long, QingQingPayment> paymentMap, Map<Long, OrderCourseStatistics> orderCourseStatMap){
                OrderDto orderDto = new OrderDto();
                Long orderId = order.getOrderId();
                orderDto.setOrderId(orderId);
                orderDto.setOrderName(order.getOrderName());
                Double price = Optional.ofNullable(paymentMap.get(orderId)).map(QingQingPayment::getPrice).map(BigDecimal::doubleValue).orElse(null);
                orderDto.setPrice(price);
                OrderCourseStatistics orderCourseStat = orderCourseStatMap.get(orderId);
                if (orderCourseStat != null){
                    orderDto.setTotalCourseNum(orderCourseStat.getTotal());
                    orderDto.setLeftCourseNum(orderCourseStat.getLeft());
                }
                return orderDto;
            }
        }

        public static List<OrderDto> useFormerAPI(Long studentId){
            if (studentId == null){
                return Collections.emptyList();
            }
            List<GroupSubOrder> orders = groupSubOrderService.getByStudentIdAndOrderType(studentId,OrderType.class_hour_v2_order_type);
            List<Long> orderIds = new ArrayList<>(orders.size());
            for (GroupSubOrder subOrder : orders){
                orderIds.add(subOrder.getOrderId());
            }
            List<QingQingPayment> payments = paymentService.getByRelatedOrderIds(orderIds);
            Map<Long,QingQingPayment> paymentMap = new HashMap<>(payments.size());
            for (QingQingPayment payment : payments){
                paymentMap.put(payment.getRelatedOrderId(), payment);
            }
            List<OrderCourseStatistics> orderCourseStats = courseSvcManager.getStatisticsByOrderIds(orderIds);
            Map<Long, OrderCourseStatistics> orderCourseStatMap = new HashMap<>();
            for (OrderCourseStatistics orderCourseStat : orderCourseStats){
                orderCourseStatMap.put(orderCourseStat.getOrderId(), orderCourseStat);
            }
            List<OrderDto> orderDtos = new ArrayList<>(orders.size());
            for (GroupSubOrder order : orders){
                orderDtos.add(OrderConverter.toOrderDto(order, paymentMap, orderCourseStatMap));
            }
            return orderDtos;
        }

        //可能有第三方类库通过泛型或是定义接口简化编程
        public static List<OrderDto> useCustomizedAPI(Long studentId){
            if (studentId == null){
                return Collections.emptyList();
            }
            List<GroupSubOrder> orders = groupSubOrderService.getByStudentIdAndOrderType(studentId,OrderType.class_hour_v2_order_type);
            List<Long> orderIds = CollectionsUtil.getComposerIds(orders, GroupSubOrder.ID_COMPOSER);
            List<QingQingPayment> payments = paymentService.getByRelatedOrderIds(orderIds);
            Map<Long, QingQingPayment> paymentMap = CollectionsUtil.mapComposerId(payments, QingQingPayment.ID_COMPOSER);
            List<OrderCourseStatistics> courseStatistics = courseSvcManager.getStatisticsByOrderIds(orderIds);
            Map<Long, OrderCourseStatistics> orderCourseStatMap = CollectionsUtil.mapComposerId(courseStatistics,OrderCourseStatistics.ID_COMPOSER);
            List<OrderDto> orderDtos = new ArrayList<>(orders.size());
            for (GroupSubOrder order : orders){
                orderDtos.add(OrderConverter.toOrderDto(order, paymentMap, orderCourseStatMap));
            }
            return orderDtos;
        }

        public static List<OrderDto> useJava8StreamAPI(Long studentId) {
            if (studentId == null){
                return Collections.emptyList();
            }
            List<GroupSubOrder> orders = groupSubOrderService.getByStudentIdAndOrderType(studentId,OrderType.class_hour_v2_order_type);
            List<Long> orderIds = orders.stream().map(GroupSubOrder::getOrderId).collect(Collectors.toList());//A
            List<QingQingPayment> payments = paymentService.getByRelatedOrderIds(orderIds);
            Map<Long, QingQingPayment> paymentMap = payments.stream().collect(Collectors.toMap(QingQingPayment::getRelatedOrderId, item -> item));//B
            List<OrderCourseStatistics> courseStatistics = courseSvcManager.getStatisticsByOrderIds(orderIds);
            Map<Long, OrderCourseStatistics> orderCourseStatMap = courseStatistics.stream().collect(HashMap::new, (map, item) -> map.put(item.getOrderId(), item), HashMap::putAll);//C
            List<OrderDto> orderDtos = orders.stream().map(item -> OrderConverter.toOrderDto(item, paymentMap, orderCourseStatMap)).collect(Collectors.toList());//D 不要在业务代码里写get set
            return orderDtos;
        }
        /**
         * 引出问题
         */
    }


}
