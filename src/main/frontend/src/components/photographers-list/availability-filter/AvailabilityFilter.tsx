import { Card, Checkbox, Dropdown, IconDropdown } from "components/shared";
import { Info } from "luxon";
import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { PhotographerListRequest } from "redux/types/api";
import { Weekday } from "redux/types/api/dataTypes";
import styles from "./AvailabilityFilter.module.scss";

interface Props {
   onChange: (req: PhotographerListRequest) => void;
   state: PhotographerListRequest;
}

export const AvailabilityFilter: React.FC<Props> = ({ onChange, state }) => {
   const { t, i18n } = useTranslation();

   const [enabled, setEnabled] = useState<boolean>(false);

   const [selectedDay, setSelectedDay] = useState<string>("");
   const [selectedHourFrom, setSelectedHourFrom] = useState<string>("00");
   const [selectedMinuteFrom, setSelectedMinuteFrom] = useState<string>("00");
   const [selectedHourTo, setSelectedHourTo] = useState<string>("00");
   const [selectedMinuteTo, setSelectedMinuteTo] = useState<string>("00");

   useEffect(() => {
      if (enabled) {
         onChange({
            ...state,
            weekDay: selectedDay,
            from: `${selectedHourFrom}:${selectedMinuteFrom}`,
            to: `${selectedHourTo}:${selectedMinuteTo}`,
         });
      } else {
         onChange({
            ...state,
            weekDay: undefined,
            from: undefined,
            to: undefined,
         });
      }
   }, [
      enabled,
      selectedDay,
      selectedHourFrom,
      selectedMinuteFrom,
      selectedHourTo,
      selectedMinuteTo,
   ]);

   return (
      <Card className={styles.availability_filter_wrapper}>
         <div className={styles.title}>
            <Checkbox
               id={"availability"}
               value={enabled}
               onChange={(e) => setEnabled(e.target.checked)}
            />
            <p className="section-title">{t("photographer_list_page.availability")}</p>
         </div>
         <div className={styles.content}>
            <div className={styles.days}>
               {Info.weekdays("narrow", { locale: i18n.language }).map((day, index) => {
                  return (
                     <div key={index} className={styles.day}>
                        <input
                           name="day"
                           type="radio"
                           id={Weekday[index + 1]}
                           onChange={(e) => {
                              if (e.target.checked) {
                                 console.log(e.target.id);
                                 setSelectedDay(e.target.id);
                              }
                           }}
                        />
                        <label htmlFor={Weekday[index + 1]}>{day}</label>
                     </div>
                  );
               })}
            </div>
            <div className={styles.line} />
            <div className={styles.hours_container}>
               <table>
                  <tbody>
                     <tr>
                        <td>
                           <span className={`${styles.text} ${styles.from_to}`}>
                              {t("photographer_list_page.from")}
                           </span>
                        </td>
                        <td>
                           <div className={styles.hours}>
                              <IconDropdown
                                 options={Object.fromEntries(
                                    Array.from(Array(24).keys()).map((key) => [
                                       key.toString(),
                                       key.toString().padStart(2, "0"),
                                    ])
                                 )}
                                 value={selectedHourFrom}
                                 onChange={(value) => {
                                    setSelectedHourFrom(value);
                                 }}
                              />
                              <span className={styles.text}>:</span>
                              <IconDropdown
                                 options={Object.fromEntries(
                                    Array.from(Array(60).keys()).map((key) => [
                                       key.toString(),
                                       key.toString().padStart(2, "0"),
                                    ])
                                 )}
                                 value={selectedMinuteFrom}
                                 onChange={(value) => {
                                    setSelectedMinuteFrom(value);
                                 }}
                              />
                           </div>
                        </td>
                     </tr>
                     <tr>
                        <td>
                           <span className={`${styles.text} ${styles.from_to}`}>
                              {t("photographer_list_page.to")}
                           </span>
                        </td>
                        <td>
                           <div className={styles.hours}>
                              <IconDropdown
                                 options={Object.fromEntries(
                                    Array.from(Array(24).keys()).map((key) => [
                                       key.toString(),
                                       key.toString().padStart(2, "0"),
                                    ])
                                 )}
                                 value={selectedHourTo}
                                 onChange={(value) => {
                                    setSelectedHourTo(value);
                                 }}
                              />
                              <span className={styles.text}>:</span>
                              <IconDropdown
                                 options={Object.fromEntries(
                                    Array.from(Array(60).keys()).map((key) => [
                                       key.toString(),
                                       key.toString().padStart(2, "0"),
                                    ])
                                 )}
                                 value={selectedMinuteTo}
                                 onChange={(value) => {
                                    setSelectedMinuteTo(value);
                                 }}
                              />
                           </div>
                        </td>
                     </tr>
                  </tbody>
               </table>
            </div>
         </div>
      </Card>
   );
};
