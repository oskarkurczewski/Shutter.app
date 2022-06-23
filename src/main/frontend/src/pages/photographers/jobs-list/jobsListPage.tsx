import React, { useEffect, useMemo, useRef, useState } from "react";
import styles from "./jobsListPage.module.scss";
import { Calendar } from "components/shared/calendar";
import { Button, Card, Modal, TextInput } from "components/shared";
import {
   useDiscardJobMutation,
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
import { ReportModal } from "components/job-list-page/report-modal";

export const JobsListPage = () => {
   const { t } = useTranslation();
   const dispatch = useAppDispatch();
   const { username } = useAppSelector((state) => state.auth);

   const [filter, setFilter] = useState("");
   const [dateFrom, setDateFrom] = useState(DateTime.local().startOf("week"));
   const [availability, setAvailability] = useState<AvailabilityHour[]>([]);

   const availabilityQuery = useGetAvailabityHoursQuery(username);
   const [getJobsMutation, getJobsMutationState] = useGetJobListMutation();
   const [discardJobMutation, discardJobMutationState] = useDiscardJobMutation();
   const [modalOpen, setModalOpen] = useState<boolean>(false);
   const [reservationId, setReservationId] = useState(null);
   const [reportModalOpen, setReportModalOpen] = useState(false);
   const clinetLogin = useRef("");

   const sendRequest = () => {
      getJobsMutation({
         date: dateFrom.toFormat("yyyy-LL-dd"),
         name: filter ? filter : undefined,
      });
   };

   const cancelReservation = (id: number) => {
      setReservationId(id);
      setModalOpen(true);
   };

   const reportReservation = (login: string) => {
      clinetLogin.current = login;
      setReportModalOpen(true);
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

   // discard job function
   const discardJob = () => {
      discardJobMutation(reservationId);
      setModalOpen(false);
   };

   // Send request on date change
   useEffect(() => {
      sendRequest();
   }, [dateFrom]);

   // Handle success
   useEffect(() => {
      if (discardJobMutationState.isSuccess) {
         sendRequest();
         const successToast: Toast = {
            type: ToastTypes.SUCCESS,
            text: t("photographer_jobs_page.discard_reservation.success"),
         };
         dispatch(push(successToast));
      }
   }, [discardJobMutationState.isSuccess]);

   // Handle errors
   useEffect(() => {
      let err: string[];

      availabilityQuery.isError &&
         (err = parseError(availabilityQuery.error as ErrorResponse));
      getJobsMutationState.isError &&
         (err = parseError(getJobsMutationState.error as ErrorResponse));
      discardJobMutationState.isError &&
         (err = parseError(discardJobMutationState.error as ErrorResponse));

      const errorToast: Toast = {
         type: ToastTypes.ERROR,
         text: t(err),
      };
      err && dispatch(push(errorToast));
   }, [
      availabilityQuery.isError,
      getJobsMutationState.isError,
      discardJobMutationState.isError,
   ]);

   return (
      <section className={styles.jobs_list_page_wrapper}>
         <Modal
            title={t("photographer_jobs_page.discard_reservation.modal.title")}
            type="confirm"
            isOpen={modalOpen}
            onCancel={() => setModalOpen(false)}
            onSubmit={() => discardJob()}
            submitText={t("photographer_jobs_page.discard_reservation.modal.title")}
         >
            <p>{t("photographer_jobs_page.discard_reservation.modal.description")}</p>
         </Modal>
         <ReportModal
            isOpen={reportModalOpen}
            onCancel={() => setReportModalOpen(false)}
            onSubmit={() => setReportModalOpen(false)}
            login={clinetLogin.current}
         />
         <Card className={styles.calendar_wrapper}>
            <Calendar
               title={t("global.label.calendar")}
               availability={availability}
               isLoading={getJobsMutationState.isLoading}
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
                     {t("photographer_jobs_page.search_client")}
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
                     <Button onClick={() => sendRequest()}>
                        {t("global.label.search")}
                     </Button>
                  </form>
               </Card>
            </div>
            <Card className={`${styles.card_wrapper} ${styles.content}`}>
               <div className={styles.scroll_wrapper}>
                  <p className="section-title">{t("global.navigation.jobs")}</p>

                  {(function renderReservations() {
                     if (
                        getJobsMutationState.data?.length == 0 ||
                        getJobsMutationState.isError
                     ) {
                        return <p>{t("photographer_jobs_page.no_reservations")}</p>;
                     }
                     return getJobsMutationState.data?.map((reservation, index) => (
                        <ReservationBox
                           key={index}
                           reservation={reservation}
                           reservationFor="photogapher"
                           onCancel={() => cancelReservation(reservation.id)}
                           onReport={() => reportReservation(reservation.client.login)}
                        />
                     ));
                  })()}
               </div>
            </Card>
         </div>
      </section>
   );
};
