import React, { useState } from "react";
import styles from "./changeOwnEmailPage.module.scss";
import { Button, Card, TextInput } from "components/shared";
import { useParams } from "react-router-dom";
import { useTranslation } from "react-i18next";
import { useChangeEmailMutation } from "redux/service/tokenBasedService";

export const ChangeOwnEmailPage = () => {
   const { t } = useTranslation();

   const [newEmail, setNewEmail] = useState("");
   const [confirmEmail, setConfirmEmail] = useState("");
   const [equalityError, setEqualityError] = useState(false);

   const { token } = useParams();

   const [changeEmailMutation, { isLoading, isSuccess, isError }] =
      useChangeEmailMutation();

   const onSubmit = () => {
      setEqualityError(false);
      if (newEmail == confirmEmail) {
         return changeEmailMutation({
            newEmail,
            token,
         });
      }
      setEqualityError(true);
   };

   return (
      <Card className={styles.change_own_email_wrapper}>
         <p className="category-title">{t("label.email-change")}</p>

         <TextInput
            label={t("label.email-new")}
            placeholder={t("label.email-short")}
            required
            value={newEmail}
            onChange={(e) => setNewEmail(e.target.value)}
         />
         <TextInput
            label={t("label.email-confirm")}
            placeholder={t("label.email-short")}
            required
            value={confirmEmail}
            onChange={(e) => setConfirmEmail(e.target.value)}
         />

         <Button onClick={onSubmit}>{t("label.submit")}</Button>

         {(() => {
            if (isLoading) {
               return <p>{t("message.loading.change-email")}</p>;
            }
            if (equalityError) {
               return <p className="error">{t("message.error.equality-error-email")}</p>;
            }
            if (isError) {
               return <p className="error">{t("message.error.failed-change-emial")}</p>;
            }
            if (isSuccess) {
               return <p>{t("message.success.changed-email")}.</p>;
            }
         })()}
      </Card>
   );
};
