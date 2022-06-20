import React, { useEffect, useState } from "react";
import styles from "./photographersListAdminPage.module.scss";
import { useTranslation } from "react-i18next";
import { useLocation, useNavigate } from "react-router-dom";
import { useGetActivePhotographersMutation } from "redux/service/photographerManagementService";
import { tableHeader } from "types/ComponentTypes";
import { Card, Table } from "components/shared";

export const PhotographersListAdminPage = () => {
   const { t } = useTranslation();

   const [headers, setHeaders] = useState<tableHeader[]>([
      {
         id: "login",
         label: t("photographer_list_admin_page.login"),
         sortable: false,
         sort: "asc",
      },
      {
         id: "name",
         label: t("photographer_list_admin_page.name"),
         sortable: false,
         sort: "asc",
      },
      {
         id: "surname",
         label: t("photographer_list_admin_page.surname"),
         sortable: false,
         sort: "asc",
      },
      {
         id: "score",
         label: t("photographer_list_admin_page.score"),
         sortable: false,
         sort: "asc",
      },
      {
         id: "reviewCount",
         label: t("photographer_list_admin_page.review-count"),
         sortable: false,
         sort: "asc",
      },
      {
         id: "specializations",
         label: t("photographer_list_admin_page.specializations"),
         sortable: false,
         sort: "asc",
      },
      {
         id: "longitude",
         label: t("photographer_list_admin_page.longitude"),
         sortable: false,
         sort: "asc",
      },
      {
         id: "latitutde",
         label: t("photographer_list_admin_page.latitude"),
         sortable: false,
         sort: "asc",
      },
   ]);

   const [tableData, setTableData] = useState([]);

   const [params, setParams] = useState({
      pageNo: 1,
      recordsPerPage: 25,
   });

   const location = useLocation();
   const navigate = useNavigate();
   const queryParams = new URLSearchParams(location.search);

   const [fetchList, data] = useGetActivePhotographersMutation();
   const allRecords = data.data?.allRecords || 0;
   const allPages = data.data?.allPages || 0;

   const setQueryParam = (key: string, value: string) => {
      queryParams.set(key, value);
      navigate({
         pathname: location.pathname,
         search: queryParams.toString(),
      });
   };

   useEffect(() => {
      setQueryParam("pageNo", String(params.pageNo));
      setQueryParam("recordsPerPage", String(params.recordsPerPage));
   });

   useEffect(() => {
      setParams({ ...params });
   }, [headers]);

   useEffect(() => {
      fetchList(params);
   }, [params]);

   useEffect(() => {
      const list = data.data?.list?.map((item) => [
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
               allRecords={allRecords}
               allPages={allPages}
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
