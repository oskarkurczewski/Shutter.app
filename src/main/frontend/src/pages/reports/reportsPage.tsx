import React, { useEffect, useState } from "react";
import styles from "./reportsPage.module.scss";
import { useTranslation } from "react-i18next";
import { MdComment } from "react-icons/md";
import { HiCamera, HiUser } from "react-icons/hi";
import { Card, Table } from "components/shared";
import { tableHeader } from "types";
import * as headers from "./headers";
import { useGetAccountReportListQuery } from "redux/service/usersManagementService";
import { accountReport, photographerReport, reviewReport } from "redux/types/api";
import {
   useGetPhotographerReportListQuery,
   useGetReviewReportListQuery,
} from "redux/service/photographerManagementService";

export const ReportsPage = () => {
   const { t } = useTranslation();
   enum tab {
      USERS = "userReports",
      PHOTOGRAPHERS = "photographerReports",
      REVIEWS = "reviewReports",
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
   const [accountHeaders, setAccountHeaders] = useState<tableHeader[]>(headers.account);
   const [photographerHeaders, setPhotographerHeaders] = useState<tableHeader[]>(
      headers.photographer
   );
   const [reviewHeaders, setReviewHeaders] = useState<tableHeader[]>(headers.review);

   const [pageNo, setPageNo] = useState<number>(1);
   const [recordsPerPage, setRecordsPerPage] = useState<number>(10);

   const accountReports = useGetAccountReportListQuery({
      order: accountHeaders[5].sort,
      page: pageNo,
      recordsPerPage: recordsPerPage,
   });

   const photographerReports = useGetPhotographerReportListQuery({
      order: photographerHeaders[5].sort,
      page: pageNo,
      recordsPerPage: recordsPerPage,
   });

   const reviewReports = useGetReviewReportListQuery({
      order: reviewHeaders[5].sort,
      page: pageNo,
      recordsPerPage: recordsPerPage,
   });

   const getData = (): string[][] => {
      if (accountReports.data) {
         if (tabState === tab.USERS) {
            console.log(accountReports);
            return accountReports?.data?.list.map((item: accountReport): string[] => {
               return [
                  item.id.toString(),
                  item.reportedLogin,
                  item.reporteeLogin,
                  item.cause,
                  item.reviewed.toString(),
                  new Date(item.createdAt).toUTCString(),
               ];
            });
         }

         if (tabState === tab.PHOTOGRAPHERS) {
            return photographerReports?.data?.list.map(
               (item: photographerReport): string[] => {
                  return [
                     item.id.toString(),
                     item.photographerLogin,
                     item.accountLogin,
                     item.cause,
                     item.reviewed.toString(),
                     new Date(item.createdAt).toUTCString(),
                  ];
               }
            );
         }

         if (tabState === tab.REVIEWS) {
            return reviewReports?.data?.list.map((item: reviewReport): string[] => {
               return [
                  item.id.toString(),
                  item.reviewId.toString(),
                  item.accountLogin,
                  item.cause,
                  item.reviewed.toString(),
                  new Date(item.createdAt).toUTCString(),
               ];
            });
         }
      }
      return [];
   };

   useEffect(() => {
      console.log("account", accountReports);
   }, [accountReports]);

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
                        setPageNo(1);
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
         </div>
         <Card className={styles.card}>
            <Table
               data={getData()}
               headers={
                  tabState === tab.USERS
                     ? accountHeaders
                     : tabState === tab.PHOTOGRAPHERS
                     ? photographerHeaders
                     : reviewHeaders
               }
               setHeaders={
                  tabState === tab.USERS
                     ? setAccountHeaders
                     : tabState === tab.PHOTOGRAPHERS
                     ? setPhotographerHeaders
                     : setReviewHeaders
               }
               allRecords={0}
               allPages={4}
               pageNo={pageNo}
               setPageNo={setPageNo}
               recordsPerPage={recordsPerPage}
               setRecordsPerPage={setRecordsPerPage}
            />
         </Card>
      </section>
   );
};
