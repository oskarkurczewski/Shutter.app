import React from "react";
import styles from "./ChangeEmailSettings.module.scss";
import { Button, Card } from "components/shared";
import { useTranslation } from "react-i18next";
import { useSendChangeEmailLinkMutation } from "redux/service/userSettingsService";

export const ChangeEmailSettings = () => {
   const { t } = useTranslation();

   const [mutation, { isLoading, isError, isSuccess }] = useSendChangeEmailLinkMutation();

   return (
      <Card id="change-email" className={styles.card_wrapper}>
         <p className={`category-title ${styles.category_title}`}>
            {t("settings_page.email_settings.title")}
         </p>
         <p>{t("settings_page.email_settings.details_message")}</p>
         <Button
            loading={isLoading}
            onClick={() => {
               mutation();
            }}
         >
            {t("settings_page.email_settings.send_link")}
         </Button>

         {isError && <p>{t("message.error.change-email")}</p>}
         {isSuccess && <p>{t("message.success.change-email")}</p>}
      </Card>
   );
};
