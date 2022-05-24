import React from "react";
import { useTranslation } from "react-i18next";
import { Link } from "react-router-dom";
import "./style.scss";

const NotFound404 = () => {
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

export default NotFound404;
