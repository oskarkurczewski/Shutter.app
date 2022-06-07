import React, { useEffect } from "react";
import styles from "./confirmRegistrationPage.module.scss";
import Card from "components/shared/card/Card";
import { useParams } from "react-router-dom";
import { useConfirmRegistrationMutation } from "redux/service/api";
import { useTranslation } from "react-i18next";

const ConfirmRegistrationPage = () => {
   const { t } = useTranslation();

   const { token } = useParams();

   const [confirmRegistrationMutation, { isLoading, isSuccess, isError }] =
      useConfirmRegistrationMutation();

   useEffect(() => {
      confirmRegistrationMutation(token);
   }, []);

   return (
      <section className={styles.confirm_registration_page_wrapper}>
         <Card className={styles.card_wrapper}>
            <p className="category-title">{t("label.register-account")}</p>
            {(() => {
               if (isLoading) {
                  return <p>{t("message.loading.register")}</p>;
               }
               if (isError) {
                  return <p className="error">{t("message.error.register-confirm")}</p>;
               }
               if (isSuccess) {
                  return <p>{t("message.success.regiter-confirm")}</p>;
               }
            })()}
         </Card>
      </section>
   );
};

export default ConfirmRegistrationPage;
