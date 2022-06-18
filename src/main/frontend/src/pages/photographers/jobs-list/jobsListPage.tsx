import React, { useEffect, useMemo, useState } from "react";
import styles from "./jobsListPage.module.scss";
import { Calendar } from "components/shared/calendar";
import { Button, Card, Loader, TextInput } from "components/shared";
import {
   useGetAvailabityHoursQuery,
   useGetJobListMutation,
} from "redux/service/photographerService";
import { useTranslation } from "react-i18next";
import { DateTime } from "luxon";
import { ReservationBox } from "components/reservations";
import { useAppSelector } from "redux/hooks";
import { AvailabilityHour, Reservation } from "types";
import { parseToAvailabilityHour } from "redux/converters";

export const JobsListPage = () => {
   const { t } = useTranslation();
   const { username } = useAppSelector((state) => state.auth);

   const [filter, setFilter] = useState("");
   const [dateFrom, setDateFrom] = useState(DateTime.local().startOf("week"));

   const { data: availability } = useGetAvailabityHoursQuery(username);
   const [getJobsMutation, getJobsMutationState] = useGetJobListMutation();

   const [availabilityData, setAvailabilityData] = useState<AvailabilityHour[]>([]);

   useEffect(() => {
      availability && setAvailabilityData(parseToAvailabilityHour(availability));
   }, [availability]);

   useEffect(() => {
      getJobsMutation({ date: dateFrom.toFormat("yyyy-LL-dd") });
   }, [filter, dateFrom]);

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

   return (
      <section className={styles.jobs_list_page_wrapper}>
         <Calendar
            availability={availabilityData}
            isLoading={getJobsMutationState.isLoading}
            reservations={reservations}
            className={styles.calendar_wrapper}
            onDateChange={(monday) => setDateFrom(monday)}
         />

         <div className={styles.list_wrapper}>
            <div className={styles.filters}>
               <div>
                  <p className="category-title">{t("global.label.filters")}</p>
               </div>
               <Card className={styles.filter_card}>
                  <p className="section-title">
                     {t("photographer_jobs_page.search_client")}
                  </p>
                  <div>
                     <TextInput
                        placeholder={t("global.label.search")}
                        value={filter}
                        onChange={(e) => setFilter(e.target.value)}
                     />
                     <Button
                        onClick={() => {
                           console.log("Szukaj");
                        }}
                     >
                        Szukaj
                     </Button>
                  </div>
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
                           onShow={() => console.log("show")}
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
