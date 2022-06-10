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
            {t("label.settings")}
         </p>
         <p>{t("message.info.main-settings")}</p>
         <div className={styles.row}>
            <TextInput
               label={t("label.first-name")}
               placeholder={t("label.first-name")}
               required
               value={name}
               onChange={(e) => setName(e.target.value)}
            />
            <TextInput
               label={t("label.second-name")}
               placeholder={t("label.second-name")}
               required
               value={surname}
               onChange={(e) => setSurname(e.target.value)}
            />
         </div>

         {isLoading && <p>{t("message.loading.main-settings")}</p>}
         {isError && <p>{t("message.error.main-settings")}</p>}
         {isSuccess && <p>{t("message.success.main-settings")}</p>}

         <Button
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
            {t("label.change")}
         </Button>
      </Card>
   );
};
