import moment from "moment";
import React, { useMemo } from "react";
import { HourBox } from "types/CalendarTypes";
import styles from "./DayColumn.module.scss";

interface Props {
   dayData: HourBox[];
}

export const DayColumn: React.FC<Props> = ({ dayData }) => {
   const today = useMemo(() => moment().startOf("day"), [dayData]);
   const dayStart = dayData[0].from;
   const labelClass = dayStart.isBefore(today)
      ? styles.before
      : dayStart.isSame(today) && styles.today;

   return (
      <div className={styles.day_column_wrapper}>
         <div className={`${styles.header} ${labelClass}`}>
            <p className="label-bold">{dayStart.format("dd. DD")}</p>
         </div>
         <div className={styles.content}>
            {dayData.map((_, index) => (
               <div className={styles.half_hour} key={index} />
            ))}
         </div>
      </div>
   );
};
