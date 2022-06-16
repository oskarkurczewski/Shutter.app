import React, { useEffect, useState } from "react";
import styles from "./ChangeAvailability.module.scss";
import { Calendar } from "components/shared/calendar";
import { Button, Card } from "components/shared";
import { useTranslation } from "react-i18next";
import { Info } from "luxon";
import {
   useGetAvailabityHoursQuery,
   useUpdateAvailabilityHoursMutation,
} from "redux/service/photographerService";
import { useAppDispatch, useAppSelector } from "redux/hooks";
import { AvailabilityHour, ErrorResponse, HourBox, Toast } from "types";
import { push, ToastTypes } from "redux/slices/toastSlice";
import { parseError } from "util/errorUtil";

export const ChangeAvailabilityPage = () => {
   const { t } = useTranslation();
   const { username } = useAppSelector((state) => state.auth);
   const dispatch = useAppDispatch();

   const { data } = useGetAvailabityHoursQuery(username);
   const [updateMutation, updateMutationState] = useUpdateAvailabilityHoursMutation();

   const [newHours, setNewHours] = useState<AvailabilityHour[]>();

   useEffect(() => {
      data && setNewHours(data);
   }, [data]);

   // Parse to display
   const weekdayData = Info.weekdays().map((weekday, index) => ({
      weekday,
      data: newHours?.filter((availability) => availability.day === index + 1),
   }));

   // Manage availability
   const addAvailability = (selection: HourBox[]) => {
      const day = selection[0].weekday;
      const from = selection[0].from;
      const to = selection[selection.length - 1].to;

      const isConflict = newHours.find(
         (availability) => from < availability.to && to > availability.from
      );
      !isConflict && setNewHours([...newHours, { day, from, to }]);
   };

   const removeAvailability = (availability: AvailabilityHour): void => {
      const res = newHours.filter(
         (element) => availability.from !== element.from && availability.to !== element.to
      );
      setNewHours(res);
   };

   // Handle request response
   useEffect(() => {
      if (updateMutationState.isSuccess) {
         const successToast: Toast = {
            type: ToastTypes.SUCCESS,
            text: t("toast.success_update"),
         };
         dispatch(push(successToast));
      }

      if (updateMutationState.isError) {
         const err = updateMutationState.error as ErrorResponse;
         const errorToast: Toast = {
            type: ToastTypes.ERROR,
            text: t(`change_availability_page.${parseError(err.data.message)}`),
         };
         dispatch(push(errorToast));
      }
   }, [updateMutationState]);

   return (
      <section className={styles.change_availability_page_wrapper}>
         <Calendar
            title={t("change_availability_page.calendar_title")}
            showWeekNavigation={false}
            className={styles.calendar_wrapper}
            availability={newHours}
            onRangeSelection={addAvailability}
            onAvailabilityRemove={(availability) => removeAvailability(availability)}
         />

         <div className={styles.list_wrapper}>
            <p className="category-title">
               {t("change_availability_page.availability_hours")}
            </p>

            <Card className={styles.card_wrapper}>
               <div className={styles.week}>
                  {weekdayData?.map((day, index) => (
                     <div className={styles.day_wrapper} key={index}>
                        <p className="section-title">{day.weekday}</p>
                        <div className={styles.day}>
                           {day.data?.length > 0 ? (
                              day.data.map((availability, index) => (
                                 <div className={styles.row} key={index}>
                                    <p className="label">
                                       {`${availability.from.toFormat("HH:mm")}
                                        - ${availability.to.toFormat("HH:mm")}`}
                                    </p>
                                    <span>
                                       {`${availability.to
                                          .diff(availability.from)
                                          .toFormat("h'h' mm'min'")}`}
                                    </span>
                                 </div>
                              ))
                           ) : (
                              <p>{t("change_availability_page.no_availability")}</p>
                           )}
                        </div>
                     </div>
                  ))}
               </div>
               <div className={styles.footer}>
                  <Button onClick={() => updateMutation(newHours)}>Zapisz</Button>
               </div>
            </Card>
         </div>
      </section>
   );
};
