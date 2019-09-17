-- 缓存抢红包列表信息列表key,KEY[1]是程序中传入的redpacketId, ..表示字符串拼接
local listKey = 'red_packet_list_'..KEYS[1]
-- 当前被抢红包key
local redPacket = 'red_packet_'..KEYS[1]
-- 获取当前红包库存
local stock = tonumber(redis.call('hget',redPacket,'stock'))
-- 库存耗尽，返回0
if stock <= 0 then return 0 end
-- 库存减1
stock = stock - 1
-- 保存当前库存
redis.call('hset',redPacket,'stock',tostring(stock))
-- 往链表中加入当前红包信息，链表的key为red_packet_list_{id}
redis.call('rpush',listKey,ARGV[1])
-- 如果是最后一个红包，则返回2，表示抢红包结束，需要持久化缓存数据
if stock == 0 then return 2 end
-- 不是最后一个红包，返回1，可以继续抢
return 1

-- 书中提到的一些现实世界技术点
-- 现实世界中存在限时抢红包的情况，如果在固定时间内未抢完，使用超时触发机制；
-- 最后一次抢红包成功时持久化的数据量比较大，为不影响下一个进入的请求，会使用JMS向其他服务器发送消息；