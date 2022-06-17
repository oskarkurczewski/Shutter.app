import { Table } from "components/shared";
import { DateTime } from "luxon";
import React, { ReactNode, useState } from "react";
import { useTranslation } from "react-i18next";
import { BiLink } from "react-icons/bi";
import { FaCheck } from "react-icons/fa";
import { Link } from "react-router-dom";
import { useGetPhotographerReportListQuery } from "redux/service/photographerManagementService";
import { photographerReport } from "redux/types/api";
import { tableHeader } from "types";
import styles from "./PhotographerReportTable.module.scss";

interface Props {
   filter: boolean;
}

export const PhotographerReportTable: React.FC<Props> = ({ filter }) => {
   const { t } = useTranslation();

   const [headers, setHeaders] = useState<tableHeader[]>([
      { id: "id", label: "ID", sort: null, sortable: false },
      { id: "photographer", label: "Photographer", sort: null, sortable: false },
      { id: "reportee", label: "Reportee", sort: null, sortable: false },
      { id: "cause", label: "Cause", sort: null, sortable: false },
      { id: "reviewed", label: "Reviewed", sort: null, sortable: false },
      { id: "date", label: "Report date", sort: "asc", sortable: true },
   ]);
   const [pageNo, setPageNo] = useState<number>(1);
   const [recordsPerPage, setRecordsPerPage] = useState<number>(25);

   const photographerReports = useGetPhotographerReportListQuery({
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
   };

   return (
      photographerReports?.isSuccess && (
         <Table
            data={getData()}
            headers={headers}
            setHeaders={setHeaders}
            allRecords={photographerReports?.data?.allRecords}
            allPages={photographerReports?.data?.allPages}
            pageNo={pageNo}
            setPageNo={setPageNo}
            recordsPerPage={recordsPerPage}
            setRecordsPerPage={setRecordsPerPage}
         />
      )
   );
};
