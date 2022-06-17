import React, { useEffect, useState } from "react";
import styles from "./ChangeAvailability.module.scss";
import { Calendar } from "components/shared/calendar";
import { Button, Card } from "components/shared";
import { useTranslation } from "react-i18next";
import { Info } from "luxon";
import { useGetAvailabityHoursQuery } from "redux/service/photographerService";
import { useAppSelector } from "redux/hooks";
import { AvailabilityHour } from "types/CalendarTypes";

export const ChangeAvailabilityPage = () => {
   const { t } = useTranslation();
   const { username } = useAppSelector((state) => state.auth);

   const { data } = useGetAvailabityHoursQuery(username);
   const [newHours, setNewHours] = useState<AvailabilityHour[]>([]);

   useEffect(() => {
      data && setNewHours(data);
   }, [data]);

   const weekdayData = Info.weekdays().map((weekday, index) => ({
      weekday,
      data: newHours?.filter((availability) => availability.day === index),
   }));

   return (
      <section className={styles.change_availability_page_wrapper}>
         <Calendar
            title={t("change_availability_page.calendar_title")}
            showWeekNavigation={false}
            className={styles.calendar_wrapper}
            availability={newHours}
            onRangeSelection={(selection) => {
               setNewHours([
                  ...newHours,
                  {
                     day: selection[0].weekday,
                     from: selection[0].from,
                     to: selection[selection.length - 1].to,
                  },
               ]);
            }}
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
                  <Button onClick={() => console.log("wcisnieto")}>Zapisz</Button>
               </div>
            </Card>
         </div>
      </section>
   );
};
