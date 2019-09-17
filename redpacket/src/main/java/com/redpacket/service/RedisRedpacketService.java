package com.redpacket.service;

import java.math.BigDecimal;

public interface RedisRedpacketService {
    void saveUserRedpacketByRedis(Integer redpacketId, BigDecimal unitAmount);
}
