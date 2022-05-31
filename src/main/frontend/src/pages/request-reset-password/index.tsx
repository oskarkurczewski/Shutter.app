import "./style.scss";
import Button from "components/shared/Button";
import Card from "components/shared/Card";
import TextInput from "components/shared/TextInput";
import React, { useRef, useState } from "react";
import { useRequestResetPasswordMutation } from "redux/service/api";
import { useTranslation } from "react-i18next";
import ReCAPTCHA from "react-google-recaptcha";

const RequestResetPasswordPage = () => {
   const { t } = useTranslation();

   const recaptchaRef = useRef(null);

   const [login, setLogin] = useState<string>("");
   const [requestResetPasswordMutation, { isLoading, isSuccess, isError }] =
      useRequestResetPasswordMutation();

   const onSubmit = async (e) => {
      e.preventDefault();
      const captchaToken = await recaptchaRef.current.getValue();

      await requestResetPasswordMutation({ login: login, captcha: captchaToken });
   };

   return (
      <section className="reset-password-page-wrapper">
         <Card>
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
               <div className="footer">
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
