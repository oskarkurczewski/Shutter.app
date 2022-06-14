import React from "react";
import styles from "./SuspenseLoader.module.scss";
import { Loader } from "components/shared";
import { useTranslation } from "react-i18next";

export const SuspenseLoader = () => {
   const { t } = useTranslation();

   return (
      <div className={styles.suspense_loader_wrapper}>
         <Loader className={styles.loader} />
         <p className="label">{t("global.label.loading")}</p>
      </div>
   );
};
