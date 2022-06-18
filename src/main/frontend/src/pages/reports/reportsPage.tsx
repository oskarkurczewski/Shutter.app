import React, { ReactNode, useEffect, useState } from "react";
import styles from "./reportsPage.module.scss";
import { useTranslation } from "react-i18next";
import { MdComment } from "react-icons/md";
import { HiCamera, HiUser } from "react-icons/hi";
import { Card, Dropdown } from "components/shared";
import { AccountReportTable } from "components/reports/AccountReportTable";
import { PhotographerReportTable } from "components/reports/PhotographerReportTable";
import { ReviewReportTable } from "components/reports/ReviewReportTable";

export const ReportsPage: React.FC = () => {
   const { t } = useTranslation();
   enum tab {
      USERS = "userReports",
      PHOTOGRAPHERS = "photographerReports",
      REVIEWS = "reviewReports",
   }

   enum filterValues {
      ALL = "all",
      REVIEWED = "reviewed",
      UNREVIEWED = "unreviewed",
   }
   const [tabState, setTabState] = useState<tab>(tab.USERS);
   const sections = [
      {
         icon: <HiUser />,
         id: tab.USERS,
         label: t("reports_page.user.title"),
      },
      {
         icon: <HiCamera />,
         id: tab.PHOTOGRAPHERS,
         label: t("reports_page.photographer.title"),
      },
      {
         icon: <MdComment />,
         id: tab.REVIEWS,
         label: t("reports_page.review.title"),
      },
   ];

   const [filter, setFilter] = useState<number>(0);

   const convertFilter = (): boolean => {
      return filter === 0 ? undefined : filter == 1 ? true : false;
   };

   return (
      <section className={styles.reports_page_wrapper}>
         <div className={styles.nav}>
            <p className="category-title">{t("reports_page.title")}</p>
            <div className={styles.links}>
               {sections.map((section, index) => (
                  <div
                     key={index}
                     className={`${styles.link} ${
                        tabState === section.id && styles.active
                     }`}
                     tabIndex={index}
                     role="button"
                     onClick={() => {
                        setTabState(section.id);
                     }}
                     onKeyDown={() => {
                        return;
                     }}
                  >
                     {section.icon}
                     <p className="label-bold">{section.label}</p>
                  </div>
               ))}
            </div>
            <div className={styles.separator} />
            <Dropdown
               values={Object.values(filterValues).map((value): string =>
                  t(`reports_page.filter.${value}`)
               )}
               selectedValue={filter}
               onChange={(e) => {
                  setFilter(e.target.selectedIndex);
               }}
               name="filter"
               id="reviewedFilter"
               className={styles.filter}
            />
         </div>
         <Card className={styles.card}>
            {tabState === tab.USERS && <AccountReportTable filter={convertFilter()} />}
            {tabState === tab.PHOTOGRAPHERS && (
               <PhotographerReportTable filter={convertFilter()} />
            )}
            {tabState === tab.REVIEWS && <ReviewReportTable filter={convertFilter()} />}
         </Card>
      </section>
   );
};
