import React from "react";
import styles from "./ChangeEmailSettings.module.scss";
import { Button, Card } from "components/shared";
import { useTranslation } from "react-i18next";
import { useSendChangeOwnEmailLinkMutation } from "redux/service/api";

export const ChangeEmailSettings = () => {
   const { t } = useTranslation();

   const [mutation, { isLoading, isError, isSuccess }] =
      useSendChangeOwnEmailLinkMutation();

   return (
      <Card id="change-email" className={styles.card_wrapper}>
         <p className={`category-title ${styles.category_title}`}>{t("label.email")}</p>
         <p>{t("message.change.email")}</p>
         <Button
            onClick={() => {
               mutation({});
            }}
         >
            {t("message.send.link")}
         </Button>

         {isLoading && <p>{t("message.loading.change-email")}</p>}
         {isError && <p>{t("message.error.change-email")}</p>}
         {isSuccess && <p>{t("message.success.change-email")}</p>}
      </Card>
   );
};
