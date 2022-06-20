import React, { useState } from "react";
import styles from "./ChangeDescriptionSettings.module.scss";
import { Button, Card, TextArea } from "components/shared";
import { useTranslation } from "react-i18next";
import { useSendChangeDescriptionLinkMutation } from "redux/service/userSettingsService";

export const ChangeDescriptionSettings = () => {
   const { t } = useTranslation();

   const [mutation, { isLoading, isError, isSuccess }] =
      useSendChangeDescriptionLinkMutation();
   const [newDescription, setNewDescription] = useState("");

   return (
      <Card id="change-description" className={styles.card_wrapper}>
         <p className={`category-title ${styles.category_title}`}>
            {t("settings_page.change_description.title")}
         </p>
         <p>{t("settings_page.change_description.details_message")}</p>

         <TextArea
            className={styles.textarea}
            label={t("settings_page.change_description.label_message")}
            placeholder={t("settings_page.change_description.placeholder_message")}
            required
            value={newDescription}
            onChange={(e) => setNewDescription(e.target.value)}
            rows={8}
            maxLength={4096}
         />

         <Button
            onClick={() => {
               mutation({ content: newDescription });
            }}
         >
            {t("settings_page.change_description.confirm")}
         </Button>

         {isLoading && <p>{t("")}</p>}
         {isError && <p>{t("settings_page.change_description.error_message")}</p>}
         {isSuccess && <p>{t("settings_page.change_description.success_message")}</p>}
      </Card>
   );
};
