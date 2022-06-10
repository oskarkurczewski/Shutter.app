import React, { useMemo } from "react";
import styles from "./DayColumn.module.scss";
import { AvailabilityHour, HourBox, Reservation } from "types/CalendarTypes";
import { AvailabilityBox } from "../availability";
import { ReservationBox } from "../reservation";
import { DateTime } from "luxon";

interface Props {
   dayData: HourBox[];
   availabilityList?: AvailabilityHour[];
   reservationsList?: Reservation[];
}

export const DayColumn: React.FC<Props> = ({
   dayData,
   availabilityList,
   reservationsList,
}) => {
   const today = useMemo(() => DateTime.local().startOf("day"), [dayData]);
   const dayStart = dayData[0].from;
   const labelClass =
      dayStart < today ? styles.before : dayStart.equals(today) && styles.today;

   return (
      <div className={styles.day_column_wrapper}>
         <div className={`${styles.header} ${labelClass}`}>
            <p className="label-bold">{dayStart.toFormat("ccc. dd")}</p>
         </div>
         <div className={styles.content}>
            <div className={styles.grid}>
               {dayData.map((_, index) => (
                  <div className={styles.half_hour} key={index} />
               ))}
            </div>
            <div className={styles.avalability_wrapper}>
               {availabilityList.map((availability, index) => (
                  <AvailabilityBox availability={availability} key={index} />
               ))}
            </div>
            <div className={styles.reservations_wrapper}>
               {reservationsList.map((reservation, index) => (
                  <ReservationBox reservation={reservation} key={index} />
               ))}
            </div>
         </div>
      </div>
   );
};
