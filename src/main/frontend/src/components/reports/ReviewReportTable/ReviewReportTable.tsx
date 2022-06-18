import { Checkbox, Table } from "components/shared";
import { DateTime } from "luxon";
import React, { ReactNode, useState, useEffect } from "react";
import { useTranslation } from "react-i18next";
import { BiLink } from "react-icons/bi";
import { FaCheck } from "react-icons/fa";
import { Link } from "react-router-dom";
import { useAppDispatch } from "redux/hooks";
import {
   useGetReviewReportListQuery,
   useResolveReviewReportMutation,
} from "redux/service/photographerManagementService";
import { push, ToastTypes } from "redux/slices/toastSlice";
import { reviewReport } from "redux/types/api";
import { tableHeader, Toast } from "types";
import { ReviewReportModal } from "..";
import styles from "./ReviewReportTable.module.scss";

interface Props {
   filter: boolean;
}

export const ReviewReportTable: React.FC<Props> = ({ filter }) => {
   const { t } = useTranslation();
   const dispatch = useAppDispatch();

   const [headers, setHeaders] = useState<tableHeader[]>([
      { id: "id", label: t("reports_page.columns.id"), sort: null, sortable: false },
      {
         id: "reviewId",
         label: t("reports_page.columns.review_id"),
         sort: null,
         sortable: false,
      },
      {
         id: "reportee",
         label: t("reports_page.columns.reportee"),
         sort: null,
         sortable: false,
      },
      {
         id: "cause",
         label: t("reports_page.columns.cause"),
         sort: null,
         sortable: false,
      },
      {
         id: "reviewed",
         label: t("reports_page.columns.reviewed"),
         sort: null,
         sortable: false,
      },
      {
         id: "date",
         label: t("reports_page.columns.report_time"),
         sort: "asc",
         sortable: true,
      },
   ]);
   const [pageNo, setPageNo] = useState<number>(1);
   const [recordsPerPage, setRecordsPerPage] = useState<number>(25);

   const [modalIsOpen, setModalIsOpen] = useState<boolean>(false);
   const [currentReview, setCurrentReview] = useState(1);
   const [isReviewed, setIsReviewed] = useState<boolean>(false);

   const reviewReports = useGetReviewReportListQuery({
      order: headers[5].sort,
      page: pageNo,
      recordsPerPage: recordsPerPage,
      reviewed: filter,
   });

   const [resolveMutation, resolveMutationState] = useResolveReviewReportMutation();

   const generateDateNode = (date: Date) => {
      const dateTime = DateTime.fromJSDate(new Date(date));
      return (
         <>
            <p>{dateTime.toFormat("dd.MM.yyyy")}</p>
            <p>{dateTime.toFormat("HH:mm:ss")}</p>
         </>
      );
   };

   const openModal = (reviewNumber: number, isReviewed: boolean) => {
      setCurrentReview(reviewNumber);
      setIsReviewed(isReviewed);
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
                     openModal(item.reviewId, item.reviewed);
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
               <Checkbox
                  id={index.toString()}
                  key={index}
                  value={item.reviewed}
                  onChange={() => {
                     resolveMutation(item.id).then(() => {
                        reviewReports.refetch();
                     });
                  }}
                  disabled={item.reviewed}
               />,
               generateDateNode(item.createdAt),
            ];
         }
      );
   };

   useEffect(() => {
      if (resolveMutationState.isSuccess) {
         const successToast: Toast = {
            type: ToastTypes.SUCCESS,
            text: t("toast.success_update"),
         };
         dispatch(push(successToast));
      }
   }, [resolveMutationState]);

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
