package com.redpacket.service;

import com.redpacket.model.RedpacketRecord;
import com.redpacket.model.RedpacketUser;

public interface RedpacketBizService {
    void testVoidAsyncImpact();

    void testNonVoidAsyncImpact();

    void testAsyncTxImpact();

}
