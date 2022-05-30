import "./style.scss";
import Button from "components/shared/Button";
import Card from "components/shared/Card";
import TextInput from "components/shared/TextInput";
import React, { useState } from "react";
import { useResetPasswordMutation } from "redux/service/api";
import { useParams } from "react-router-dom";
import { useTranslation } from "react-i18next";

const ResetPasswordPage = () => {
   const { t } = useTranslation();

   const { token } = useParams();
   const [password, setPassword] = useState<string>("");
   const [resetPasswordMutation, { isLoading, isSuccess, isError, error }] =
      useResetPasswordMutation();

   const onSubmit = async (e) => {
      e.preventDefault();
      await resetPasswordMutation({ token: token, newPassword: password });
   };

   return (
      <section className="reset-password-page-wrapper">
         <Card>
            <form>
               <p className="category-title">{t("label.reset-password")}</p>
               <p>{t("message.info.password-reset-procedure")}</p>
               <TextInput
                  label={t("label.new-password")}
                  type="password"
                  placeholder={t("label.password")}
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
               />
               <div className="footer">
                  <Button onClick={(e) => onSubmit(e)}>
                     {t("label.reset-password")}
                  </Button>
               </div>
               {(() => {
                  if (isLoading) {
                     return <p>{t("message.loading.reset-password")}</p>;
                  }
                  if (isError) {
                     const err = error as any;
                     if (
                        err.status === 400 &&
                        err.data.message === "exception.password.not_unique"
                     ) {
                        return (
                           <p className="error">
                              {t("message.error.password-not-unique")}
                           </p>
                        );
                     }
                     return <p className="error">{t("message.error.change-password")}</p>;
                  }
                  if (isSuccess) {
                     return <p>{t("message.success.change-email")}</p>;
                  }
               })()}
            </form>
         </Card>
      </section>
   );
};

export default ResetPasswordPage;
