package com.redpacket._16;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

/**
 * 自定义一个转换器，在WebConfig中进行注册，实现?user=jack-12 也能正常接收数据
 */
public class String2UserConverter implements Converter<String, User> {
    @Override
    public User convert(String source) {
        if (StringUtils.isEmpty(source)){
            return null;
        }
        if (!source.contains("-")){
            return null;
        }
        String[] split = source.split("-");
        if (split.length != 2){
            return null;
        }
        User user = new User();
        user.setName(split[0]);
        user.setAge(Integer.valueOf(split[1]));
        return user;
    }
}
