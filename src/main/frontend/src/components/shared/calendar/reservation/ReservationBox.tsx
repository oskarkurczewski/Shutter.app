import React, { useRef, useState } from "react";
import styles from "./ReservationBox.module.scss";
import { useOnClickOutside } from "hooks";
import { Reservation } from "types/CalendarTypes";
import { InfoBox } from "../info-box";

interface Props {
   reservation: Reservation;
   fullWidth: boolean;
}

export const ReservationBox: React.FC<Props> = ({ reservation, fullWidth }) => {
   const ref = useRef(null);
   const [infoBoxOpen, setInfoBoxOpen] = useState(false);

   const offset = reservation.from.hour + reservation.from.minute / 60;
   const height = reservation.to.diff(reservation.from, "minutes").minutes / 60;

   useOnClickOutside(ref, () => setInfoBoxOpen(false));

   return (
      <div
         ref={ref}
         className={`${styles.reservation_wrapper} ${fullWidth ? styles.full_width : ""}`}
         style={{
            top: offset * 48,
         }}
      >
         <div
            className={styles.reservation}
            style={{
               height: height * 48,
            }}
            role="button"
            tabIndex={-1}
            onClick={() => setInfoBoxOpen(true)}
            onKeyDown={() => setInfoBoxOpen(true)}
         />
         {infoBoxOpen && (
            <InfoBox className={styles.reservation_info_wrapper}>
               <p className="section-title">Rezerwacja</p>
               <div>
                  <p>{reservation.from.toFormat("cccc - dd.LL.yyyy")}</p>
                  <p>
                     {`${reservation.from.toFormat("T")} - ${reservation.to.toFormat(
                        "T"
                     )}`}
                  </p>
               </div>
               <div className={styles.actions}></div>
            </InfoBox>
         )}
      </div>
   );
};
