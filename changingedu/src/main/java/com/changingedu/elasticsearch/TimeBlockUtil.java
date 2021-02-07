package com.qingqing.search.core.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.qingqing.common.intf.TimeBlockInterface;
import com.qingqing.common.util.CollectionsUtil;
import com.qingqing.common.util.TimeUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TimeBlockUtil {

    public final static int DEFAULT_TIMEZONE = +8;

    // 时间块改造30->5
    public final static int MIDNIGHT_BLOCK = -96;
    public final static int NEW_MAX_END_BLOCK = 191;
    //一个30分钟时间块对应6个5分钟时间块。
    public final static int THIRTY_FIVE_COEFFICIENT = 6;
    //一天24*60分，每5分钟为一个时间块，共288个时间块，Long长度8字节共64位，这里取60个使用，共拼成5个Long,最后一个不足60位
    public final static int TIME_BLOCK_BINARY_INTERVAL = 60;


    /**
     * @description 从原有package com.qingqing.search.sync.util.teachertime.TimeBlockUtil迁移过来;
     * @auther fanziqi
     * @param timeBlockInterfaces
     * @return
     */
    public static final <T extends TimeBlockInterface> List<Integer> getBlockListFromObject(List<T> timeBlockInterfaces) {
        Set<Integer> blcoks = Sets.newHashSet();
        for (TimeBlockInterface blockInterface : timeBlockInterfaces) {
            blcoks.addAll(getAllNumbersBetweenTwoBlocks(blockInterface.getStartBlock(), blockInterface.getEndBlock()));
        }

        List<Integer> result = Lists.newArrayList(blcoks);
        Collections.sort(result);
        return result;
    }

    /**
     * @description 从原有package com.qingqing.search.sync.util.teachertime.TimeBlockUtil迁移过来;
     * @auther fanziqi
     * @param startBlock
     * @param endBlock
     * @return
     */
    public static final List<Integer> getAllNumbersBetweenTwoBlocks(int startBlock, int endBlock) {
        List<Integer> blocks = new ArrayList<>();
        for (int i = startBlock; i <= endBlock; i++) {
            blocks.add(i);
        }
        return blocks;
    }

    /**
     * @description 从原有package com.qingqing.search.sync.util.teachertime.TimeBlockUtil迁移过来并做30Block转5变更;
     * @auther fanziqi
     * @param date
     * @return
     */
    public static int toFiveStartBlock(Date date) {
        Date day = TimeUtil.formatDate(date, TimeUtil.DATE_TO_YEAR_MONTH_DAY);
        long milliSeconds = date.getTime() - day.getTime();
        long step = 5L * 60 * 1000;
        Long timeBolck = milliSeconds - 12 * step * DEFAULT_TIMEZONE;
        int blockIndex = (int)(timeBolck / step);
        //不能用blockIndex判断小于0，原因：-0.8会直接取为0
        //小于0时候且不为整点的时候必须减1
        if (timeBolck<0 && (timeBolck % step != 0)){
            blockIndex--;
        }
        return blockIndex;
    }

    /**
     * @description 从原有package com.qingqing.search.sync.util.teachertime.TimeBlockUtil迁移过来并做30Block转5变更;
     * 注意：5分钟块最小值是-96，如果是入参是00:00:00 那么会返回-97注意处理
     * @auther fanziqi
     * @param date
     * @return
     */
    public static int toFiveEndBlock(Date date) {
        Date day = TimeUtil.formatDate(date, TimeUtil.DATE_TO_YEAR_MONTH_DAY);
        long milliSeconds = date.getTime() - day.getTime();
        long step = 5L * 60 * 1000;
        Long timeBolck = milliSeconds - 12 * step * DEFAULT_TIMEZONE;
        int blockIndex = (int)(timeBolck / step);
        //timeBolck小于0必须减一 或者  大于0且整点半点的时候必须减一
        if ((timeBolck < 0) || ((timeBolck % step) == 0)){
            blockIndex--;
        }
        return blockIndex;
    }

    /**
     *  将列表中对象的startBlock~endBlock30分钟时间块转换成5分钟时间块对应的值
     */
    public static void  thirtyBlockObjList2FiveBlockObjList(List<? extends TimeBlockInterface> timeBlockList) {
        for (TimeBlockInterface blockInterface : timeBlockList) {
            blockInterface.setStartBlock(blockInterface.getStartBlock()!=null ?
                    thirtyStartBlock2FiveStartBlock(blockInterface.getStartBlock()) : null);
            blockInterface.setEndBlock(blockInterface.getEndBlock()!=null ?
                    thirtyEndBlock2FiveEndBlock(blockInterface.getEndBlock()) : null);
        }
    }

    //30分钟时间块值转5分钟时间块值工具方法（反之）
    // -16 对应 [-96, -95, -94, -93, -92, -91]  结果-96
    public static Integer thirtyStartBlock2FiveStartBlock(Integer blockValue) {
        if (blockValue == null)
            return null;
        return blockValue * THIRTY_FIVE_COEFFICIENT;
    }

    // -16 对应 [-96, -95, -94, -93, -92, -91]  结果-91
    public static Integer thirtyEndBlock2FiveEndBlock(Integer blockValue) {
        if (blockValue == null)
            return null;
        return (blockValue + 1) * THIRTY_FIVE_COEFFICIENT - 1;
    }

    //[-96, -95, -94, -93, -92, -91, -90, -89, -88, -87, -86, -85]
    //如果是-96 那么返回 -16 如果是-95，说明对应的30分钟块-16位置有一个-96不能用导致得去找下一个30分钟可用的块，也就是-15
    //向后取完整可用的30分钟块
    public static Integer fiveStartBlock2ThirtyStartBlock(Integer blockValue) {
        if (blockValue == null)
            return null;
        return blockValue  / THIRTY_FIVE_COEFFICIENT;
    }
    //[-96, -95, -94, -93, -92, -91, -90, -89, -88, -87, -86, -85]
    //如果是-96, -95, -94, -93, -92 那么返回 -17 如果是-91，那就是这个块可以用返回-16
    //向前取完整可用的30分钟块
    public static Integer fiveEndBlock2ThirtyEndBlock(Integer blockValue) {
        if (blockValue == null)
            return null;
        if ((blockValue + 1) % THIRTY_FIVE_COEFFICIENT == 0) {
            return (blockValue + 1)  / THIRTY_FIVE_COEFFICIENT - 1;
        }else {
            return (blockValue + 1)  / THIRTY_FIVE_COEFFICIENT - 2;
        }
    }

    //30分钟一个块对应5分钟6个块
    public static Integer five2Thirty(Integer blockValue) {
        if (blockValue == null) {
            return blockValue;
        }else if ((blockValue) % THIRTY_FIVE_COEFFICIENT == 0) {
            return  blockValue  / THIRTY_FIVE_COEFFICIENT;
        }else {
            return  blockValue < 0 ? blockValue / THIRTY_FIVE_COEFFICIENT - 1 : blockValue / THIRTY_FIVE_COEFFICIENT ;
        }
    }

    // [0,1,2,3,4,5,8,9,56]只能转化出一个完整30分钟块0，因为8，9不完整直接舍弃
    public static List<Integer> fiveList2ThirtyList(List<Integer> fiveList) {
        if (CollectionsUtil.isNullOrEmpty(fiveList))
            return Lists.newArrayList();
        //1.分离连续的片段
        //2.将连续的片段转化成30分钟对应的块。
        List<List<Integer>> shardList = Lists.newArrayList();
        int startIndex = 0, endIndex = 0;
        int flag = fiveList.get(0);

        for (Integer item : fiveList) {
            if (item.intValue() == flag) {
                flag++;
            }else {
                shardList.add(fiveList.subList(startIndex, endIndex));
                flag = ++item;
                startIndex = endIndex;
            }
            endIndex++;
        }
        shardList.add(fiveList.subList(startIndex, endIndex));

        //2.将连续的片段转化成30分钟对应的块。
        List<Integer> thirtyBlockList = Lists.newArrayList();
        for (List<Integer> itemList : shardList) {
            //不足6个说明不够拼成一个30分钟块直接舍弃。
            if (itemList.size() < 6)
                continue;
            //将4,5,   6,7,8,9,10,11 ,12 识别成一个30分钟块1
            //能整除6说明是一个时间块的开端。
            int indexFlag = 0;
            for (int index = 0; index < itemList.size();) {
                if (itemList.get(index) % 6 == 0) {
                    indexFlag = index + 5;
                    //后边不足6个块拼凑不成一个30分钟的块
                    if (indexFlag >= itemList.size()) {
                        break;
                    }
                    //是一个连续块
                    if (itemList.get(indexFlag) - itemList.get(index) == 5) {
                        thirtyBlockList.add(TimeBlockUtil.five2Thirty(itemList.get(index)));
                    }
                    index = indexFlag + 1;
                }else {
                    index++;
                }
            }
        }

        return thirtyBlockList;
    }

    /**
     * @description:  [0,1,2,3,4,5,8,9,56]只能转化出一个完整30分钟块0，因为8，9不完整直接舍弃
     * @idea houchunxin
     * @param fiveList 5分钟时间块list
     * @return 30分钟时间块list
     */
    public static List<Integer> fiveList2ThirtyListV2(List<Integer> fiveList) {
        if (CollectionsUtil.isNullOrEmpty(fiveList))
            return Lists.newArrayList();
        //key 30分钟块，value记录有几个5分钟的值匹配了
        Map<Integer, Integer> map = Maps.newHashMap();
        for (Integer item : fiveList) {
            Integer thirtyBlock = five2Thirty(item);
            if (map.get(thirtyBlock) == null) {
                map.put(thirtyBlock, 1);
            }else {
                map.put(thirtyBlock, map.get(thirtyBlock) + 1);
            }
        }
        List<Integer> thirtyBlockSet = Lists.newArrayList();
        Set<Integer> thirtyFiveBlockSet = map.keySet();
        for (Integer item : thirtyFiveBlockSet) {
            if (map.get(item) == THIRTY_FIVE_COEFFICIENT) {
                thirtyBlockSet.add(item);
            }
        }
        Collections.sort(thirtyBlockSet);
        return thirtyBlockSet;
    }

    /**
     * @param thirtyList [0,1]
     * @return [0,1,2,3,4,5,6,7,8,9,10,11]
     */
    public static List<Integer> thirtyList2FiveList(List<Integer> thirtyList) {
        List<Integer> fiveList = Lists.newArrayList();
        for(Integer item : thirtyList) {
            fiveList.addAll(getAllNumbersBetweenTwoBlocks(thirtyStartBlock2FiveStartBlock(item), thirtyEndBlock2FiveEndBlock(item)));
        }
        return fiveList;
    }

    /**
     * 把时间块有课是1没课是0用位的形式转换成五个long
     * @param allBlocksOfOneDay
     * @return
     */
    public static List<Long> timeBlock2Long(List<Integer> allBlocksOfOneDay) {
        Long[] timeLongList = new Long[5];
        Map<Integer, Long> blockMap = Maps.newHashMap();
        List<Long> blockBinaryList = Lists.newArrayListWithCapacity(NEW_MAX_END_BLOCK - MIDNIGHT_BLOCK + 1);
        //1.获取完整时间块0or1状态(1为空闲，0为不空闲)
        for (int i = 0; i < allBlocksOfOneDay.size(); i++) {
            blockMap.put(allBlocksOfOneDay.get(i), 1L);
        }
        for(int i = MIDNIGHT_BLOCK; i <= NEW_MAX_END_BLOCK; i++) {
            if (blockMap.containsKey(i)) {
                blockBinaryList.add(1L);
            } else {
                blockBinaryList.add(0L);
            }
        }
        //2.将二进制数据转换成Long
        int step = 0;
        int timeLongListIndex = -1;
        for (Long item : blockBinaryList) {
            if (step%TIME_BLOCK_BINARY_INTERVAL == 0) {
                step = 0;
                timeLongListIndex++;
                timeLongList[timeLongListIndex] = 0L;
            }
            timeLongList[timeLongListIndex] = (timeLongList[timeLongListIndex] + (item << step));
            step++;
        }
        return Lists.newArrayList(timeLongList);
    }

    //v1把以位存储的时间块转换成5分钟的时间块。
    public static List<Integer> binaryLongList2FiveBlockList(List<Long> binaryLongList) {
        List<Integer> fiveBlockList = Lists.newArrayList();
        StringBuilder result = new StringBuilder();
        for (Long item : binaryLongList) {
            result.append(d2B(item));
        }
        char[] chars = result.toString().toCharArray();
        for (int i = 0; i < (NEW_MAX_END_BLOCK-MIDNIGHT_BLOCK+1); i++){//0-287
            if (Integer.parseInt(String.valueOf(chars[i])) == 1) {
                fiveBlockList.add(i + MIDNIGHT_BLOCK);
            }
        }
        return fiveBlockList;
    }

    //版本不兼容painless filter->script 用string替换
    //8.9.1-course es索引中时间块由30转为5，返回时仍以30返回。
    // v1把以位存储的时间块转换成5分钟的时间块。
    // v2把以字符串存储的日期和时间块转换成5分钟的时间块。
    public static List<Integer> binaryLongList2FiveBlockList(String binaryLongString) {
        List<Long> binaryLongList = Lists.newArrayList();
        String[] binaryLongArray =  binaryLongString.split(",");
        for(String item : binaryLongArray) {
            binaryLongList.add(Long.valueOf(item));
        }
        return binaryLongList2FiveBlockList(binaryLongList);
    }

    //这里是反转的并不是正常的十进制转二进制
    public static StringBuilder d2B(Long para) {
        String longStr = Long.toBinaryString(para);
        StringBuilder result = new StringBuilder(longStr);
        result = result.reverse();
        if (result.length() < TIME_BLOCK_BINARY_INTERVAL) {
            int length = result.length();
            for (int i = 0; i < TIME_BLOCK_BINARY_INTERVAL - length; i++) {
                result = result.append("0");
            }
        }
        return result;
    }


    public static void main(String[] args) {

        //12-12
        List<Long> fiveL = Arrays.asList(1152921504606846975L,1073741823L,1073741823L,1073741823L,281474974613504L);
        List<Long> fiveRestL = Arrays.asList(1152921504606846975L,1073741823L,0L,0L,0L);//1152921504606846975,1073741823
        List<Long> fiveToL = Arrays.asList(0L,0L,0L,1134907106097364992L,7L);
        List<Long> fiveOrL = new ArrayList<>();

        for( int i = 0; i<5; i++ ) {
            fiveOrL.add(fiveL.get(i) ^ fiveRestL.get(i));
        }

        List<Integer> five = binaryLongList2FiveBlockList(fiveL);
        List<Integer> fiveRest = binaryLongList2FiveBlockList(fiveL);
        System.out.println(fiveRest);
        List<Integer> fiveOr = binaryLongList2FiveBlockList(fiveOrL);
        List<Integer> fiveTo = binaryLongList2FiveBlockList(fiveToL);



//        List<Integer> five = binaryLongList2FiveBlockList(Arrays.asList(1152921504606846975L, 1152921504606846975L, 1152921504606846975L, 1152921504606846975L, 281474976710655L));
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        for (Integer i = -96; i < 192; i++) {
            Date d = cal.getTime();
            cal.add(Calendar.MINUTE, 5);
            Date e = cal.getTime();
            System.out.println( i+ " - " + sdf.format(d) + " ~ " + sdf.format(e) + ", " + (five.contains(i) ? "--":"有课") + ", " + (fiveRest.contains(i) ? "有事":"--"));
        }

/*        Date date = new Date("2020/05/26 00:00");
        int startBlock = toFiveStartBlock(date);
        int endBlock = toFiveEndBlock(date);
        System.out.println(startBlock);
        System.out.println(endBlock);


//        Date startTime = TimeUtil.getStartTimeOfOneDay(new Date()); //今天正常时间
//        Date endTime = TimeUtil.dayAfter(startTime, 4 * 7);//28天后 正常时间
//        System.out.println(startTime);
//        System.out.println(endTime);

        System.out.println("**************");
        System.out.println(thirtyStartBlock2FiveStartBlock(1));
        System.out.println(thirtyEndBlock2FiveEndBlock(1));
        System.out.println(fiveStartBlock2ThirtyStartBlock(191));
        System.out.println(fiveEndBlock2ThirtyEndBlock(191));

        List<Integer> integers = fiveList2ThirtyList(Lists.newArrayList(0, 1, 2, 3,4, 5, 8, 9,10, 11,12,13,14,15,16,17,56));
        List<Integer> integers1 = fiveList2ThirtyListV2(Lists.newArrayList(0, 1, 2, 3,4, 5, 8, 9,10, 11,12,13,14,15,16,17,56));
        System.out.println(integers);
        System.out.println(integers1);

        Integer integer6 = five2Thirty(6);

        System.out.println(integer6);*/
    }


}
