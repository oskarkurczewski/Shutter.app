import React, { useEffect, useMemo, useState } from "react";
import styles from "./PhotographerCalendar.module.scss";
import {
   PhotographerDescription,
   PhotographerInfo,
   PhotographerReviewsCardWrapper,
} from "components/photographer-profile";
import {
   useCreateReservationMutation,
   useGetAvailabityHoursQuery,
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
import { parseToAvailabilityHour } from "redux/converters";
import { ExpandableCard } from "components/shared";
import { PhotographerAddReservationModal } from "../photographer-add-reservation-modal";
import { UserData } from "redux/types/api";

interface Props {
   photographer: UserData;
}

export const PhotographerCalendar: React.FC<Props> = ({ photographer }) => {
   const { t } = useTranslation();
   const { login } = useParams();
   const { username } = useAppSelector((state) => state.auth);

   const dispatch = useAppDispatch();

   const [dateFrom, setDateFrom] = useState(DateTime.local().startOf("week"));

   const availabilityQuery = useGetAvailabityHoursQuery(login);
   const getReservationsQuery = useGetReservationsForUserQuery({
      date: dateFrom.toFormat("yyyy-LL-dd"),
      name: login,
   });
   // const [createReservation, createReservationState] = useCreateReservationMutation();
   const [modalOpen, setModalOpen] = useState<boolean>(false);
   const [availability, setAvailability] = useState<AvailabilityHour[]>();
   const [calendarOpen, setCalendarOpen] = useState(false);
   const [reservation, setReservation] = useState<Reservation>({
      id: null,
      client: username,
      photographer: login,
      from: DateTime.now(),
      to: DateTime.now(),
   });

   // parse reservations
   const reservations: Reservation[] = useMemo(
      () =>
         getReservationsQuery.data?.map((res) => ({
            id: res.id,
            photographer: login,
            client: "",
            from: DateTime.fromISO(res.from),
            to: DateTime.fromISO(res.to),
         })),
      [getReservationsQuery.data]
   );

   useEffect(() => {
      availabilityQuery.data &&
         setAvailability(parseToAvailabilityHour(availabilityQuery.data));
   }, [availabilityQuery.data]);

   // send request on date change
   useEffect(() => {
      getReservationsQuery.refetch();
   }, [dateFrom]);

   // add reservations function
   const addReservation = (selection: HourBox[]) => {
      if (username === "") {
         const errorToast: Toast = {
            type: ToastTypes.ERROR,
            text: t("photographer_page.calendar.user_not_logged_in"),
         };
         dispatch(push(errorToast));
         return;
      }
      if (selection[0].from < DateTime.now()) {
         const errorToast: Toast = {
            type: ToastTypes.ERROR,
            text: t("photographer_page.calendar.past_date"),
         };
         dispatch(push(errorToast));
         return;
      }

      const from = selection[0].from;
      const to = selection[selection.length - 1].to;

      const isConflict = reservations.find(
         (reservation) => from < reservation.to && to > reservation.from
      );

      const isAvailabilityValid = availability.find(
         (availability) =>
            availability.from.set({
               weekNumber: from.weekNumber,
               weekYear: from.weekYear,
            }) <= from &&
            to <=
               availability.to.set({
                  weekNumber: to.weekNumber,
                  weekYear: to.weekYear,
               })
      );
      if (!isConflict) {
         if (isAvailabilityValid) {
            setReservation({ ...reservation, from, to });
            setModalOpen(true);
         } else {
            const errorToast: Toast = {
               type: ToastTypes.ERROR,
               text: t("photographer_page.calendar.no_avalibility"),
            };
            dispatch(push(errorToast));
         }
      } else {
         const errorToast: Toast = {
            type: ToastTypes.ERROR,
            text: t("photographer_page.calendar.conflict_reservation_time"),
         };
         dispatch(push(errorToast));
      }
   };

   // Handle errors
   useEffect(() => {
      let err: string;

      availabilityQuery.isError &&
         (err = parseError(availabilityQuery.error as ErrorResponse));
      getReservationsQuery.isError &&
         (err = parseError(getReservationsQuery.error as ErrorResponse));

      const errorToast: Toast = {
         type: ToastTypes.ERROR,
         text: t(err),
      };
      err && dispatch(push(errorToast));
   }, [availabilityQuery.isError, getReservationsQuery.isError]);

   return (
      <ExpandableCard isOpen={calendarOpen} setIsOpen={setCalendarOpen}>
         {reservations && (
            <>
               <Calendar
                  title={t("photographer_page.calendar_title")}
                  showWeekNavigation={true}
                  isLoading={getReservationsQuery.isLoading}
                  availability={availability}
                  reservations={[...reservations, reservation]}
                  onDateChange={(monday) => setDateFrom(monday)}
                  onRangeSelection={addReservation}
               />
               <PhotographerAddReservationModal
                  reservation={reservation}
                  setReservation={setReservation}
                  photographer={photographer}
                  isOpen={modalOpen}
                  onCancel={() => setModalOpen(false)}
                  onSubmit={() => setModalOpen(false)}
               />
            </>
         )}
      </ExpandableCard>
   );
};
