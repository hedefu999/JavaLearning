package com.redpacket.service;

public interface RedpacketUserService {
    int grabRedpacket(Integer redpacketId, Integer userId);
    int grabRedpacketWithVersion(Integer redpacketId, Integer userId);
    /**
     * 固定时长的重试
     * 这种策略存在的缺陷：系统空闲和繁忙时抢红包业务的执行时长不一致，导致不同用户重试的次数不一样
     */
    int retryGrabByDuration(Integer redpacketId, Integer userId);
    //固定次数的重试
    int retryGrabByTimes(Integer redpacketId, Integer userId);

    long grabRedpacketByRedis(Integer redpacketId, Integer userId);

}
