import React from "react";
import { Reservation } from "types/CalendarTypes";
import styles from "./ReservationBox.module.scss";

interface Props {
   reservation: Reservation;
}

export const ReservationBox: React.FC<Props> = ({ reservation }) => {
   const offset = reservation.from.hour + reservation.from.minute / 60;
   const height = reservation.to.diff(reservation.from, "minutes").minutes / 60;

   return (
      <div
         className={styles.reservation}
         style={{
            top: offset * 48,
            height: height * 48,
         }}
      />
   );
};
