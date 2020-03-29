package misc.datetime;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
//temporal 时间的； spatial [ˈspeɪʃl] 空间的
import java.time.temporal.ChronoUnit;

public class DateTimeAPITest {
    @Test
    public void test1(){
        LocalDateTime past = LocalDateTime.of(2000, 12, 7, 16, 25, 0, 0);
        LocalDateTime now = LocalDateTime.now();

        Period period = Period.between(past.toLocalDate(),now.toLocalDate());
        String string = period.getYears() + " " + period.getMonths() + " " +period.getDays();
        System.out.println(string);



        long days2makeUpHumanityCount = ChronoUnit.DAYS.between(past, now);

        String string2 = days2makeUpHumanityCount/365+" "+days2makeUpHumanityCount%365;
        System.out.println(string);
    }

    @Test
    public void test2(){
        /*
         //入参：一周的第一天是周日 + 零头第一周最少要几天算作一周，否则去掉
         WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 5);
         LocalDate today = LocalDate.now();
         TemporalField temporalField = weekFields.weekOfYear();
         int i = today.get(temporalField);
         System.out.println(i);

         */
        LocalDate past = LocalDate.of(1992, 7, 17);

        long l = LocalDate.now().toEpochDay() - past.toEpochDay();
        long until = LocalDate.now().until(past, ChronoUnit.DAYS);
        System.out.println(l);
        System.out.println(until);
    }


}
