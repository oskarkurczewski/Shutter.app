import React from "react";
import styles from "./ChangeAvailability.module.scss";
import { Calendar } from "components/shared/calendar";
import { availabilityHours } from "components/shared/calendar/dumbData";
import { Button, Card } from "components/shared";
import { useTranslation } from "react-i18next";
import { Info } from "luxon";

export const ChangeAvailabilityPage = () => {
   const { t } = useTranslation();

   const weekdayData = Info.weekdays().map((weekday, index) => ({
      weekday,
      data: availabilityHours.filter((availability) => availability.day === index),
   }));

   return (
      <section className={styles.change_availability_page_wrapper}>
         <Calendar
            className={styles.calendar_wrapper}
            availability={availabilityHours}
            onRangeSelection={(e) => console.log(e)}
         />

         <div className={styles.list_wrapper}>
            <p className="category-title">
               {t("change_availability_page.availability_hours")}
            </p>

            <Card className={styles.card_wrapper}>
               <div className={styles.week}>
                  {weekdayData.map((day, index) => (
                     <div className={styles.day_wrapper} key={index}>
                        <p className="section-title">{day.weekday}</p>
                        <div className={styles.day}>
                           {day.data.length > 0 ? (
                              day.data.map((availability, index) => (
                                 <div className={styles.row} key={index}>
                                    <p className="label">
                                       {`${availability.from.toFormat("HH:mm")}
                                        - ${availability.to.toFormat("HH:mm")}`}
                                    </p>
                                    <span>{`${availability.to
                                       .diff(availability.from)
                                       .toFormat("h'h' mm'min'")}`}</span>
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
