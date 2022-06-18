import React, { useEffect, useMemo, useState } from "react";
import styles from "./reservationsListPage.module.scss";
import { Calendar } from "components/shared/calendar";
import { Button, Card, TextInput } from "components/shared";
import { useTranslation } from "react-i18next";
import { DateTime } from "luxon";
import { ReservationBox } from "components/reservations";
import { useAppDispatch } from "redux/hooks";
import { ErrorResponse, Reservation, Toast } from "types";
import { parseError } from "util/errorUtil";
import { push, ToastTypes } from "redux/slices/toastSlice";
import { useGetReservationsListMutation } from "redux/service/usersManagementService";

export const ReservationsListPage = () => {
   const { t } = useTranslation();
   const dispatch = useAppDispatch();

   const [filter, setFilter] = useState("");
   const [dateFrom, setDateFrom] = useState(DateTime.local().startOf("week"));

   const [getReservationsMutation, getReservationsMutationState] =
      useGetReservationsListMutation();

   const sendRequest = () => {
      getReservationsMutation({
         date: dateFrom.toFormat("yyyy-LL-dd"),
         name: filter ? filter : undefined,
      });
   };

   const cancelReservation = (id: number) => {
      console.log("reservation to remove", id);
   };

   const reportReservation = (id: number) => {
      console.log("reservation to remove", id);
   };

   // Parse reservations
   const reservations: Reservation[] = useMemo(
      () =>
         getReservationsMutationState.data?.map((reservation) => ({
            id: reservation.id,
            photographer: reservation.photographer.login,
            client: reservation.client.login,
            from: DateTime.fromISO(reservation.from),
            to: DateTime.fromISO(reservation.to),
         })),
      [getReservationsMutationState.data]
   );

   // Send request on date change
   useEffect(() => {
      sendRequest();
   }, [dateFrom]);

   // Handle errors
   useEffect(() => {
      let err: string;
      getReservationsMutationState.isError &&
         (err = parseError(getReservationsMutationState.error as ErrorResponse));

      const errorToast: Toast = {
         type: ToastTypes.ERROR,
         text: t(err),
      };
      err && dispatch(push(errorToast));
   }, [getReservationsMutationState.isError]);

   return (
      <section className={styles.reservations_list_page_wrapper}>
         <Card className={styles.calendar_wrapper}>
            <Calendar
               title={t("global.label.calendar")}
               isLoading={getReservationsMutationState.isLoading}
               reservations={reservations}
               className={styles.calendar_wrapper}
               onDateChange={(monday) => setDateFrom(monday)}
               onReservationRemove={(reservation) => cancelReservation(reservation.id)}
            />
         </Card>

         <div className={styles.list_wrapper}>
            <div className={styles.filters}>
               <div>
                  <p className="category-title">{t("global.label.filters")}</p>
               </div>
               <Card className={styles.filter_card}>
                  <p className="section-title">
                     {t("user_reservations_page.search_client")}
                  </p>
                  <form
                     onSubmit={(e) => {
                        e.preventDefault();
                        sendRequest();
                     }}
                  >
                     <TextInput
                        className={styles.text_input}
                        placeholder={t("global.label.search")}
                        value={filter}
                        onChange={(e) => setFilter(e.target.value)}
                     />
                     <Button onClick={() => sendRequest()}>Szukaj</Button>
                  </form>
               </Card>
            </div>
            <Card className={`${styles.card_wrapper} ${styles.content}`}>
               <div className={styles.scroll_wrapper}>
                  <p className="section-title">Rezerwacje</p>

                  {(function renderReservations() {
                     if (
                        getReservationsMutationState.data?.length == 0 ||
                        getReservationsMutationState.isError
                     ) {
                        return <p>{t("user_reservations_page.no_reservations")}</p>;
                     }
                     return getReservationsMutationState.data?.map(
                        (reservation, index) => (
                           <ReservationBox
                              key={index}
                              reservation={reservation}
                              reservationFor="client"
                              onCancel={() => cancelReservation(reservation.id)}
                              onReport={() => reportReservation(reservation.id)}
                           />
                        )
                     );
                  })()}
               </div>
            </Card>
         </div>
      </section>
   );
};
