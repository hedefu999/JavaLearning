package com.redpacket.repository;

import com.redpacket.model.RedpacketUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RedpacketUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RedpacketUser record);

    int insertSelective(RedpacketUser record);

    RedpacketUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RedpacketUser record);

    int updateByPrimaryKey(RedpacketUser record);

    int grabRedpacket(RedpacketUser redpacketUser);

    int getGrabRecordCount(@Param("redpacketId") int redpacketId);

    List<RedpacketUser> getGrabRecord(@Param("redpacketId") int redpacketId,
                                      @Param("offset") int offset,
                                      @Param("count") int count);
}