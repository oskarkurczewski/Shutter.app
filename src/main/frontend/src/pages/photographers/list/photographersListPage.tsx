import React, { useEffect, useState } from "react";
import styles from "./photographersListPage.module.scss";
import { useTranslation } from "react-i18next";
import { useLocation } from "react-router-dom";
import { useGetActivePhotographersQuery } from "redux/service/photographerManagementService";
import { tableHeader } from "types/ComponentTypes";
import { Card, Table } from "components/shared";

export const PhotographersListPage = () => {
   const { t } = useTranslation();
   const location = useLocation();
   const queryParams = new URLSearchParams(location.search);

   const [headers, setHeaders] = useState<tableHeader[]>([
      {
         id: "login",
         label: t("photographer_list_page.login"),
         sortable: false,
         sort: "asc",
      },
      {
         id: "name",
         label: t("photographer_list_page.name"),
         sortable: false,
         sort: "asc",
      },
      {
         id: "surname",
         label: t("photographer_list_page.surname"),
         sortable: false,
         sort: "asc",
      },
      {
         id: "score",
         label: t("photographer_list_page.score"),
         sortable: false,
         sort: "asc",
      },
      {
         id: "reviewCount",
         label: t("photographer_list_page.review-count"),
         sortable: false,
         sort: "asc",
      },
      {
         id: "specializations",
         label: t("photographer_list_page.specializations"),
         sortable: false,
         sort: "asc",
      },
      {
         id: "longitude",
         label: t("photographer_list_page.longitude"),
         sortable: false,
         sort: "asc",
      },
      {
         id: "latitutde",
         label: t("photographer_list_page.latitude"),
         sortable: false,
         sort: "asc",
      },
   ]);

   const [tableData, setTableData] = useState([]);
   const [params, setParams] = useState({
      pageNo: 1,
      recordsPerPage: 25,
   });

   const { data } = useGetActivePhotographersQuery(params);

   const setQueryParam = (key: string, value: string) => {
      queryParams.set(key, value);
   };

   useEffect(() => {
      setQueryParam("pageNo", String(params.pageNo));
      setQueryParam("recordsPerPage", String(params.recordsPerPage));
   }, [params]);

   useEffect(() => {
      setParams({ ...params });
   }, [headers]);

   useEffect(() => {
      const list = data?.list?.map((item) => [
         item.login,
         item.name,
         item.surname,
         item.score,
         item.reviewCount,
         <>
            {item.specializations?.map((speciatlization) => (
               <p key={`${item.login}-${speciatlization}`}>{speciatlization}</p>
            ))}
         </>,
         item.longitude,
         item.latitude,
      ]);

      list && setTableData(list);
   }, [data]);

   return (
      <div className={styles.account_list_page_wrapper}>
         <Card className={styles.table_card}>
            <Table
               data={tableData}
               headers={headers}
               setHeaders={setHeaders}
               allRecords={data?.allRecords || 0}
               allPages={data?.allPages || 0}
               pageNo={params.pageNo}
               setPageNo={(num) => setParams({ ...params, pageNo: num })}
               recordsPerPage={params.recordsPerPage}
               setRecordsPerPage={(num) =>
                  setParams({ ...params, pageNo: 1, recordsPerPage: num })
               }
            />
         </Card>
      </div>
   );
};
