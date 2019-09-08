package com.redpacket.repository;

import com.redpacket.model.RedpacketRecord;
import org.springframework.stereotype.Repository;

@Repository
public interface RedpacketRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RedpacketRecord record);

    int insertSelective(RedpacketRecord record);

    RedpacketRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RedpacketRecord record);

    int updateByPrimaryKey(RedpacketRecord record);

    int decreaseRedpacket(Integer id);
}