import React, { useEffect, useRef, useState } from "react";
import "./style.scss";
import Card from "components/shared/Card";
import Table from "components/shared/Table";
import { Link, useLocation, useNavigate } from "react-router-dom";
import {
   FaAngleRight,
   FaCheck,
   FaChevronRight,
   FaEdit,
   FaInfoCircle,
} from "react-icons/fa";
import {
   useGetAccountListPreferencesMutation,
   useGetUserListMutation,
} from "redux/service/api";

const AccountListPage = () => {
   const [headers, setHeaders] = useState([
      {
         id: "id",
         label: "ID",
         sortable: true,
         sort: "asc",
      },
      {
         id: "login",
         label: "Login",
         sortable: true,
         sort: null,
      },
      {
         id: "email",
         label: "Email",
         sortable: true,
         sort: null,
      },
      {
         id: "name",
         label: "Name",
         sortable: true,
         sort: null,
      },
      {
         id: "surname",
         label: "Surname",
         sortable: true,
         sort: null,
      },
      {
         id: "roles",
         label: "Roles",
         sortable: false,
         sort: null,
      },
      {
         id: "registered",
         label: "Registered",
         sortable: false,
         sort: null,
      },
      {
         id: "active",
         label: "Active",
         sortable: false,
         sort: null,
      },
      {
         id: "actions",
         label: "",
         sortable: false,
         sort: null,
      },
   ]);

   const [tableData, setTableData] = useState([]);

   const [params, setParams] = useState({
      pageNo: 1,
      recordsPerPage: 25,
      columnName: "id",
      order: "asc",
   });

   const location = useLocation();
   const navigate = useNavigate();
   const queryParams = new URLSearchParams(location.search);

   // Fetching
   const [
      fetchParameters,
      { data: databaseParameters, isSuccess: parametersSuccess, isUninitialized },
   ] = useGetAccountListPreferencesMutation();

   const [fetchList, { data }] = useGetUserListMutation();
   // const pageNo = parseInt(queryParams.get("pageNo")) || 1;
   // const recordsPerPage = parseInt(queryParams.get("records")) || 25;
   // const columnName = queryParams.get("column") || "id";
   // const order = queryParams.get("order") || "asc";

   const allRecords = data?.allRecords || 0;
   const allPages = data?.allPages || 0;

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
         fetchList(params);
      }
      setQueryParam("column", params.columnName);
      setQueryParam("order", params.order);
      setQueryParam("pageNo", String(params.pageNo));
      setQueryParam("records", String(params.recordsPerPage));
   }, [params, isUninitialized]);

   useEffect(() => {
      const sorted = headers.find((header) => header.sort);
      setParams({ ...params, columnName: sorted.id, order: sorted.sort });
   }, [headers]);

   useEffect(() => {
      const list = data?.list?.map((item) => [
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
         <div key={item.login} className="actions">
            <Link to={`/users/${item.login}/edit`}>
               <div role="button" className="edit">
                  <FaEdit />
               </div>
            </Link>
            <Link to={`/users/${item.login}/info`}>
               <div role="button" className="info">
                  <FaAngleRight />
               </div>
            </Link>
         </div>,
      ]);

      list && setTableData(list);
   }, [data]);

   const setQueryParam = (key: string, value: string) => {
      queryParams.set(key, value);
      navigate({
         pathname: location.pathname,
         search: queryParams.toString(),
      });
   };

   return (
      <div className="account-list-page-wrapper">
         <Card className="table-card">
            <Table
               data={tableData}
               headers={headers}
               setHeaders={(headers) => {
                  setHeaders(headers);
               }}
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

export default AccountListPage;
