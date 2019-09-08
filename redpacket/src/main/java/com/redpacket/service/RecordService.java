package com.redpacket.service;

import com.redpacket.model.RedpacketRecord;
import com.redpacket.model.RedpacketUser;

public interface RecordService {
    RedpacketRecord getRedpacket(Integer id);
    int decreaseRedpacket(Integer id);
}
