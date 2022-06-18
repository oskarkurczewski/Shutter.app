import React, { useEffect, useMemo, useState } from "react";
import styles from "./jobsListPage.module.scss";
import { Calendar } from "components/shared/calendar";
import { Button, Card, TextInput } from "components/shared";
import {
   useGetAvailabityHoursQuery,
   useGetJobListMutation,
} from "redux/service/photographerService";
import { useTranslation } from "react-i18next";
import { DateTime } from "luxon";
import { ReservationBox } from "components/reservations";
import { useAppDispatch, useAppSelector } from "redux/hooks";
import { AvailabilityHour, ErrorResponse, Reservation, Toast } from "types";
import { parseToAvailabilityHour } from "redux/converters";
import { parseError } from "util/errorUtil";
import { push, ToastTypes } from "redux/slices/toastSlice";

export const JobsListPage = () => {
   const { t } = useTranslation();
   const dispatch = useAppDispatch();
   const { username } = useAppSelector((state) => state.auth);

   const [filter, setFilter] = useState("");
   const [dateFrom, setDateFrom] = useState(DateTime.local().startOf("week"));
   const [availability, setAvailability] = useState<AvailabilityHour[]>([]);

   const availabilityQuery = useGetAvailabityHoursQuery(username);
   const [getJobsMutation, getJobsMutationState] = useGetJobListMutation();

   const sendRequest = () => {
      getJobsMutation({
         date: dateFrom.toFormat("yyyy-LL-dd"),
         name: filter ? filter : undefined,
      });
   };

   // Parse availability
   useEffect(() => {
      availabilityQuery.data &&
         setAvailability(parseToAvailabilityHour(availabilityQuery.data));
   }, [availabilityQuery.data]);

   // Parse reservations
   const reservations: Reservation[] = useMemo(
      () =>
         getJobsMutationState.data?.map((reservation) => ({
            id: reservation.id,
            photographer: reservation.photographer.login,
            client: reservation.client.login,
            from: DateTime.fromISO(reservation.from),
            to: DateTime.fromISO(reservation.to),
         })),
      [getJobsMutationState.data]
   );

   // Send request on date change
   useEffect(() => {
      sendRequest();
   }, [dateFrom]);

   // Handle errors
   useEffect(() => {
      let err: string;

      availabilityQuery.isError &&
         (err = parseError(availabilityQuery.error as ErrorResponse));
      getJobsMutationState.isError &&
         (err = parseError(getJobsMutationState.error as ErrorResponse));

      const errorToast: Toast = {
         type: ToastTypes.ERROR,
         text: t(err),
      };
      err && dispatch(push(errorToast));
   }, [availabilityQuery.isError, getJobsMutationState.isError]);

   return (
      <section className={styles.jobs_list_page_wrapper}>
         <Card className={styles.calendar_wrapper}>
            <Calendar
               availability={availability}
               isLoading={getJobsMutationState.isLoading}
               reservations={reservations}
               className={styles.calendar_wrapper}
               onDateChange={(monday) => setDateFrom(monday)}
            />
         </Card>

         <div className={styles.list_wrapper}>
            <div className={styles.filters}>
               <div>
                  <p className="category-title">{t("global.label.filters")}</p>
               </div>
               <Card className={styles.filter_card}>
                  <p className="section-title">
                     {t("photographer_jobs_page.search_client")}
                  </p>
                  <form
                     onSubmit={(e) => {
                        e.preventDefault();
                        sendRequest();
                     }}
                  >
                     <TextInput
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
                     if (getJobsMutationState.data?.length == 0) {
                        return <p>{t("photographer_jobs_page.no_reservations")}</p>;
                     }
                     return getJobsMutationState.data?.map((reservation, index) => (
                        <ReservationBox
                           key={index}
                           reservation={reservation}
                           reservationFor="photogapher"
                           onCancel={() => console.log("cancel")}
                        />
                     ));
                  })()}
               </div>
            </Card>
         </div>
      </section>
   );
};
