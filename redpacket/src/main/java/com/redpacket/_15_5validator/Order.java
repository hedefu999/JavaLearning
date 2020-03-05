package com.redpacket._15_5validator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class Order {
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

    public static void main(String[] args) throws JsonProcessingException {
        Order tx = new Order();
        tx.setPrice(new BigDecimal("23.0"));
        tx.setDiscount(new BigDecimal("23.0"));
        tx.setTotal(new BigDecimal("23.0"));
        tx.setPaid(new BigDecimal("23.0"));
        tx.setOwed(new BigDecimal("23.0"));
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(tx);
        System.out.println(s);
    }
}
