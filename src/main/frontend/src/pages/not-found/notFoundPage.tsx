import { Button } from "components/shared";
import React from "react";
import { useTranslation } from "react-i18next";
import { Link } from "react-router-dom";
import styles from "./notFoundPage.module.scss";

export const NotFoundPage = () => {
   const { t } = useTranslation();

   return (
      <div className={styles.not_found_page}>
         <h1 className="text-huge">
            4<img src="/icons/logo.svg" alt="shutter logo" className={styles.app_logo} />4
         </h1>
         <p className="category-title">{t("not_found.not_found_message")}!</p>
         <Link to="/" replace>
            <Button onClick={null}>{t("not_found.go_back_message")}</Button>
         </Link>
      </div>
   );
};
