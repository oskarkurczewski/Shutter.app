import { Table } from "components/shared";
import { DateTime } from "luxon";
import React, { ReactNode, useState } from "react";
import { useTranslation } from "react-i18next";
import { BiLink } from "react-icons/bi";
import { FaCheck } from "react-icons/fa";
import { Link } from "react-router-dom";
import { useGetReviewReportListQuery } from "redux/service/photographerManagementService";
import { reviewReport } from "redux/types/api";
import { tableHeader } from "types";
import { ReviewReportModal } from "..";
import styles from "./ReviewReportTable.module.scss";

interface Props {
   filter: boolean;
}

export const ReviewReportTable: React.FC<Props> = ({ filter }) => {
   const { t } = useTranslation();

   const [headers, setHeaders] = useState<tableHeader[]>([
      { id: "id", label: "ID", sort: null, sortable: false },
      { id: "reviewId", label: "Review ID", sort: null, sortable: false },
      { id: "reportee", label: "Reportee", sort: null, sortable: false },
      { id: "cause", label: "Cause", sort: null, sortable: false },
      { id: "reviewed", label: "Reviewed", sort: null, sortable: false },
      { id: "date", label: "Report date", sort: "asc", sortable: true },
   ]);
   const [pageNo, setPageNo] = useState<number>(1);
   const [recordsPerPage, setRecordsPerPage] = useState<number>(25);

   const [modalIsOpen, setModalIsOpen] = useState<boolean>(false);
   const [currentReview, setCurrentReview] = useState(1);

   const reviewReports = useGetReviewReportListQuery({
      order: headers[5].sort,
      page: pageNo,
      recordsPerPage: recordsPerPage,
      reviewed: filter,
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

   const openModal = (reviewNumber) => {
      setCurrentReview(reviewNumber);
      setModalIsOpen(true);
   };

   const getData = (): (string | ReactNode)[][] => {
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
   };

   return (
      reviewReports?.isSuccess && (
         <>
            <Table
               data={getData()}
               headers={headers}
               setHeaders={setHeaders}
               allRecords={reviewReports?.data?.allRecords}
               allPages={reviewReports?.data?.allPages}
               pageNo={pageNo}
               setPageNo={setPageNo}
               recordsPerPage={recordsPerPage}
               setRecordsPerPage={setRecordsPerPage}
            />
            <ReviewReportModal
               isOpen={modalIsOpen}
               onSubmit={() => {
                  setModalIsOpen(false);
               }}
               reviewId={currentReview}
            />
         </>
      )
   );
};
