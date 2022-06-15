import React, { useState } from "react";
import styles from "./MainSettings.module.scss";
import { Button, TextInput, Card } from "components/shared";
import { useChangeUserDataMutation } from "redux/service/userSettingsService";
import { useGetUserInfoQuery } from "redux/service/authService";
import { useTranslation } from "react-i18next";
import { useAppSelector } from "redux/hooks";

export const MainSettings = () => {
   const { t } = useTranslation();

   const [name, setName] = useState("");
   const [surname, setSurname] = useState("");

   const { username } = useAppSelector((state) => state.auth);
   const [mutation, { isLoading, isError, isSuccess }] = useChangeUserDataMutation();
   const { data } = useGetUserInfoQuery();

   return (
      <Card id="main-settings" className={styles.card_wrapper}>
         <p className={`category-title ${styles.category_title}`}>
            {t("settings_page.main_settings.title")}
         </p>
         <p>{t("settings_page.main_settings.details_message")}</p>
         <div className={styles.row}>
            <TextInput
               label={t("global.label.first_name")}
               placeholder={t("global.label.first_name")}
               required
               value={name}
               onChange={(e) => setName(e.target.value)}
            />
            <TextInput
               label={t("global.label.second_name")}
               placeholder={t("global.label.second_name")}
               required
               value={surname}
               onChange={(e) => setSurname(e.target.value)}
            />
         </div>

         {isError && <p>{t("message.error.main-settings")}</p>}
         {isSuccess && <p>{t("message.success.main-settings")}</p>}

         <Button
            loading={isLoading}
            onClick={() => {
               mutation({
                  data: {
                     name: data.data.name,
                     surname: data.data.surname,
                     login: username,
                  },
                  etag: data.etag,
               });
            }}
         >
            {t("settings_page.main_settings.confirm")}
         </Button>
      </Card>
   );
};
