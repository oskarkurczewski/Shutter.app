import React, { useRef, useState } from "react";
import styles from "./ReservationBox.module.scss";
import { useOnClickOutside } from "hooks";
import { Reservation } from "types/CalendarTypes";
import { InfoBox } from "../info-box";
import { useTranslation } from "react-i18next";
import { motion, AnimatePresence } from "framer-motion";

interface Props {
   reservation: Reservation;
   fullWidth: boolean;
}

export const ReservationBox: React.FC<Props> = ({ reservation, fullWidth }) => {
   const ref = useRef(null);
   const [infoBoxOpen, setInfoBoxOpen] = useState(false);
   const { t, i18n } = useTranslation();

   const offset = reservation.from.hour + reservation.from.minute / 60;
   const height = reservation.to.diff(reservation.from, "minutes").minutes / 60;

   useOnClickOutside(ref, () => setInfoBoxOpen(false));

   return (
      <AnimatePresence>
         <motion.div
            ref={ref}
            className={`${styles.reservation_wrapper} ${
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
                  <p className="section-title">{t("calendar.reservation")}</p>
                  <div>
                     <p>
                        {reservation.from
                           .setLocale(i18n.language)
                           .toFormat("cccc - dd.LL.yyyy")}
                     </p>
                     <p>
                        {`${reservation.from.toFormat("T")} - ${reservation.to.toFormat(
                           "T"
                        )}`}
                     </p>
                  </div>
                  <div className={styles.actions}></div>
               </InfoBox>
            )}
         </motion.div>
      </AnimatePresence>
   );
};
