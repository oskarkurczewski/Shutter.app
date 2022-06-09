import React from "react";
import { useTranslation } from "react-i18next";
import { Link } from "react-router-dom";

export const NotFoundPage = () => {
   const { t } = useTranslation();

   return (
      <div className="content">
         <p>NotFound404</p>
         <Link to="/" replace>
            {t("message.info.go-back-404")}
         </Link>
      </div>
   );
};
