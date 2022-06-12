import React, { useEffect, useState } from "react";
import styles from "./userAccountListPage.module.scss";
import { Card, Modal, Table } from "components/shared";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { FaAngleRight, FaCheck, FaEdit } from "react-icons/fa";
import {
   useGetAccountListPreferencesMutation,
   useGetAdvancedUserListMutation,
   useGetBasicUserListMutation,
} from "redux/service/usersManagementService";
import { tableHeader } from "types/ComponentTypes";
import { useTranslation } from "react-i18next";
import { ListUsersFilter } from "components/account-management/list-account";
import EditAccountModal from "components/account-management/list-account/edit-modal/EditAccountModal";

export const UserAccountListPage = () => {
   const { t } = useTranslation();

   const [headers, setHeaders] = useState<tableHeader[]>([
      {
         id: "id",
         label: t("user_account_list_page.id"),
         sortable: true,
         sort: "asc",
      },
      {
         id: "login",
         label: t("user_account_list_page.login"),
         sortable: true,
         sort: null,
      },
      {
         id: "email",
         label: t("user_account_list_page.email"),
         sortable: true,
         sort: null,
      },
      {
         id: "name",
         label: t("user_account_list_page.first_name"),
         sortable: true,
         sort: null,
      },
      {
         id: "surname",
         label: t("user_account_list_page.second_name"),
         sortable: true,
         sort: null,
      },
      {
         id: "roles",
         label: t("user_account_list_page.access_level"),
         sortable: false,
         sort: null,
      },
      {
         id: "registered",
         label: t("user_account_list_page.registered"),
         sortable: false,
         sort: null,
      },
      {
         id: "active",
         label: t("user_account_list_page.active"),
         sortable: false,
         sort: null,
      },
      {
         id: "actions",
         label: t("user_account_list_page.actions"),
         sortable: false,
         sort: null,
      },
   ]);
   const [editModalIsOpen, setEditModalIsOpen] = useState<boolean>(false);
   const [login, setLogin] = useState<string>("");
   const closeEditModal = () => setEditModalIsOpen(false);
   const openEditModal = (login: string) => {
      setEditModalIsOpen(true);
      setLogin(login);
   };
   const [tableData, setTableData] = useState([]);

   const [query, setQuery] = useState<string>("");
   const [params, setParams] = useState({
      pageNo: 1,
      recordsPerPage: 25,
      columnName: "id",
      order: "asc",
      q: query,
   });

   const location = useLocation();
   const navigate = useNavigate();
   const queryParams = new URLSearchParams(location.search);

   // Fetching
   const [
      fetchParameters,
      { data: databaseParameters, isSuccess: parametersSuccess, isUninitialized },
   ] = useGetAccountListPreferencesMutation();

   const [fetchAdvancedList, { data: dataAdvanced }] = useGetAdvancedUserListMutation();
   const [fetchBasicList, { data: dataBasic }] = useGetBasicUserListMutation();
   // const pageNo = parseInt(queryParams.get("pageNo")) || 1;
   // const recordsPerPage = parseInt(queryParams.get("records")) || 25;
   // const columnName = queryParams.get("column") || "id";
   // const order = queryParams.get("order") || "asc";

   const allRecords = dataBasic?.allRecords || 0;
   const allPages = dataBasic?.allPages || 0;

   useEffect(() => {
      fetchParameters();
   }, []);

   useEffect(() => {
      if (parametersSuccess) {
         const dbOrder = databaseParameters.orderAsc ? "asc" : "desc";

         setParams({
            pageNo: databaseParameters.page,
            recordsPerPage: databaseParameters.recordsPerPage,
            columnName: databaseParameters.orderBy,
            order: dbOrder,
            q: query,
         });

         setHeaders(
            headers.map((hd) =>
               hd.id === databaseParameters.orderBy
                  ? { ...hd, sort: dbOrder }
                  : { ...hd, sort: null }
            )
         );
      }
   }, [parametersSuccess]);

   useEffect(() => {
      if (parametersSuccess || !isUninitialized) {
         fetchBasicList(params);
      }
      setQueryParam("column", params.columnName);
      setQueryParam("order", params.order);
      setQueryParam("pageNo", String(params.pageNo));
      setQueryParam("records", String(params.recordsPerPage));
   }, [params, isUninitialized]);

   useEffect(() => {
      const sorted = headers.find((header) => header.sort);
      setParams({ ...params, columnName: sorted.id, order: sorted.sort, q: query });
   }, [headers, query]);

   useEffect(() => {
      const list = dataBasic?.list?.map((item) => [
         item.id,
         item.login,
         item.email,
         item.name,
         item.surname,
         <>
            {item.accessLevels?.map((accessLevel) => (
               <p key={`${item.login}-${accessLevel}`}>{accessLevel}</p>
            ))}
         </>,
         item.isActive ? <FaCheck className="check" /> : <></>,
         item.isRegistered ? <FaCheck className="check" /> : <></>,
         <div key={item.login} className={styles.actions}>
            {/* <Link to={`/users/${item.login}/edit`}> */}
            <button
               className={styles.edit}
               onClick={() => {
                  openEditModal(item.login);
               }}
            >
               <FaEdit />
            </button>
            {/* </Link> */}
            <Link to={`/users/${item.login}/info`}>
               <div role="button" className={styles.info}>
                  <FaAngleRight />
               </div>
            </Link>
         </div>,
      ]);

      list && setTableData(list);
   }, [dataBasic]);

   const setQueryParam = (key: string, value: string) => {
      queryParams.set(key, value);
      navigate({
         pathname: location.pathname,
         search: queryParams.toString(),
      });
   };

   return (
      <div className={styles.account_list_page_wrapper}>
         <ListUsersFilter query={query} setQuery={setQuery} />
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
         <EditAccountModal
            login={login}
            isOpen={editModalIsOpen}
            onSubmit={closeEditModal}
         />
      </div>
   );
};
