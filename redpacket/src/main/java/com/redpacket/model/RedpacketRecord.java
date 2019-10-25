package com.redpacket.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@Data
public class RedpacketRecord implements Serializable {
    private static final long serialVersionUID = 3722221784693605729L;
    private Integer id;

    private Integer userId;

    private BigDecimal amount;

    private Date sendDate;

    private Integer total;

    private BigDecimal unitAmount;

    private Integer stock;

    private Integer version;

    private String note;
}