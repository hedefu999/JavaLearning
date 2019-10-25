package com.redpacket.service;

import com.redpacket.model.RedpacketRecord;
import com.redpacket.model.RedpacketUser;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface RedpacketBizService {
    void testVoidAsyncImpact();

    void testNonVoidAsyncImpact();

    void processRedpacketBizLogic();

    void processCommonBizLogic(Integer redpacketId);
}
