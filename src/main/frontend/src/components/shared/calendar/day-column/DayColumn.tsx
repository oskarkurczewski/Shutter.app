import React, { useMemo } from "react";
import styles from "./DayColumn.module.scss";
import { useSelectable } from "hooks";
import { AvailabilityHour, HourBox, Reservation } from "types/CalendarTypes";
import { AvailabilityBox } from "../availability";
import { ReservationBox } from "../reservation";
import { DateTime } from "luxon";

interface Props {
   selectable?: boolean;
   dayData: HourBox[];
   availabilityList?: AvailabilityHour[];
   reservationsList?: Reservation[];
}

export const DayColumn: React.FC<Props> = ({
   selectable,
   dayData,
   availabilityList,
   reservationsList,
}) => {
   const today = useMemo(() => DateTime.local().startOf("day"), [dayData]);
   const displayFullWidth = !(availabilityList && reservationsList);
   const dayStart = dayData[0].from;

   const labelClass =
      dayStart < today ? styles.before : dayStart.equals(today) && styles.today;

   const Selectable = useSelectable({
      objects: dayData,
      onSelect: (s) => console.log("selected", s),
   });

   return (
      <div className={styles.day_column_wrapper}>
         <div className={`${styles.header} ${labelClass}`}>
            <p className="label-bold">{dayStart.setLocale("pl").toFormat("ccc dd")}</p>
         </div>
         <div className={styles.content}>
            <div className={styles.grid}>
               {dayData.map((_, index) => {
                  return (
                     <Selectable
                        index={index}
                        disabled={!selectable}
                        selectedClassName={selectable ? styles.selected : ""}
                        className={`${styles.half_hour} ${
                           !selectable ? styles.disabled : ""
                        }`}
                        key={index}
                     />
                  );
               })}
            </div>
            {availabilityList?.map((availability, index) => (
               <AvailabilityBox
                  availability={availability}
                  key={index}
                  fullWidth={displayFullWidth}
               />
            ))}
            {reservationsList?.map((reservation, index) => (
               <ReservationBox
                  reservation={reservation}
                  key={index}
                  fullWidth={displayFullWidth}
               />
            ))}
         </div>
      </div>
   );
};
