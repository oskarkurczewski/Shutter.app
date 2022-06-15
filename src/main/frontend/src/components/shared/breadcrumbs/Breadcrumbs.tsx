import React from "react";
import styles from "./Breadcrumbs.module.scss";
import { BiChevronRight } from "react-icons/bi";
import { Link, useLocation, useParams } from "react-router-dom";
import { useTranslation } from "react-i18next";

export const Breadcrumbs = () => {
   const location = useLocation();
   const { t } = useTranslation();
   const params = useParams();

   let fullURL = "";
   const crumbs = location.pathname
      .split("/")
      .filter(
         (crumb) => crumb !== "" && !Object.values(params).find((elem) => elem == crumb)
      )
      .map((crumb) => {
         fullURL += `${crumb}/`;
         return {
            fullURL,
            url: crumb.replace("-", "_"),
         };
      });

   return (
      <div className={styles.breadcumbs_wrapper}>
         <Link className="label" to="/">
            {t("navigation.home")}
         </Link>
         <BiChevronRight />
         {crumbs.map((crumb, index) => {
            const isLast = crumbs.length == index + 1;

            return (
               <React.Fragment key={index}>
                  {!isLast ? (
                     <Link
                        className={`label ${isLast ? styles.active : ""}`}
                        to={crumb.fullURL}
                     >
                        {t(`navigation.${crumb.url}`)}
                     </Link>
                  ) : (
                     <p className={`label ${styles.active}`}>
                        {t(`navigation.${crumb.url}`)}
                     </p>
                  )}

                  {!isLast && <BiChevronRight />}
               </React.Fragment>
            );
         })}
      </div>
   );
};
