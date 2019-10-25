package com.redpacket.service;

import com.redpacket.model.RedpacketRecord;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Future;

public interface RedpacketBasicService {
    int saveRedpacketUsers(Integer redpacketId, BigDecimal unitAmount, List<String> userIdTimestampList);
    RedpacketRecord getRedpacket(Integer id);
    int decreaseRedpacket(Integer id);
    //保存记录并返回主键
    int saveRedpacket(RedpacketRecord record);

    void doTimeConsumedOperation();

    Future<String> doHasFeedBackOperation();
}
