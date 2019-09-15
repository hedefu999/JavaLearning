package com.redpacket.repository;

import com.redpacket.model.RedpacketRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RedpacketRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(RedpacketRecord record);

    RedpacketRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RedpacketRecord record);

    int decreaseRedpacket(Integer id);

    int decreaseRedpacketWithVersion(@Param("redpacketId") Integer redpacketId, @Param("version") Integer version);
}