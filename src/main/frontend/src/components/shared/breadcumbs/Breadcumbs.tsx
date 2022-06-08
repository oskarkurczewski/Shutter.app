import React from "react";
import styles from "./Breadcumbs.module.scss";
import { BsFillCaretLeftFill } from "react-icons/bs";
import { Link, useLocation, useNavigate } from "react-router-dom";

export const Breadcumbs = () => {
   const location = useLocation();
   const navigate = useNavigate();

   let fullURL = "";
   const crumbs = location.pathname
      .split("/")
      .filter((crumb) => crumb !== "")
      .map((crumb) => {
         fullURL += `${crumb}/`;
         return {
            fullURL,
            url: crumb,
         };
      });

   return (
      <div className={styles.breadcumbs_wrapper}>
         <BsFillCaretLeftFill onClick={() => navigate(-1)} />
         {crumbs.map((crumb, index) => (
            <>
               <Link to={crumb.fullURL} key={index}>
                  {crumb.url}
               </Link>
               <span>/</span>
            </>
         ))}
      </div>
   );
};
