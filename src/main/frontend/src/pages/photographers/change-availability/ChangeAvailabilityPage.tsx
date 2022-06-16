import React from "react";
import styles from "./ChangeAvailability.module.scss";
import { Calendar } from "components/shared/calendar";
import { availabilityHours } from "components/shared/calendar/dumbData";
import { Button, Card } from "components/shared";
import { useTranslation } from "react-i18next";

export const ChangeAvailabilityPage = () => {
   const { t } = useTranslation();

   return (
      <section className={styles.change_availability_page_wrapper}>
         <Calendar className={styles.calendar_wrapper} availability={availabilityHours} />

         <div className={styles.list_wrapper}>
            <p className="category-title">
               {t("change_availability_page.availability_hours")}
            </p>

            <Card className={styles.card_wrapper}>
               <div className={styles.week}>
                  <div className={styles.day_wrapper}>
                     <p className="section-title">Poniedziałek</p>
                     <div className={styles.day}>
                        <div className={styles.row}>
                           <p>08:00 - 10:00</p>
                           <span>2 godziny</span>
                        </div>
                        <div className={styles.row}>
                           <p>08:00 - 10:00</p>
                           <span>2 godziny</span>
                        </div>
                        <div className={styles.row}>
                           <p>08:00 - 10:00</p>
                           <span>2 godziny</span>
                        </div>
                     </div>
                  </div>
                  <div className={styles.day_wrapper}>
                     <p className="section-title">Poniedziałek</p>
                     <div className={styles.day}>
                        <div className={styles.row}>
                           <p>08:00 - 10:00</p>
                           <span>2 godziny</span>
                        </div>
                        <div className={styles.row}>
                           <p>08:00 - 10:00</p>
                           <span>2 godziny</span>
                        </div>
                        <div className={styles.row}>
                           <p>08:00 - 10:00</p>
                           <span>2 godziny</span>
                        </div>
                     </div>
                  </div>
                  <div className={styles.day_wrapper}>
                     <p className="section-title">Poniedziałek</p>
                     <div className={styles.day}>
                        <div className={styles.row}>
                           <p>08:00 - 10:00</p>
                           <span>2 godziny</span>
                        </div>
                        <div className={styles.row}>
                           <p>08:00 - 10:00</p>
                           <span>2 godziny</span>
                        </div>
                        <div className={styles.row}>
                           <p>08:00 - 10:00</p>
                           <span>2 godziny</span>
                        </div>
                     </div>
                  </div>
                  <div className={styles.day_wrapper}>
                     <p className="section-title">Poniedziałek</p>
                     <div className={styles.day}>
                        <div className={styles.row}>
                           <p>08:00 - 10:00</p>
                           <span>2 godziny</span>
                        </div>
                        <div className={styles.row}>
                           <p>08:00 - 10:00</p>
                           <span>2 godziny</span>
                        </div>
                        <div className={styles.row}>
                           <p>08:00 - 10:00</p>
                           <span>2 godziny</span>
                        </div>
                     </div>
                  </div>
                  <div className={styles.day_wrapper}>
                     <p className="section-title">Poniedziałek</p>
                     <div className={styles.day}>
                        <div className={styles.row}>
                           <p>08:00 - 10:00</p>
                           <span>2 godziny</span>
                        </div>
                        <div className={styles.row}>
                           <p>08:00 - 10:00</p>
                           <span>2 godziny</span>
                        </div>
                        <div className={styles.row}>
                           <p>08:00 - 10:00</p>
                           <span>2 godziny</span>
                        </div>
                     </div>
                  </div>
                  <div className={styles.day_wrapper}>
                     <p className="section-title">Poniedziałek</p>
                     <div className={styles.day}>
                        <div className={styles.row}>
                           <p>08:00 - 10:00</p>
                           <span>2 godziny</span>
                        </div>
                        <div className={styles.row}>
                           <p>08:00 - 10:00</p>
                           <span>2 godziny</span>
                        </div>
                        <div className={styles.row}>
                           <p>08:00 - 10:00</p>
                           <span>2 godziny</span>
                        </div>
                     </div>
                  </div>
                  <div className={styles.day_wrapper}>
                     <p className="section-title">Poniedziałek</p>
                     <div className={styles.day}>
                        <div className={styles.row}>
                           <p>08:00 - 10:00</p>
                           <span>2 godziny</span>
                        </div>
                     </div>
                  </div>
               </div>
               <div className={styles.footer}>
                  <Button onClick={() => console.log("wcisnieto")}>Zapisz</Button>
               </div>
            </Card>
         </div>
      </section>
   );
};
