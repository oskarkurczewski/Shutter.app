import React, { ReactNode, useEffect, useState } from "react";
import styles from "./reportsPage.module.scss";
import { useTranslation } from "react-i18next";
import { MdComment } from "react-icons/md";
import { HiCamera, HiUser } from "react-icons/hi";
import { Card, Dropdown, IconDropdown, Table } from "components/shared";
import { tableHeader } from "types";
import * as headers from "./headers";
import { useGetAccountReportListQuery } from "redux/service/usersManagementService";
import { accountReport, photographerReport, reviewReport } from "redux/types/api";
import {
   useGetPhotographerReportListQuery,
   useGetReviewReportListQuery,
} from "redux/service/photographerManagementService";
import { DateTime } from "luxon";
import { FaCheck } from "react-icons/fa";
import { Link } from "react-router-dom";
import { BiLink } from "react-icons/bi";
import { ReviewReportModal } from "components/reports";

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
   const [accountHeaders, setAccountHeaders] = useState<tableHeader[]>(headers.account);
   const [photographerHeaders, setPhotographerHeaders] = useState<tableHeader[]>(
      headers.photographer
   );
   const [reviewHeaders, setReviewHeaders] = useState<tableHeader[]>(headers.review);

   const [pageNo, setPageNo] = useState<number>(1);
   const [recordsPerPage, setRecordsPerPage] = useState<number>(10);

   const [filter, setFilter] = useState<number>(0);

   const [modalIsOpen, setModalIsOpen] = useState<boolean>(false);
   const [currentReview, setCurrentReview] = useState<number>();

   const accountReports = useGetAccountReportListQuery({
      order: accountHeaders[5].sort,
      page: pageNo,
      recordsPerPage: recordsPerPage,
      reviewed: filter !== 0 ? (filter === 1 ? true : false) : undefined,
   });

   const photographerReports = useGetPhotographerReportListQuery({
      order: photographerHeaders[5].sort,
      page: pageNo,
      recordsPerPage: recordsPerPage,
      reviewed: filter !== 0 ? (filter === 1 ? true : false) : undefined,
   });

   const reviewReports = useGetReviewReportListQuery({
      order: reviewHeaders[5].sort,
      page: pageNo,
      recordsPerPage: recordsPerPage,
      reviewed: filter !== 0 ? (filter === 1 ? true : false) : undefined,
   });

   const generateDateNode = (date: Date) => {
      const dateTime = DateTime.fromJSDate(new Date(date));
      return (
         <>
            <p>{dateTime.toFormat("dd.MM.yyyy")}</p>
            <p>{dateTime.toFormat("HH:mm:ss")}</p>
         </>
      );
   };

   const openModal = (reviewNumber: number) => {
      setModalIsOpen(true);
      setCurrentReview(reviewNumber);
   };

   const getData = (): (string | ReactNode)[][] => {
      if (accountReports.data) {
         if (tabState === tab.USERS) {
            return accountReports?.data?.list.map(
               (item: accountReport, index): (string | ReactNode)[] => {
                  return [
                     item.id.toString(),
                     <Link
                        key={index}
                        to={`/users/${item.reportedLogin}/info`}
                        className={styles.table_link}
                     >
                        <span>{item.reportedLogin}</span>
                        <BiLink />
                     </Link>,
                     item.reporteeLogin,
                     t(`reports_page.user.causes.${item.cause}`),
                     item.reviewed && <FaCheck />,
                     generateDateNode(item.createdAt),
                  ];
               }
            );
         }

         if (tabState === tab.PHOTOGRAPHERS) {
            return photographerReports?.data?.list.map(
               (item: photographerReport, index): (string | ReactNode)[] => {
                  return [
                     item.id.toString(),
                     <Link
                        key={index}
                        to={`/profile/${item.photographerLogin}`}
                        className={styles.table_link}
                     >
                        <span>{item.photographerLogin}</span>
                        <BiLink />
                     </Link>,
                     item.accountLogin,
                     t(`reports_page.photographer.causes.${item.cause}`),
                     item.reviewed && <FaCheck />,
                     generateDateNode(item.createdAt),
                  ];
               }
            );
         }

         if (tabState === tab.REVIEWS) {
            return reviewReports?.data?.list.map(
               (item: reviewReport, index): (string | ReactNode)[] => {
                  return [
                     item.id.toString(),
                     <div
                        key={index}
                        role="button"
                        onClick={() => {
                           openModal(item.reviewId);
                        }}
                        onKeyDown={null}
                        tabIndex={index}
                        className={styles.table_link}
                     >
                        <span>{item.reviewId}</span>
                        <BiLink />
                     </div>,
                     item.accountLogin,
                     t(`reports_page.review.causes.${item.cause}`),
                     item.reviewed && <FaCheck />,
                     generateDateNode(item.createdAt),
                  ];
               }
            );
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
               allRecords={
                  tabState === tab.USERS
                     ? accountReports?.data?.allRecords
                     : tabState === tab.PHOTOGRAPHERS
                     ? photographerReports?.data?.allRecords
                     : reviewReports?.data?.allRecords
               }
               allPages={
                  tabState === tab.USERS
                     ? accountReports?.data?.allPages
                     : tabState === tab.PHOTOGRAPHERS
                     ? photographerReports?.data?.allPages
                     : reviewReports?.data?.allPages
               }
               pageNo={pageNo}
               setPageNo={setPageNo}
               recordsPerPage={recordsPerPage}
               setRecordsPerPage={setRecordsPerPage}
            />
         </Card>
         <ReviewReportModal
            isOpen={modalIsOpen}
            onSubmit={() => {
               setModalIsOpen(false);
            }}
            reviewId={1}
         />
      </section>
   );
};
