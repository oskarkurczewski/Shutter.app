/* eslint-disable @typescript-eslint/no-unused-vars */
import React, { ReactNode } from "react";
import "./style.scss";
import { VscTriangleUp, VscTriangleDown } from "react-icons/vsc";
import SquareButton from "../SquareButton";
import {
   HiChevronDoubleLeft,
   HiChevronDoubleRight,
   HiChevronLeft,
   HiChevronRight,
} from "react-icons/hi";

import Dropdown from "../Dropdown";

type header = {
   id: string;
   label: string;
   sort: string | null;
};

interface Props {
   data: (string | ReactNode)[][];
   headers: header[];
   setHeaders: (header: header[]) => void;
   allRecords: number;
   allPages: number;
   pageNo: number;
   setPageNo: (number: number) => void;
   recordsPerPage: number;
   setRecordsPerPage: (number: number) => void;
}

const Table: React.FC<Props> = ({
   data,
   headers,
   setHeaders,
   allRecords,
   allPages,
   pageNo,
   setPageNo,
   recordsPerPage,
   setRecordsPerPage,
}) => {
   const changeOrder = (id: string, order: string) => {
      const newHeaders = (headers: header[]) => {
         return headers.map((header) => {
            return header.id === id
               ? { ...header, sort: order }
               : { ...header, sort: null };
         });
      };

      setHeaders(newHeaders(headers));
   };

   return (
      <div className="table-wrapper">
         <header></header>
         <table className="table">
            <tbody>
               <tr className="table-header">
                  {headers.map((header, index) => (
                     <th key={header.id}>
                        <div className="cell-wrapper">
                           <div className="label">{header.label}</div>
                           <div>
                              <div
                                 role="button"
                                 tabIndex={index}
                                 onKeyDown={() => {
                                    return;
                                 }}
                                 className={`sort ${header.sort}`}
                                 onClick={() => {
                                    switch (header.sort) {
                                       case "desc":
                                          changeOrder(header.id, "asc");
                                          break;
                                       case "asc":
                                          changeOrder(header.id, "desc");
                                          break;

                                       default:
                                          changeOrder(header.id, "asc");
                                          break;
                                    }
                                 }}
                              >
                                 <VscTriangleUp className="up" />
                                 <VscTriangleDown className="down" />
                              </div>
                              {/* <div className="drag" /> */}
                              <div className="separator" />
                           </div>
                        </div>
                     </th>
                  ))}
               </tr>
               {data.map((row, rowIndex) => (
                  <tr key={rowIndex} className="table-row">
                     {row.map((item, columnIndex) => (
                        <td key={`${rowIndex}.${columnIndex}`}>{item}</td>
                     ))}
                  </tr>
               ))}
            </tbody>
         </table>
         <footer className="pages">
            <div className="pages-wrapper">
               <div className="all-records">{allRecords} wyników</div>
               <div className="line" />
               <div className="change-page-buttons">
                  <SquareButton
                     onClick={() => {
                        setPageNo(1);
                     }}
                     disabled={pageNo == 1}
                  >
                     <HiChevronDoubleLeft />
                  </SquareButton>
                  <SquareButton
                     onClick={() => {
                        setPageNo(pageNo - 1);
                     }}
                     disabled={pageNo == 1}
                  >
                     <HiChevronLeft />
                  </SquareButton>
                  <SquareButton disabled>{pageNo}</SquareButton>
                  <SquareButton
                     onClick={() => {
                        setPageNo(pageNo + 1);
                     }}
                     disabled={pageNo == allPages}
                  >
                     <HiChevronRight />
                  </SquareButton>
                  <SquareButton
                     onClick={() => {
                        setPageNo(allPages);
                     }}
                     disabled={pageNo == allPages}
                  >
                     <HiChevronDoubleRight />
                  </SquareButton>
               </div>
               <div className="change-records-per-page">
                  <Dropdown
                     values={[10, 25, 50, 100]}
                     name="recordsPerPage"
                     id="recordsPerPage"
                     selectedValue={recordsPerPage}
                     onChange={(e) => {
                        setRecordsPerPage(parseInt(e.target.value));
                     }}
                  />
               </div>
            </div>
         </footer>
      </div>
   );
};

export default Table;
