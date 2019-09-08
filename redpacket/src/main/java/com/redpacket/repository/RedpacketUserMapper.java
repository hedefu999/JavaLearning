package com.redpacket.repository;

import com.redpacket.model.RedpacketUser;

public interface RedpacketUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RedpacketUser record);

    int insertSelective(RedpacketUser record);

    RedpacketUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RedpacketUser record);

    int updateByPrimaryKey(RedpacketUser record);

    int grabRedpacket(RedpacketUser redpacketUser);
}