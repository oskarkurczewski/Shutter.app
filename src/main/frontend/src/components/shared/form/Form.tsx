import React from "react";
import { useTranslation } from "react-i18next";
import styles from "./Form.module.scss";

interface Props {
   onSubmit: React.FormEventHandler<HTMLFormElement>;
   children?: JSX.Element | JSX.Element[];
   isLoading?: boolean;
   className?: string;
}

export const Form: React.FC<Props> = ({ onSubmit, children, isLoading, className }) => {
   const { t } = useTranslation();

   return (
      <form
         onSubmit={onSubmit}
         className={`${styles.form_wrapper} ${className && className}`}
      >
         {children}
         {isLoading && (
            <div className={styles.loader_wrapper}>
               <p>{t("message.loading.general")}</p>
            </div>
         )}
      </form>
   );
};
