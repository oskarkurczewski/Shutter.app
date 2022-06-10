import React from "react";
import { AvailabilityHour } from "types/CalendarTypes";
import styles from "./AvailabilityBox.module.scss";

interface Props {
   availability: AvailabilityHour;
}

export const AvailabilityBox: React.FC<Props> = ({ availability }) => {
   const offset = availability.from.hour + availability.from.minute / 60;
   const height = availability.to.diff(availability.from, "minutes").minutes / 60;

   return (
      <div
         className={styles.avalability}
         style={{
            top: offset * 48,
            height: height * 48,
         }}
      />
   );
};
