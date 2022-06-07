import React, { useState } from "react";
import styles from "./ChangePasswordSettings.module.scss";
import Button from "components/shared/button/Button";
import Card from "components/shared/card/Card";
import TextInput from "components/shared/text-input/TextInput";
import { useTranslation } from "react-i18next";
import { useChangeOwnPasswordMutation } from "redux/service/api";

const ChangePasswordSettings = () => {
   const { t } = useTranslation();

   const [oldPassword, setOldPassword] = useState("");
   const [password, setPassword] = useState("");
   const [confirmPassword, setConfirmPassword] = useState("");

   const [equalityError, setEqualityError] = useState(false);

   const [mutation, { isLoading, isError, isSuccess, error }] =
      useChangeOwnPasswordMutation();

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
            {t("label.password")}
         </p>
         <p>{t("message.change.password")}</p>
         <div className={styles.row}>
            <TextInput
               label={t("label.current-password")}
               placeholder={t("label.password")}
               type="password"
               required
               value={oldPassword}
               onChange={(e) => setOldPassword(e.target.value)}
            />
         </div>
         <div className={styles.row}>
            <TextInput
               label={t("label.password")}
               placeholder={t("label.password")}
               type="password"
               required
               value={password}
               onChange={(e) => setPassword(e.target.value)}
            />
            <TextInput
               label={t("label.repeat-password")}
               placeholder={t("label.password")}
               type="password"
               required
               value={confirmPassword}
               onChange={(e) => setConfirmPassword(e.target.value)}
            />
         </div>

         {(() => {
            if (isLoading) {
               return <p>{t("message.loading.change-password")}</p>;
            }
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

         <Button onClick={onSubmit}>{t("label.change")}</Button>
      </Card>
   );
};

export default ChangePasswordSettings;
