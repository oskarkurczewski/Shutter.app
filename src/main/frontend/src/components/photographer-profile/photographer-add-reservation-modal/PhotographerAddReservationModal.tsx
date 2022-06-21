import { Avatar, IconText, Modal, Notification, TimeInput } from "components/shared";
import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useAppDispatch, useAppSelector } from "redux/hooks";
import { useCreateReservationMutation } from "redux/service/photographerService";
import { push, ToastTypes } from "redux/slices/toastSlice";
import { UserData } from "redux/types/api";
import { ErrorResponse, Reservation, Toast } from "types";
import { parseError } from "util/errorUtil";
import styles from "./PhotographerAddReservationModal.module.scss";
import { RiCalendarEventFill } from "react-icons/ri";
import { DateTime } from "luxon";
import i18n from "i18n";

interface Props {
   reservation: Reservation;
   setReservation: (reservation: Reservation) => void;
   photographer: UserData;
   isOpen: boolean;
   onSubmit: () => void;
   onCancel: () => void;
}

export const PhotographerAddReservationModal: React.FC<Props> = ({
   reservation,
   setReservation,
   photographer,
   isOpen,
   onSubmit,
   onCancel,
}) => {
   const { t } = useTranslation();
   const dispatch = useAppDispatch();
   const [createReservationMutation, createReservationMutationState] =
      useCreateReservationMutation();

   const { name, surname, email } = useAppSelector((state) => state.auth);

   const [notification, setNotification] = useState<Notification>(null);

   const toHour = (hour: string) => {
      return +hour.slice(0, 2);
   };

   useEffect(() => {
      if (createReservationMutationState.isSuccess) {
         const successToast: Toast = {
            type: ToastTypes.SUCCESS,
            text: t("toast.success_reservation_create"),
         };
         dispatch(push(successToast));
      }

      if (createReservationMutationState.isError) {
         const errorToast: Toast = {
            type: ToastTypes.ERROR,
            text: t(parseError(createReservationMutationState.error as ErrorResponse)),
         };
         dispatch(push(errorToast));

         setReservation({
            ...reservation,
            from: DateTime.now(),
            to: DateTime.now(),
         });
      }
   }, [createReservationMutationState]);

   const createReservation = () => {
      createReservationMutation({
         photographerLogin: photographer.login,
         from: reservation.from,
         to: reservation.to,
      });
      onSubmit();
   };

   return (
      <Modal
         type="confirm"
         isOpen={isOpen}
         onSubmit={createReservation}
         onCancel={onCancel}
         title={t("photographer_page.modal.create_reservation")}
         notification={notification}
      >
         <div className={styles.modal}>
            <div className={styles.content}>
               <div className={styles.row}>
                  <p className="label">{t("photographer_page.modal.user")}</p>
                  <div className={styles.box}>
                     <Avatar className={styles.avatar} email={email} />
                     <p className="label-bold">{`${name} ${surname}`}</p>
                  </div>
               </div>

               <div className={styles.row}>
                  <p className="label">{t("photographer_page.modal.photographer")}</p>
                  <div className={styles.box}>
                     <Avatar className={styles.avatar} email={photographer.email} />
                     <p className="label-bold">{`${photographer.name} ${photographer.surname}`}</p>
                  </div>
               </div>
            </div>
            <div className={styles.content}>
               <div className={styles.second_row}>
                  <IconText
                     text={reservation.from
                        .setLocale(i18n.language)
                        .toFormat("ccc dd-LL-yyyy")}
                     icon={RiCalendarEventFill}
                  />
                  <TimeInput
                     value={reservation.from.toFormat("HH:mm")}
                     onChange={(value) =>
                        setReservation({
                           ...reservation,
                           from: reservation.from.set({ hour: toHour(value) }),
                        })
                     }
                  />

                  <IconText
                     text={reservation.to
                        .setLocale(i18n.language)
                        .toFormat("ccc dd-LL-yyyy")}
                     icon={RiCalendarEventFill}
                  />
                  <TimeInput
                     value={reservation.to.toFormat("HH:mm")}
                     onChange={(value) =>
                        setReservation({
                           ...reservation,
                           to: reservation.to.set({ hour: toHour(value) }),
                        })
                     }
                  />
               </div>
            </div>
         </div>
      </Modal>
   );
};
