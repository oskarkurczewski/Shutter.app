import React, { useMemo, useState } from "react";
import styles from "./Calendar.module.scss";
import { formatWeekLabel, getHourRange, getWeekRange } from "util/calendarUtil";
import { Card } from "components/shared/card";
import { CalendarHeader } from "../calendar-header";
import { DateTime } from "luxon";
import { DayColumn } from "../day-column";
import { AvailabilityHour, HourBox, Reservation } from "types/CalendarTypes";
import { useTranslation } from "react-i18next";

interface Props {
   title?: string;
   className?: string;
   availability?: AvailabilityHour[];
   reservations?: Reservation[];
   showWeekNavigation?: boolean;
   isLoading?: boolean;
   onRangeSelection?: (selection?: HourBox[]) => void;
   onDateChange?: (monday?: DateTime) => void;
   onAvailabilityRemove?: (availability: AvailabilityHour) => void;
   onReservationRemove?: (reservation: Reservation) => void;
}

export const Calendar: React.FC<Props> = ({
   title,
   className = "",
   availability,
   reservations,
   showWeekNavigation = true,
   isLoading = false,
   onRangeSelection,
   onDateChange,
   onAvailabilityRemove,
   onReservationRemove,
}) => {
   const [selectedWeek, setSelectedWeek] = useState(DateTime.local().startOf("week"));

   const { i18n } = useTranslation();

   const hours = useMemo(() => getHourRange(), []);
   const week = useMemo(() => {
      return getWeekRange(selectedWeek);
   }, [selectedWeek]);

   const changeWeek = (diff: 1 | -1) => {
      const newWeek = selectedWeek.plus({
         weeks: diff,
      });

      setSelectedWeek(newWeek);
      onDateChange(newWeek);
   };

   return (
      <Card className={`${styles.calendar_wrapper} ${className}`}>
         <CalendarHeader
            showWeekNavigation={showWeekNavigation}
            title={title}
            changeWeek={changeWeek}
            weekLabel={formatWeekLabel(week, i18n.language)}
         />
         <div className={styles.content}>
            <div>
               <div className={styles.hours}>
                  {hours.map((hour, index) => (
                     <div className={styles.hour_box} key={index}>
                        <p className="label-bold">{hour}</p>
                     </div>
                  ))}
               </div>
               <div className={styles.days}>
                  {week.map((dayData, index) => (
                     <DayColumn
                        showWeekNavigation={showWeekNavigation}
                        dayData={dayData}
                        isLoading={isLoading}
                        key={index}
                        availabilityList={availability?.filter(
                           (day) => day.day == index + 1
                        )}
                        reservationsList={reservations?.filter((day) => {
                           return (
                              day.from.startOf("day").toUnixInteger() ==
                              dayData[0].from.toUnixInteger()
                           );
                        })}
                        onRangeSelection={onRangeSelection}
                        onAvailabilityRemove={onAvailabilityRemove}
                        onReservationRemove={onReservationRemove}
                     />
                  ))}
               </div>
            </div>
         </div>
      </Card>
   );
};
