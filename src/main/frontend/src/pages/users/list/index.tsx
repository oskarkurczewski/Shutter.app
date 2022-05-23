import React, { useEffect, useState } from "react";
import "./style.scss";
import Card from "components/shared/Card";
import Table from "components/shared/Table";
import { Link } from "react-router-dom";
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
         label: "",
         sortable: false,
         sort: null,
      },
   ]);
   const [pageNo, setPageNo] = useState(1);
   const [recordsPerPage, setRecordsPerPage] = useState(25);
   const allRecords = 2137;
   const allPages = 24;

   const [listRequestParams, setListRequestParams] = useState({
      pageNo: 1,
      recordsPerPage: 25,
      columnName: "id",
      order: "asc",
   });
   const [tableData, setTableData] = useState([]);
   const { data } = useGetUserListQuery(listRequestParams);

   useEffect(() => {
      headers.map((header) => {
         if (header.sort) {
            setListRequestParams({
               ...listRequestParams,
               columnName: header.id,
               order: header.sort,
            });
         }
      });
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
                  {item.accessLevels.map((accessLevel) => (
                     <p key={`${item.login}-${accessLevel}`}>{accessLevel}</p>
                  ))}
               </>,
               item.isActive ? <FaCheck className="check" /> : <></>,
               item.isRegistered ? <FaCheck className="check" /> : <></>,
               <td key={item.login} className="edit-button">
                  <Link to={`/users/${item.login}/edit`}>
                     <div role="button">
                        <FaEdit />
                     </div>
                  </Link>
               </td>,
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
               setPageNo={setPageNo}
               recordsPerPage={recordsPerPage}
               setRecordsPerPage={setRecordsPerPage}
            />
         </Card>
      </div>
   );
};

export default AccountListPage;
