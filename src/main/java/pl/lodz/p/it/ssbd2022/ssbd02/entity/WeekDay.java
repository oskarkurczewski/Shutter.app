package pl.lodz.p.it.ssbd2022.ssbd02.entity;

import java.time.LocalDateTime;

/**
 * Możliwe wartości dni tygodnia
 */
public enum WeekDay {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;

    public static WeekDay getWeekDay(LocalDateTime date) {
        return WeekDay.values()[date.getDayOfWeek().getValue() - 1];
    }
}
