/* eslint-disable @typescript-eslint/no-unused-vars */
import React, { ReactNode } from "react";
import styles from "./Table.module.scss";
import { VscTriangleUp, VscTriangleDown } from "react-icons/vsc";
import {
   HiChevronDoubleLeft,
   HiChevronDoubleRight,
   HiChevronLeft,
   HiChevronRight,
} from "react-icons/hi";

import { Dropdown, SquareButton } from "components/shared";
import { tableHeader } from "types/ComponentTypes";

interface Props {
   data: (string | ReactNode)[][];
   headers: tableHeader[];
   setHeaders: (header: tableHeader[]) => void;
   allRecords: number;
   allPages: number;
   pageNo: number;
   setPageNo: (number: number) => void;
   recordsPerPage: number;
   setRecordsPerPage: (number: number) => void;
}

export const Table: React.FC<Props> = ({
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
      const newHeaders = (headers: tableHeader[]) => {
         return headers.map((header) => {
            return header.id === id
               ? { ...header, sort: order }
               : { ...header, sort: null };
         });
      };

      setHeaders(newHeaders(headers));
   };
   return (
      <div className={styles.table_wrapper}>
         <table>
            <thead>
               <tr className={styles.table_header}>
                  {headers.map((header, index) => (
                     <th key={header.id}>
                        <div className={styles.cell_wrapper}>
                           <div className="label">{header.label}</div>
                           <div>
                              {header.sortable && (
                                 <div
                                    role="button"
                                    tabIndex={index}
                                    onKeyDown={() => {
                                       return;
                                    }}
                                    className={`${styles.sort} ${
                                       header.sort
                                          ? header.sort === "asc"
                                             ? styles.asc
                                             : styles.desc
                                          : ""
                                    } `}
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
                                    <VscTriangleUp className={styles.up} />
                                    <VscTriangleDown className={styles.down} />
                                 </div>
                              )}
                              {/* <div className="drag" /> */}
                              <div className={styles.separator} />
                           </div>
                        </div>
                     </th>
                  ))}
               </tr>
            </thead>
            <tbody>
               {data.map((row, rowIndex) => (
                  <tr key={rowIndex} className={styles.table_row}>
                     {row.map((item, columnIndex) => (
                        <td key={`${rowIndex}.${columnIndex}`}>{item}</td>
                     ))}
                  </tr>
               ))}
            </tbody>
         </table>
         <div className={styles.pages}>
            <div className={styles.pages_wrapper}>
               <div className={styles.all_records}>{allRecords} wyników</div>
               <div className={styles.line} />
               <div className={styles.change_page_buttons}>
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
               <div>
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
         </div>
      </div>
   );
};
