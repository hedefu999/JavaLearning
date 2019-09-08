package com.redpacket.service.impl;

import com.redpacket.model.RedpacketRecord;
import com.redpacket.model.RedpacketUser;
import com.redpacket.repository.RedpacketRecordMapper;
import com.redpacket.repository.RedpacketUserMapper;
import com.redpacket.service.RedpacketUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("redpacketUserService")
public class RedpacketUserServiceImpl implements RedpacketUserService {
    @Autowired
    private RedpacketUserMapper redpacketUserMapper;
    @Autowired
    private RedpacketRecordMapper redpacketRecordMapper;
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
    @Override
    public int grabRedpacket(Integer redpacketId, Integer userId) {
        RedpacketRecord redpacketRecord = redpacketRecordMapper.selectByPrimaryKey(redpacketId);
        //库存>0说明还有红包可以抢
        if (redpacketRecord.getStock() > 0){
            redpacketRecordMapper.decreaseRedpacket(redpacketId);
            RedpacketUser redpacketUser = new RedpacketUser();
            redpacketUser.setRedpacketId(redpacketId);
            redpacketUser.setUserId(userId);
            redpacketUser.setAmount(redpacketRecord.getUnitAmount());
            int i = redpacketUserMapper.grabRedpacket(redpacketUser);
            return i;
        }
        return 0;
    }
}
