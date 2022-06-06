import Card from "components/shared/card";
import Table from "components/shared/table";
import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useGetAccountChangeLogMutation } from "redux/service/api";
import { getOwnAccountChangeLogRequest } from "redux/types/api/accountTypes";
import { tableHeader } from "types/ComponentTypes";
import "./style.scss";

interface Props {
   login: string;
}

const AccountChangeLog: React.FC<Props> = ({ login }) => {
   const { t } = useTranslation();
   const [getAccountChangeLogMutation, getAccountChangeLogMutationState] =
      useGetAccountChangeLogMutation();
   const [params, setParams] = useState<getOwnAccountChangeLogRequest>({
      pageNo: 1,
      recordsPerPage: 25,
      columnName: "id",
      order: "asc",
   });
   const [tableData, setTableData] = useState([]);
   useEffect(() => {
      getAccountChangeLogMutation({ params: params, pathParam: login });
   }, [params]);

   useEffect(() => {
      const list = getAccountChangeLogMutationState.data?.list?.map((item) => [
         item.id,
         new Date(item.changedAt).toUTCString(),
         item.changedBy,
         item.changeType,
      ]);
      list && setTableData(list);
   }, [getAccountChangeLogMutationState.data]);

   const [headers, setHeaders] = useState<tableHeader[]>([
      {
         id: "id",
         label: t("label.tables.account-changelog.id"),
         sortable: true,
         sort: "asc",
      },
      {
         id: "changedAt",
         label: t("label.tables.account-changelog.changed-at"),
         sortable: true,
         sort: null,
      },
      {
         id: "changedBy",
         label: t("label.tables.account-changelog.changed-by"),
         sortable: true,
         sort: null,
      },
      {
         id: "changeType",
         label: t("label.tables.account-changelog.change-type"),
         sortable: true,
         sort: null,
      },
   ]);

   return (
      <Card className="account-change-log-wrapper">
         <Table
            headers={headers}
            data={tableData}
            setHeaders={setHeaders}
            allRecords={getAccountChangeLogMutationState.data?.allRecords}
            allPages={getAccountChangeLogMutationState.data?.allPages}
            pageNo={params.pageNo}
            setPageNo={(number: number) => setParams({ ...params, pageNo: number })}
            recordsPerPage={params.recordsPerPage}
            setRecordsPerPage={(number: number) =>
               setParams({ ...params, recordsPerPage: number })
            }
         />
      </Card>
   );
};

export default AccountChangeLog;
