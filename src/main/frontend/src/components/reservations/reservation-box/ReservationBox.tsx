import React from "react";
import styles from "./ReservationBox.module.scss";
import { ReservationResponse } from "redux/types/api";
import { Button } from "components/shared";
import { DateTime } from "luxon";
import { useTranslation } from "react-i18next";
import { motion, AnimatePresence } from "framer-motion";

type Props = {
   reservation: ReservationResponse;
   onReport?: () => void;
   onCancel?: () => void;
   reservationFor: "photogapher" | "client";
};

export const ReservationBox: React.FC<Props> = ({
   reservation,
   reservationFor,
   onReport,
   onCancel,
}) => {
   const { t, i18n } = useTranslation();

   const timeFrom = DateTime.fromISO(reservation.from);
   const timeTo = DateTime.fromISO(reservation.from);

   return (
      <AnimatePresence>
         <motion.div
            className={styles.reservation_box_wrapper}
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
         >
            <div className={styles.content}>
               <div className={styles.avatar_wrapper}>
                  <img
                     src="https://atlas.it.p.lodz.pl/bitbucket/users/229874/avatar.png?s=64&v=1655392225748"
                     alt="avatar"
                  />
               </div>
               <div>
                  {reservationFor == "client" ? (
                     <p className="label-bold">{`${reservation.photographer.name} ${reservation.photographer.surname}`}</p>
                  ) : (
                     <p className="label-bold">{`${reservation.client.name} ${reservation.client.surname}`}</p>
                  )}
                  <p>{`${timeFrom
                     .setLocale(i18n.language)
                     .toFormat("ccc dd-LL-yyyy")} ${timeFrom.toFormat(
                     "HH:mm"
                  )}-${timeTo.toFormat("HH:mm")}`}</p>
               </div>
            </div>
            <div className={styles.footer}>
               <Button className={styles.button} href={`#reservation-${reservation.id}`}>
                  {t("global.label.show")}
               </Button>
               {onReport && DateTime.now() > timeTo && (
                  <Button className={styles.button} onClick={onReport}>
                     {t("global.label.report")}
                  </Button>
               )}
               {onCancel && DateTime.now() < timeTo && (
                  <Button className={styles.button} onClick={onCancel}>
                     {t("global.label.reject")}
                  </Button>
               )}
            </div>
         </motion.div>
      </AnimatePresence>
   );
};
