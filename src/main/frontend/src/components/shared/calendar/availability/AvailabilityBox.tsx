import React, { useRef, useState } from "react";
import styles from "./AvailabilityBox.module.scss";
import { useOnClickOutside } from "hooks";
import { AvailabilityHour } from "types/CalendarTypes";
import { InfoBox } from "../info-box";
import { useTranslation } from "react-i18next";
import { Info } from "luxon";
import { SquareButton } from "components/shared/square-button";
import { FaTrashAlt } from "react-icons/fa";
import { motion, AnimatePresence } from "framer-motion";

interface Props {
   availability: AvailabilityHour;
   fullWidth: boolean;
   onRemove?: (availability: AvailabilityHour) => void;
}

export const AvailabilityBox: React.FC<Props> = ({
   availability,
   fullWidth,
   onRemove,
}) => {
   const ref = useRef(null);
   const [infoBoxOpen, setInfoBoxOpen] = useState(false);
   const { t } = useTranslation();
   const { i18n } = useTranslation();

   const offset = availability.from.hour + availability.from.minute / 60;
   const height = availability.to.diff(availability.from, "minutes").minutes / 60;

   useOnClickOutside(ref, () => setInfoBoxOpen(false));

   return (
      <AnimatePresence>
         <motion.div
            ref={ref}
            className={`${styles.availability_wrapper} ${
               fullWidth ? styles.full_width : ""
            }`}
            style={{
               top: offset * 48,
            }}
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
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
                  <p className="section-title">{t("calendar.availability")}:</p>
                  <div>
                     <p>
                        {
                           Info.weekdays("long", { locale: i18n.language })[
                              availability.day
                           ]
                        }
                     </p>
                     <p>
                        {`${availability.from.toFormat("T")} - ${availability.to.toFormat(
                           "T"
                        )}`}
                     </p>
                  </div>
                  <div className={styles.actions}>
                     {onRemove && (
                        <SquareButton
                           onClick={() => onRemove(availability)}
                           className={styles.remove_icon}
                        >
                           <FaTrashAlt />
                        </SquareButton>
                     )}
                  </div>
               </InfoBox>
            )}
         </motion.div>
      </AnimatePresence>
   );
};
