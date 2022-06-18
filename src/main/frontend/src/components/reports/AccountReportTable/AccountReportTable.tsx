import { Checkbox, Table } from "components/shared";
import { DateTime } from "luxon";
import React, { ReactNode, useState, useEffect } from "react";
import { useTranslation } from "react-i18next";
import { BiLink } from "react-icons/bi";
import { FaCheck } from "react-icons/fa";
import { Link } from "react-router-dom";
import { useAppDispatch } from "redux/hooks";
import { useResolveReviewReportMutation } from "redux/service/photographerManagementService";
import {
   useGetAccountReportListQuery,
   useResolveAccountReportMutation,
} from "redux/service/usersManagementService";
import { push, ToastTypes } from "redux/slices/toastSlice";
import { accountReport } from "redux/types/api";
import { tableHeader, Toast } from "types";
import styles from "./AccountReportTable.module.scss";

interface Props {
   filter: boolean;
}

export const AccountReportTable: React.FC<Props> = ({ filter }) => {
   const { t } = useTranslation();
   const dispatch = useAppDispatch();

   const [headers, setHeaders] = useState<tableHeader[]>([
      { id: "id", label: t("reports_page.columns.id"), sort: null, sortable: false },
      {
         id: "reported",
         label: t("reports_page.columns.reported"),
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

   const accountReports = useGetAccountReportListQuery({
      order: headers[5].sort,
      page: pageNo,
      recordsPerPage: recordsPerPage,
      reviewed: filter,
   });

   const [resolveMutation, resolveMutationState] = useResolveAccountReportMutation();

   const generateDateNode = (date: Date) => {
      const dateTime = DateTime.fromJSDate(new Date(date));
      return (
         <>
            <p>{dateTime.toFormat("dd.MM.yyyy")}</p>
            <p>{dateTime.toFormat("HH:mm:ss")}</p>
         </>
      );
   };

   const getData = (): (string | ReactNode)[][] => {
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
               <Checkbox
                  id={index.toString()}
                  key={index}
                  value={item.reviewed}
                  onChange={() => {
                     resolveMutation(item.id).then(() => {
                        accountReports.refetch();
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
      if (resolveMutationState.isError) {
         const errorToast: Toast = {
            type: ToastTypes.ERROR,
            text: t("toast.error_update"),
         };
         dispatch(push(errorToast));
      }
   }, [resolveMutationState]);

   return (
      accountReports?.isSuccess && (
         <Table
            data={getData()}
            headers={headers}
            setHeaders={setHeaders}
            allRecords={accountReports?.data?.allRecords}
            allPages={accountReports?.data?.allPages}
            pageNo={pageNo}
            setPageNo={setPageNo}
            recordsPerPage={recordsPerPage}
            setRecordsPerPage={setRecordsPerPage}
         />
      )
   );
};
