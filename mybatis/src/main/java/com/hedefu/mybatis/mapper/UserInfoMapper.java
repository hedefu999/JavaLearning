package com.hedefu.mybatis.mapper;

import com.hedefu.mybatis.model.UserInfo;
import com.hedefu.mybatis.qto.UserInfoQto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface UserInfoMapper {
    UserInfo getByComplicatedResultMap(UserInfoQto qto);

    UserInfo getUserInfoByQueryMap(Map<String,Object> paramMap);

    UserInfo getUserInfoByQto(UserInfoQto qto);

    List<UserInfo> getByRowBounds(int age, RowBounds rowBounds);

    int insertUserInfo(UserInfo userInfo);

    int insertUserInfoWithCustomizedKeyGenRule(UserInfo userInfo);
}
