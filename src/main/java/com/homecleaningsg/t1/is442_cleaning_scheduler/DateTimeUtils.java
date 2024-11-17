package com.homecleaningsg.t1.is442_cleaning_scheduler;

import java.time.LocalDate;
import java.time.MonthDay;

public class DateTimeUtils {

    public static LocalDate getLastOccurrenceBeforeToday(MonthDay monthDay) {
        LocalDate today = LocalDate.now();
        LocalDate dateThisYear = monthDay.atYear(today.getYear());
        if (!dateThisYear.isAfter(today)) {
            return dateThisYear;
        }
        return monthDay.atYear(today.getYear() - 1);
    }

    public static Long numberOfWorkingDaysBetween(LocalDate startDate, LocalDate endDate) {
        long workingDays = 0;
        LocalDate date = startDate;
        while (!date.isAfter(endDate)) {
            if (isWorkingDay(date)) {
                workingDays++;
            }
            date = date.plusDays(1);
        }
        return workingDays;
    }

    public static boolean isWorkingDay(LocalDate date) {
        return true;
    }

}
