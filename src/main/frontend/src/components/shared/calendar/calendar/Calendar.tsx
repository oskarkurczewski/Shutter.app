import React, { useMemo, useState } from "react";
import styles from "./Calendar.module.scss";
import { formatWeekLabel, getHourRange, getWeekRange } from "util/calendarUtil";
import { Card } from "components/shared/card";
import { CalendarHeader } from "../calendar-header";
import moment from "moment";
import { DayColumn } from "../day-column";

export const Calendar = () => {
   const [selectedWeek, setSelectedWeek] = useState(moment().startOf("isoWeek"));

   const hours = useMemo(() => getHourRange(), []);
   const week = useMemo(() => {
      return getWeekRange(selectedWeek);
   }, [selectedWeek]);

   const changeWeek = (diff: 1 | -1) => {
      setSelectedWeek(selectedWeek.clone().add(diff, "weeks"));
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
                  {week?.map((dayData, index) => (
                     <DayColumn dayData={dayData} key={index} />
                  ))}
               </div>
            </div>
         </div>
      </Card>
   );
};
