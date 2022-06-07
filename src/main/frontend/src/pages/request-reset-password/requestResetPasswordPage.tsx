import React, { useRef, useState } from "react";
import styles from "./requestResetPasswordPage.module.scss";
import { Button, Card, TextInput } from "components/shared";
import { useSendResetPasswordLinkMutation } from "redux/service/userSettingsService";
import { useTranslation } from "react-i18next";
import ReCAPTCHA from "react-google-recaptcha";

export const RequestResetPasswordPage = () => {
   const { t } = useTranslation();

   const recaptchaRef = useRef(null);

   const [login, setLogin] = useState<string>("");
   const [requestResetPasswordMutation, { isLoading, isSuccess, isError }] =
      useSendResetPasswordLinkMutation();

   const onSubmit = async (e) => {
      e.preventDefault();
      const captchaToken = await recaptchaRef.current.getValue();

      await requestResetPasswordMutation({ login: login, captcha: captchaToken });
   };

   return (
      <section className={styles.reset_password_page_wrapper}>
         <Card className={styles.card_wrapper}>
            <form>
               <p className="category-title">{t("label.dont-remember-password")}</p>
               <p>{t("message.info.email-instruction")}</p>
               <p>{t("message.info.type-login")}:</p>
               <TextInput
                  label={t("label.login-label")}
                  type="login"
                  placeholder={t("label.login-label")}
                  value={login}
                  onChange={(e) => setLogin(e.target.value)}
               />
               <ReCAPTCHA
                  ref={recaptchaRef}
                  sitekey="6LcOjh4gAAAAAJRdv-oKWqqj8565Bz6Y3QlmJv5L"
               />
               <div className={styles.footer}>
                  <Button onClick={(e) => onSubmit(e)}>
                     {t("message.info.send-email-message")}
                  </Button>
               </div>
               {(() => {
                  if (isLoading) {
                     return <p>{t("message.loading.reset-password")}</p>;
                  }
                  if (isError) {
                     return <p className="error">{t("message.error.reset-password")}</p>;
                  }
                  if (isSuccess) {
                     return <p>{t("message.success.reset-password")}</p>;
                  }
               })()}
            </form>
         </Card>
      </section>
   );
};

export default RequestResetPasswordPage;
