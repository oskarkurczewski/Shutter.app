import React, { useRef, useState } from "react";
import styles from "./AvailabilityBox.module.scss";
import { useOnClickOutside } from "hooks";
import { AvailabilityHour } from "types/CalendarTypes";
import { InfoBox } from "../info-box";

interface Props {
   availability: AvailabilityHour;
   fullWidth: boolean;
}

export const AvailabilityBox: React.FC<Props> = ({ availability, fullWidth }) => {
   const ref = useRef(null);
   const [infoBoxOpen, setInfoBoxOpen] = useState(false);

   const offset = availability.from.hour + availability.from.minute / 60;
   const height = availability.to.diff(availability.from, "minutes").minutes / 60;

   useOnClickOutside(ref, () => setInfoBoxOpen(false));

   return (
      <div
         ref={ref}
         className={`${styles.availability_wrapper} ${
            fullWidth ? styles.full_width : ""
         }`}
         style={{
            top: offset * 48,
         }}
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
            <InfoBox className={styles.availability_info_wrapper}>
               <p className="section-title">Godziny dostępności:</p>
               <div>
                  <p>{availability.from.toFormat("cccc - dd.LL.yyyy")}</p>
                  <p>
                     {`${availability.from.toFormat("T")} - ${availability.to.toFormat(
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
