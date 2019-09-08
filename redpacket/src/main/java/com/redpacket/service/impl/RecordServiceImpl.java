package com.redpacket.service.impl;

import com.redpacket.model.RedpacketRecord;
import com.redpacket.repository.RedpacketRecordMapper;
import com.redpacket.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("recordService")
public class RecordServiceImpl implements RecordService {
    @Autowired
    private RedpacketRecordMapper redpacketRecordMapper;
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
    @Override
    public RedpacketRecord getRedpacket(Integer id) {
        return redpacketRecordMapper.selectByPrimaryKey(id);
    }

    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
    @Override
    public int decreaseRedpacket(Integer id) {
        return redpacketRecordMapper.decreaseRedpacket(id);
    }
}
