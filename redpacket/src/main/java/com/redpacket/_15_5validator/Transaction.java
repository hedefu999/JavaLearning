package com.redpacket._15_5validator;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class Transaction {
    //标价
    private BigDecimal price;
    //折扣
    private BigDecimal discount;
    //实际应付总额
    private BigDecimal total;
    //已支付
    private BigDecimal paid;
    //未支付
    private BigDecimal owed;

}
