import React from "react";
import { useTranslation } from "react-i18next";
import "./style.scss";

interface Props {
   onSubmit: React.FormEventHandler<HTMLFormElement>;
   children?: JSX.Element | JSX.Element[];
   isLoading?: boolean;
   className?: string;
}

const Form: React.FC<Props> = ({ onSubmit, children, isLoading, className }) => {
   const { t } = useTranslation();

   return (
      <form onSubmit={onSubmit} className={`form-wrapper ${className && className}`}>
         {children}
         {isLoading && (
            <div className="loader-wrapper">
               <p>{t("message.loading.general")}</p>
            </div>
         )}
      </form>
   );
};

export default Form;
