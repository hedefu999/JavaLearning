package com.changingedu.asyncevent;

import com.qingqing.common.domain.time.TimeParam;
import com.qingqing.common.intf.TimeBlockInterface;
import com.qingqing.common.util.CollectionsUtil;
import com.qingqing.common.util.CompareUtil;
import com.qingqing.common.util.TimeUtil;
import org.apache.commons.lang3.tuple.Pair;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @Author Xuxy
 * @Date 2020/7/7 11:37
 **/
public class TimeBlockUtil {

    public final static String DEFAULT_ZONE_OFFSET = "+8";
    public final static long HALF_HOUR_IN_MILL_SECOND = 30L * 60 * 1000;
    private final static long EIGHT_OCLOCK_MILL_SECOND = LocalDateTime.now().withHour(8).withMinute(0).withSecond(0).toInstant(ZoneOffset.of(DEFAULT_ZONE_OFFSET)).toEpochMilli();
    private final static DateTimeFormatter HOUR_TO_SECOND_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     *  startBlock-->HH:mm:ss
     *  eg: 0-->8:00:00
     * @param startBlock
     * @return
     */
    public static String toStartTimeString(Integer startBlock){
        long nowMilliSecond = EIGHT_OCLOCK_MILL_SECOND + (HALF_HOUR_IN_MILL_SECOND) * startBlock;
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(nowMilliSecond), ZoneOffset.of(DEFAULT_ZONE_OFFSET));
        return HOUR_TO_SECOND_FORMATTER.format(localDateTime);
    }

    /**
     *  endBlock-->HH:mm:ss
     *  eg: 0-->8:30:00
     * @param endBlock
     * @return
     */
    public static String toEndTimeString(Integer endBlock){
        long milliSecond = EIGHT_OCLOCK_MILL_SECOND + (HALF_HOUR_IN_MILL_SECOND) * (endBlock + 1);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(milliSecond), ZoneOffset.of(DEFAULT_ZONE_OFFSET));
        return HOUR_TO_SECOND_FORMATTER.format(localDateTime);
    }

    public static <T extends TimeBlockInterface> List<T> mergeAllBlocks(List<T> blocks) {
        if (CollectionsUtil.isNullOrEmpty(blocks)) {
            return blocks;
        }
        Collections.sort(blocks, TimeBlockInterface.START_BLOCK_ASC_COMPARATOR);

        List<T> ret = new ArrayList<>(blocks.size());
        T last = blocks.get(0);
        ret.add(last);
        for (int i = 1; i < blocks.size(); i++) {
            T tmp = blocks.get(i);
            if (CompareUtil.gt(last.getEndBlock() , tmp.getStartBlock())) {
                last.setEndBlock(Math.max(last.getEndBlock(), tmp.getEndBlock()));
            } else {
                ret.add(tmp);
                last = tmp;
            }
        }

        return ret;
    }

    /**
     * 将排课系统中使用的time_block+当天日期 转换为全球通用的 Date ....
     * @param date
     * @param startBlock
     * @param endBlock
     * @return
     */
    public static Pair<Date, Date> convertTimeBlock2Date(Date date, Integer startBlock, Integer endBlock){
        TimeParam timeParam = new TimeParam(date, startBlock, endBlock);
        Date startTime = com.qingqing.common.util.TimeBlockUtil.getStartTime(timeParam);
        Date endTime = com.qingqing.common.util.TimeBlockUtil.getEndTime(timeParam);
        return Pair.of(startTime, endTime);
    }

    public static void main(String[] args) throws Exception{
        SimpleDateFormat dateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(dateFormat.parse("2020-08-20 10:59:48").getTime());
        // Date date = new Date();date.
        Pair<Date, Date> dateDatePair = convertTimeBlock2Date(new Date(), 0, 2);
        System.out.println(dateFormat.format(dateDatePair.getLeft()));
        System.out.println(dateFormat.format(dateDatePair.getRight()));

        System.out.println(toStartTimeString(0));


        Date endDate = TimeUtil.stringToDate("2020-08-20 10:59:48", TimeUtil.DATE_TO_YEAR_MONTH_DAY);
        System.out.println(com.qingqing.common.util.TimeBlockUtil.toLocalDate(endDate,2));

    }
}
