/* eslint-disable @typescript-eslint/no-unused-vars */
import React, { useEffect, useState } from "react";
import "./style.scss";
import Card from "components/shared/Card";
import Table from "components/shared/Table";
import UserFilter from "components/UserFilter";
import axios from "axios";
import { apiUrl } from "App";
import { Link } from "react-router-dom";
import { FaEdit } from "react-icons/fa";
import { useGetUserListQuery } from "redux/service/api";

const AccountListPage = () => {
   const [headers, setHeaders] = useState([
      {
         id: "login",
         label: "Login",
         sortable: true,
         sort: "asc",
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
         id: "registered",
         label: "Registered",
         sortable: true,
         sort: null,
      },
      {
         id: "active",
         label: "Active",
         sortable: true,
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
   const tableData = [
      [
         "Jay Lewis",
         "afujarwa@dise.cd",
         "Peter",
         "Waters",
         "true",
         "true",
         <td key="1">
            <Link to={"/users/peter/edit"}>
               <div className="edit-button" role="button">
                  <FaEdit />
               </div>
            </Link>
         </td>,
      ],
      [
         "David Castillo",
         "zuj@hah.ly",
         "Kathryn",
         "Olson",
         "true",
         "true",
         <td key="1">
            <Link to={"/users/peter/edit"}>
               <div className="edit-button" role="button">
                  <FaEdit />
               </div>
            </Link>
         </td>,
      ],
      [
         "Gary Cox",
         "midonfu@ec.om",
         "Jason",
         "Johnston",
         "true",
         "true",
         <td key="1">
            <Link to={"/users/peter/edit"}>
               <div className="edit-button" role="button">
                  <FaEdit />
               </div>
            </Link>
         </td>,
      ],
      [
         "Lena Doyle",
         "jivusev@vime.er",
         "Norman",
         "Fields",
         "true",
         "true",
         <td key="1">
            <Link to={"/users/peter/edit"}>
               <div className="edit-button" role="button">
                  <FaEdit />
               </div>
            </Link>
         </td>,
      ],
      [
         "Derrick Myers",
         "fej@tejjehpu.ng",
         "Marc",
         "Lloyd",
         "true",
         "true",
         <td key="1">
            <Link to={"/users/peter/edit"}>
               <div className="edit-button" role="button">
                  <FaEdit />
               </div>
            </Link>
         </td>,
      ],
      [
         "Nellie Reynolds",
         "adubo@suzepnoz.cx",
         "Essie",
         "Barnett",
         "true",
         "true",
         <td key="1">
            <Link to={"/users/peter/edit"}>
               <div className="edit-button" role="button">
                  <FaEdit />
               </div>
            </Link>
         </td>,
      ],
      [
         "Ethel French",
         "camzafaj@nadjuc.sk",
         "Lettie",
         "Russell",
         "true",
         "true",
         <td key="1">
            <Link to={"/users/peter/edit"}>
               <div className="edit-button" role="button">
                  <FaEdit />
               </div>
            </Link>
         </td>,
      ],
      [
         "Marguerite Marsh",
         "jaero@ko.sc",
         "Minnie",
         "Hicks",
         "true",
         "true",
         <td key="1">
            <Link to={"/users/peter/edit"}>
               <div className="edit-button" role="button">
                  <FaEdit />
               </div>
            </Link>
         </td>,
      ],
      [
         "Glen Morton",
         "cohziagu@boces.gh",
         "Richard",
         "Ross",
         "true",
         "true",
         <td key="1">
            <Link to={"/users/peter/edit"}>
               <div className="edit-button" role="button">
                  <FaEdit />
               </div>
            </Link>
         </td>,
      ],
      [
         "Austin Wilkerson",
         "ribarfob@zolgopa.ki",
         "Addie",
         "Leonard",
         "true",
         "true",
         <td key="1">
            <Link to={"/users/peter/edit"}>
               <div className="edit-button" role="button">
                  <FaEdit />
               </div>
            </Link>
         </td>,
      ],
      [
         "Rachel Morrison",
         "wi@mimgip.tl",
         "Luella",
         "Cannon",
         "true",
         "true",
         <td key="1">
            <Link to={"/users/peter/edit"}>
               <div className="edit-button" role="button">
                  <FaEdit />
               </div>
            </Link>
         </td>,
      ],
      [
         "Marguerite Black",
         "wusa@ofabi.ne",
         "Pearl",
         "Adams",
         "true",
         "true",
         <td key="1">
            <Link to={"/users/peter/edit"}>
               <div className="edit-button" role="button">
                  <FaEdit />
               </div>
            </Link>
         </td>,
      ],
      [
         "Seth Brewer",
         "kegasgaz@eviba.ug",
         "Amy",
         "Bradley",
         "true",
         "true",
         <td key="1">
            <Link to={"/users/peter/edit"}>
               <div className="edit-button" role="button">
                  <FaEdit />
               </div>
            </Link>
         </td>,
      ],
      [
         "Austin Watson",
         "mezonuk@ru.ss",
         "Sallie",
         "Cortez",
         "true",
         "true",
         <td key="1">
            <Link to={"/users/peter/edit"}>
               <div className="edit-button" role="button">
                  <FaEdit />
               </div>
            </Link>
         </td>,
      ],
   ];

   const [listRequestParams, setListRequestParams] = useState({
      pageNo: 1,
      recordsPerPage: 25,
      columnName: "login",
      order: "asc",
   });

   const {
      data = {},
      isLoading,
      isFetching,
      isError,
   } = useGetUserListQuery(listRequestParams);

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
      console.log(data);
      return;
   }, [headers]);

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
