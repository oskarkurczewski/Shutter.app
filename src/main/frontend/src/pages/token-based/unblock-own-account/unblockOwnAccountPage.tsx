import React, { useEffect } from "react";
import styles from "./unblockOwnAccountPage.module.scss";
import { useParams } from "react-router-dom";
import { Card } from "components/shared";
import { useUnblockAccountMutation } from "redux/service/tokenBasedService";
import { useTranslation } from "react-i18next";

export const UnblockOwnAccountPage = () => {
   const { t } = useTranslation();

   const { token } = useParams();

   const [unblockOwnAccountMutation, { isLoading, isSuccess, isError }] =
      useUnblockAccountMutation();

   useEffect(() => {
      unblockOwnAccountMutation(token);
   }, []);

   return (
      <Card className={styles.unblock_own_account_wrapper}>
         <p className="category-title">{t("label.unblock-account")}</p>
         {(() => {
            if (isLoading) {
               return <p>{t("message.loading.unblock-account")}</p>;
            }
            if (isError) {
               return <p className="error">{t("message.error.unblock-account")}.</p>;
            }
            if (isSuccess) {
               return <p>{t("message.success.unblock-account")}</p>;
            }
         })()}
      </Card>
   );
};
