import React, { useEffect, useState } from "react";
import "./style.scss";
import Card from "components/shared/Card";
import Table from "components/shared/Table";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { FaCheck, FaEdit } from "react-icons/fa";
import { useGetUserListQuery } from "redux/service/api";

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
         id: "edit",
         label: "Edit",
         sortable: false,
         sort: null,
      },
   ]);

   const location = useLocation();
   const navigate = useNavigate();
   const queryParams = new URLSearchParams(location.search);

   const pageNo = parseInt(queryParams.get("pageNo")) || 1;
   const recordsPerPage = parseInt(queryParams.get("records")) || 25;
   const columnName = queryParams.get("column") || "id";
   const order = queryParams.get("order") || "asc";

   const [tableData, setTableData] = useState([]);
   const { data } = useGetUserListQuery({
      pageNo,
      recordsPerPage,
      columnName,
      order,
   });

   const setQueryParam = (key: string, value: string) => {
      queryParams.set(key, value);
      navigate({
         pathname: location.pathname,
         search: queryParams.toString(),
      });
   };

   const allRecords = data?.allRecords || 0;
   const allPages = data?.allPages || 0;

   useEffect(() => {
      const sorted = headers.find((header) => header.sort);
      setQueryParam("column", sorted.id);
      setQueryParam("order", sorted.sort);
      setQueryParam("pageNo", "1");
   }, [headers]);

   useEffect(() => {
      setTableData([]);
      data?.list.forEach((item) => {
         setTableData((a) => [
            ...a,
            [
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
               <div key={item.login} className="edit-button">
                  <Link to={`/users/${item.login}/edit`}>
                     <div role="button">
                        <FaEdit />
                     </div>
                  </Link>
               </div>,
            ],
         ]);
      });
   }, [data]);

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
               pageNo={pageNo}
               setPageNo={(num) => setQueryParam("pageNo", num.toString())}
               recordsPerPage={recordsPerPage}
               setRecordsPerPage={(num) => {
                  setQueryParam("records", num.toString());
                  setQueryParam("pageNo", "1");
               }}
            />
         </Card>
      </div>
   );
};

export default AccountListPage;
