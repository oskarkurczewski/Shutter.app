import React, { useRef, useState } from "react";
import styles from "./ReservationBox.module.scss";
import { Reservation } from "types/CalendarTypes";
import { getNthParent } from "util/domUtils";

interface Props {
   reservation: Reservation;
}

export const ReservationBox: React.FC<Props> = ({ reservation }) => {
   const [infoOpen, setInfoOpen] = useState(false);
   const boxElement = useRef(null);

   const [boxStyle, setBoxStyle] = useState<{
      left?: number | string;
      right?: number | string;
   }>({});

   const offset = reservation.from.hour + reservation.from.minute / 60;
   const height = reservation.to.diff(reservation.from, "minutes").minutes / 60;

   const showInfoBox = () => {
      const column = getNthParent(boxElement.current, 3);
      const tableWidth = getNthParent(column, 4).offsetWidth;

      tableWidth - column.offsetLeft < 300
         ? setBoxStyle({ right: "110%" })
         : setBoxStyle({ left: "110%" });
      setInfoOpen(!infoOpen);
   };

   return (
      <div
         className={styles.reservation_wrapper}
         style={{
            top: offset * 48,
         }}
         onBlur={() => setInfoOpen(false)}
         ref={boxElement}
      >
         <div
            className={styles.reservation}
            style={{
               height: height * 48,
            }}
            role="button"
            tabIndex={-1}
            onClick={showInfoBox}
            onKeyDown={showInfoBox}
         />
         {infoOpen && (
            <div className={styles.reservation_info_wrapper} style={boxStyle}>
               <p className="section-title">Rezerwacja</p>
               <div>
                  <p>{reservation.from.toFormat("cccc - dd.LL.yyyy")}</p>
                  <p>
                     {`${reservation.from.toFormat("T")} - ${reservation.to.toFormat(
                        "T"
                     )}`}
                  </p>
               </div>
            </div>
         )}
      </div>
   );
};
