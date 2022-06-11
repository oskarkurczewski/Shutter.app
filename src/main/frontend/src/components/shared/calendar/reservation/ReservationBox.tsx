import React, { useState } from "react";
import styles from "./ReservationBox.module.scss";
import { Reservation } from "types/CalendarTypes";
import { InfoBox } from "../info-box";
import { SquareButton } from "components/shared/square-button";
import { FaTrashAlt } from "react-icons/fa";

interface Props {
   reservation: Reservation;
}

export const ReservationBox: React.FC<Props> = ({ reservation }) => {
   const [infoBoxOpen, setInfoBoxOpen] = useState(false);

   const offset = reservation.from.hour + reservation.from.minute / 60;
   const height = reservation.to.diff(reservation.from, "minutes").minutes / 60;

   return (
      <div
         className={styles.reservation_wrapper}
         style={{
            top: offset * 48,
         }}
         onBlur={() => setInfoBoxOpen(false)}
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
            <InfoBox className={styles.reservation_info_wrapper} isOpen={infoBoxOpen}>
               <p className="section-title">Rezerwacja</p>
               <div>
                  <p>{reservation.from.toFormat("cccc - dd.LL.yyyy")}</p>
                  <p>
                     {`${reservation.from.toFormat("T")} - ${reservation.to.toFormat(
                        "T"
                     )}`}
                  </p>
               </div>
               <div className={styles.actions}>
                  <SquareButton
                     onClick={() => console.log("Usuwanie")}
                     className={styles.remove_icon}
                  >
                     <FaTrashAlt />
                  </SquareButton>
               </div>
            </InfoBox>
         )}
      </div>
   );
};
