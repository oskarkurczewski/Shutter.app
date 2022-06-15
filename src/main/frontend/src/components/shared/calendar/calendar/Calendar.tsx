import React, { useMemo, useState } from "react";
import styles from "./Calendar.module.scss";
import { formatWeekLabel, getHourRange, getWeekRange } from "util/calendarUtil";
import { Card } from "components/shared/card";
import { CalendarHeader } from "../calendar-header";
import { DateTime } from "luxon";
import { DayColumn } from "../day-column";
import { AvailabilityHour, Reservation } from "types/CalendarTypes";

interface Props {
   availability?: AvailabilityHour[];
   reservations?: Reservation[];
   selectable?: boolean;
}

export const Calendar: React.FC<Props> = ({
   availability,
   reservations,
   selectable = true,
}) => {
   const [selectedWeek, setSelectedWeek] = useState(DateTime.local().startOf("week"));

   const hours = useMemo(() => getHourRange(), []);
   const week = useMemo(() => {
      return getWeekRange(selectedWeek);
   }, [selectedWeek]);

   const changeWeek = (diff: 1 | -1) => {
      setSelectedWeek(
         selectedWeek.plus({
            weeks: diff,
         })
      );
   };

   return (
      <Card className={styles.calendar_wrapper}>
         <CalendarHeader
            title="Kalendarz"
            changeWeek={changeWeek}
            weekLabel={formatWeekLabel(week)}
         />
         <div className={styles.content}>
            <div className={styles.scroll_wrapper}>
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
                        dayData={dayData}
                        key={index}
                        availabilityList={availability?.filter(
                           (day) => day.from.weekday == index
                        )}
                        reservationsList={reservations?.filter((day) => {
                           return (
                              day.from.startOf("day").toUnixInteger() ==
                              dayData[0].from.toUnixInteger()
                           );
                        })}
                     />
                  ))}
               </div>
            </div>
         </div>
      </Card>
   );
};
