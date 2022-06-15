import React, { useState } from "react";
import styles from "./ChangePasswordSettings.module.scss";
import { Button, Card, TextInput } from "components/shared";
import { useTranslation } from "react-i18next";
import { useChangePasswordMutation } from "redux/service/userSettingsService";

export const ChangePasswordSettings = () => {
   const { t } = useTranslation();

   const [oldPassword, setOldPassword] = useState("");
   const [password, setPassword] = useState("");
   const [confirmPassword, setConfirmPassword] = useState("");

   const [equalityError, setEqualityError] = useState(false);

   const [mutation, { isLoading, isError, isSuccess, error }] =
      useChangePasswordMutation();

   const onSubmit = () => {
      setEqualityError(false);
      if (password == confirmPassword) {
         return mutation({
            oldPassword,
            password,
         });
      }
      setEqualityError(true);
   };

   return (
      <Card id="change-password" className={styles.card_wrapper}>
         <p className={`category-title ${styles.category_title}`}>
            {t("settings_page.password_settings.title")}
         </p>
         <p>{t("settings_page.password_settings.details_message")}</p>
         <div className={styles.row}>
            <TextInput
               label={t("global.label.current_password")}
               placeholder={t("global.label.current_password")}
               type="password"
               required
               value={oldPassword}
               onChange={(e) => setOldPassword(e.target.value)}
            />
         </div>
         <div className={styles.row}>
            <TextInput
               label={t("global.label.password")}
               placeholder={t("global.label.password")}
               type="password"
               required
               value={password}
               onChange={(e) => setPassword(e.target.value)}
            />
            <TextInput
               label={t("global.label.repeat_password")}
               placeholder={t("global.label.repeat_password")}
               type="password"
               required
               value={confirmPassword}
               onChange={(e) => setConfirmPassword(e.target.value)}
            />
         </div>

         {(() => {
            if (equalityError) {
               return (
                  <p className="error">{t("message.error.equality-error-password")}</p>
               );
            }
            if (isError) {
               const err = error as any;
               if (
                  err.status === 400 &&
                  err.data.message === "exception.password.not_unique"
               ) {
                  return (
                     <p className="error">{t("message.error.password-not-unique")}</p>
                  );
               }
               return <p className="error">{t("message.error.change-password")}</p>;
            }
            if (isSuccess) {
               return <p>{t("message.success.change-password")}</p>;
            }
         })()}

         <Button loading={isLoading} onClick={onSubmit}>
            {t("settings_page.password_settings.confirm")}
         </Button>
      </Card>
   );
};
