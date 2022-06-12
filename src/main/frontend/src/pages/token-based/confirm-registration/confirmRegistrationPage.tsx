import React, { useEffect } from "react";
import styles from "./confirmRegistrationPage.module.scss";
import { Card } from "components/shared";
import { useParams } from "react-router-dom";
import { useConfirmRegistrationMutation } from "redux/service/tokenBasedService";
import { useTranslation } from "react-i18next";

export const ConfirmRegistrationPage = () => {
   const { t } = useTranslation();

   const { token } = useParams();

   const [confirmRegistrationMutation, { isLoading, isSuccess, isError, error }] =
      useConfirmRegistrationMutation();

   useEffect(() => {
      confirmRegistrationMutation(token);
   }, []);

   return (
      <section className={styles.confirm_registration_page_wrapper}>
         <Card className={styles.card_wrapper}>
            <p className="category-title">{t("confirm_register_page.title")}</p>
            {(() => {
               if (isLoading) {
                  return <p>{t("message.loading.register")}</p>;
               }
               if (isError) {
                  const err = error as {
                     data: {
                        message: string;
                     };
                  };

                  return <p className="error">{t(err.data.message)}</p>;
               }
               if (isSuccess) {
                  return <p>{t("confirm_register_page.success_message")}</p>;
               }
            })()}
         </Card>
      </section>
   );
};
