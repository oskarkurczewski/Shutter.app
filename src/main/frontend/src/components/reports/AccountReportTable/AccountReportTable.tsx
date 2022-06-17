import { Table } from "components/shared";
import { DateTime } from "luxon";
import React, { ReactNode, useState } from "react";
import { useTranslation } from "react-i18next";
import { BiLink } from "react-icons/bi";
import { FaCheck } from "react-icons/fa";
import { Link } from "react-router-dom";
import { useGetAccountReportListQuery } from "redux/service/usersManagementService";
import { accountReport } from "redux/types/api";
import { tableHeader } from "types";
import styles from "./AccountReportTable.module.scss";

interface Props {
   filter: boolean;
}

export const AccountReportTable: React.FC<Props> = ({ filter }) => {
   const { t } = useTranslation();

   const [headers, setHeaders] = useState<tableHeader[]>([
      { id: "id", label: "ID", sort: null, sortable: false },
      { id: "reported", label: "Reported", sort: null, sortable: false },
      { id: "reportee", label: "Reportee", sort: null, sortable: false },
      { id: "cause", label: "Cause", sort: null, sortable: false },
      { id: "reviewed", label: "Reviewed", sort: null, sortable: false },
      { id: "date", label: "Report date", sort: "asc", sortable: true },
   ]);
   const [pageNo, setPageNo] = useState<number>(1);
   const [recordsPerPage, setRecordsPerPage] = useState<number>(25);

   const accountReports = useGetAccountReportListQuery({
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
               item.reviewed && <FaCheck />,
               generateDateNode(item.createdAt),
            ];
         }
      );
   };

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
