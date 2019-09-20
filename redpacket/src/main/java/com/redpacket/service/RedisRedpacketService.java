package com.redpacket.service;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Future;

public interface RedisRedpacketService {
    @Deprecated
    void saveUserRedpacketByRedis(Integer redpacketId, BigDecimal unitAmount);

    Future<Integer> asyncSaveRedpacketResult(Integer redpacketId, BigDecimal unitAmount);

}
