package com.redpacket._15_5validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class OrderValidator implements Validator {
    private final Logger log = LoggerFactory.getLogger(OrderValidator.class);

    @Override
    public boolean supports(Class<?> clazz) {
        //验证是否是Transaction类
        return Order.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Order trans = (Order) target;
        log.info("验证订单交易数据对象：{}", trans);
        int comp = trans.getDiscount().add(trans.getTotal()).compareTo(trans.getPrice());
        if (comp != 0){
            errors.rejectValue("total","COMMON SENSE","应付金额+折扣!= 标价");
            errors.rejectValue("price","COMMON SENSE","应付金额+折扣!= 标价");
        }
        comp = trans.getOwed().add(trans.getPaid()).compareTo(trans.getTotal());
        if (comp != 0){
            errors.rejectValue("paid","COMMON SENCE","总价不等于已付+未付");
        }
    }
}
