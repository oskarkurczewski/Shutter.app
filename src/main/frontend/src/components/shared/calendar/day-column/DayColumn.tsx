import React, { useMemo } from "react";
import styles from "./DayColumn.module.scss";
import { useSelectable } from "hooks";
import { AvailabilityHour, HourBox, Reservation } from "types/CalendarTypes";
import { AvailabilityBox } from "../availability";
import { ReservationBox } from "../reservation";
import { DateTime } from "luxon";
import { useTranslation } from "react-i18next";

interface Props {
   onRangeSelection?: (selection: HourBox[]) => void;
   dayData: HourBox[];
   availabilityList?: AvailabilityHour[];
   reservationsList?: Reservation[];
}

export const DayColumn: React.FC<Props> = ({
   onRangeSelection,
   dayData,
   availabilityList,
   reservationsList,
}) => {
   const { i18n } = useTranslation();
   const today = useMemo(() => DateTime.local().startOf("day"), [dayData]);
   const displayFullWidth = !(availabilityList && reservationsList);
   const dayStart = dayData[0].from;

   const labelClass =
      dayStart < today ? styles.before : dayStart.equals(today) && styles.today;

   const Selectable = useSelectable({
      objects: dayData,
      onSelect: (e) => onRangeSelection(e),
   });

   return (
      <div className={styles.day_column_wrapper}>
         <div className={`${styles.header} ${labelClass}`}>
            <p className="label-bold">
               {dayStart.setLocale(i18n.language).toFormat("ccc dd")}
            </p>
         </div>
         <div className={styles.content}>
            <div className={styles.grid}>
               {dayData.map((_, index) => {
                  return (
                     <Selectable
                        index={index}
                        disabled={!onRangeSelection}
                        selectedClassName={onRangeSelection ? styles.selected : ""}
                        className={`${styles.half_hour} ${
                           !onRangeSelection ? styles.disabled : ""
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
