import React, { useEffect, useState } from "react";
import styles from "./PhotographerProfilePage.module.scss";
import {
   PhotographerDescription,
   PhotographerInfo,
   PhotographerReviewsCardWrapper,
} from "components/photographer-profile";
import {
   useCreateReservationMutation,
   useGetAvailabityHoursQuery,
   useGetPhotographerDetailedInfoQuery,
   useGetReservationsForUserQuery,
} from "redux/service/photographerService";
import { useParams } from "react-router-dom";
import { useTranslation } from "react-i18next";
import { Calendar } from "components/shared/calendar";
import { AvailabilityHour, HourBox, Reservation } from "types/CalendarTypes";
import { DateTime, Info } from "luxon";
import { ErrorResponse, Toast } from "types";
import { push, ToastTypes } from "redux/slices/toastSlice";
import { useAppDispatch, useAppSelector } from "redux/hooks";
import { parseError } from "util/errorUtil";
import { parseToAvailabilityHour, praseToCalendarReservation } from "redux/converters";
import { ExpandableCard } from "components/shared";
import { AddReservationRequest } from "redux/types/api";

export const PhotographerProfilePage = () => {
   const { t } = useTranslation();
   const { login } = useParams();
   const { data, isError } = useGetPhotographerDetailedInfoQuery(login);
   const { username } = useAppSelector((state) => state.auth);

   const dispatch = useAppDispatch();

   const { data: availabilityHoursData } = useGetAvailabityHoursQuery(login);
   const { data: reservationData } = useGetReservationsForUserQuery({
      name: login,
      date: DateTime.now().startOf("week").toFormat("yyyy-MM-dd"),
   });

   const [createReservation, createReservationState] = useCreateReservationMutation();
   const [dateFrom, setDateFrom] = useState(DateTime.local().startOf("week"));
   const [hours, setHours] = useState<AvailabilityHour[]>();
   const [calendarOpen, setCalendarOpen] = useState(false);
   const [reservations, setReservations] = useState<Reservation[]>();
   const [reservation, setReservation] = useState<Reservation>({
      id: null,
      client: username,
      photographer: login,
      from: DateTime.now(),
      to: DateTime.now(),
   });

   useEffect(() => {
      reservationData && setReservations(praseToCalendarReservation(reservationData));
   }, [reservationData]);

   useEffect(() => {
      availabilityHoursData && setHours(parseToAvailabilityHour(availabilityHoursData));
   }, [availabilityHoursData]);

   // request to update reservations
   const sendRequest = () => {
      // useGetReservationsForUserQuery({
      //    name: login,
      //    date: dateFrom.toFormat("yyyy-LL-dd"),
      // });
   };

   // // Send request on date change
   useEffect(() => {
      sendRequest();
   }, [dateFrom]);

   // add reservations function
   const addReservation = (selection: HourBox[]) => {
      if (selection[0].from < DateTime.now()) {
         const errorToast: Toast = {
            type: ToastTypes.ERROR,
            text: t("test"),
         };
         dispatch(push(errorToast));
         return;
      }
      const from = selection[0].from;
      const to = selection[selection.length - 1].to;
      // const isConflict = reservations.find(
      //    (reservation) => from < reservation.to && to > reservation.from
      // );
      setReservation({ ...reservation, from, to });
      // modal
   };

   // Handle request response
   useEffect(() => {
      if (createReservationState.isSuccess) {
         const successToast: Toast = {
            type: ToastTypes.SUCCESS,
            text: t("toast.success_update"),
         };
         dispatch(push(successToast));
      }

      if (createReservationState.isError) {
         const errorToast: Toast = {
            type: ToastTypes.ERROR,
            text: t(
               parseError(
                  createReservationState.error as ErrorResponse,
                  "create_reservation_failed"
               )
            ),
         };
         dispatch(push(errorToast));
      }
   }, [createReservationState]);

   return (
      <section className={styles.photographer_info_page_wrapper}>
         <p className="category-title">{t("photographer_page.title")}</p>
         {isError && <p>EXCEPTION</p>}
         {data && (
            <section>
               <div className={styles.row}>
                  <PhotographerInfo
                     name={data?.name}
                     surname={data?.surname}
                     location="Lodz"
                     stars={data?.score}
                     sessionCount={30}
                     photosCount={546}
                     reviewCount={data?.reviewCount}
                  />

                  <div className={styles.column}>
                     <PhotographerDescription
                        specializationList={data?.specializationList}
                        description={data?.description}
                     />

                     <PhotographerReviewsCardWrapper
                        reviewCount={data.reviewCount}
                        photographerLogin={login}
                     />
                  </div>
               </div>

               <div className={styles.column}>
                  <ExpandableCard
                     className={styles.calendar_wrapper}
                     isOpen={calendarOpen}
                     setIsOpen={setCalendarOpen}
                  >
                     {reservations && (
                        <Calendar
                           title={t("change_availability_page.calendar_title")}
                           showWeekNavigation={true}
                           className={styles.calendar_wrapper}
                           availability={hours}
                           reservations={[...reservations, reservation]}
                           // onDateChange={(monday) => setDateFrom(monday)}
                           onRangeSelection={addReservation}
                        />
                     )}
                  </ExpandableCard>
               </div>
            </section>
         )}
      </section>
   );
};
