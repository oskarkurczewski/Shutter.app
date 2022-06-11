import { SquareButton } from "components/shared/square-button";
import React, { useState } from "react";
import { FaTrashAlt } from "react-icons/fa";
import { AvailabilityHour } from "types/CalendarTypes";
import { InfoBox } from "../info-box";
import styles from "./AvailabilityBox.module.scss";

interface Props {
   availability: AvailabilityHour;
}

export const AvailabilityBox: React.FC<Props> = ({ availability }) => {
   const [infoBoxOpen, setInfoBoxOpen] = useState(false);

   const offset = availability.from.hour + availability.from.minute / 60;
   const height = availability.to.diff(availability.from, "minutes").minutes / 60;

   return (
      <div
         className={styles.availability_wrapper}
         style={{
            top: offset * 48,
         }}
         onBlur={() => setInfoBoxOpen(false)}
      >
         <div
            className={styles.availability}
            style={{
               height: height * 48,
            }}
            role="button"
            tabIndex={-1}
            onClick={() => setInfoBoxOpen(true)}
            onKeyDown={() => setInfoBoxOpen(true)}
         />
         {infoBoxOpen && (
            <InfoBox className={styles.availability_info_wrapper} isOpen={infoBoxOpen}>
               <p className="section-title">Godziny dostępności:</p>
               <div>
                  <p>{availability.from.toFormat("cccc - dd.LL.yyyy")}</p>
                  <p>
                     {`${availability.from.toFormat("T")} - ${availability.to.toFormat(
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
