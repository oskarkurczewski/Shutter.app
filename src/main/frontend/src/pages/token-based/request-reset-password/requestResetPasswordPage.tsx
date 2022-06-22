import React, { useEffect, useRef, useState } from "react";
import styles from "./requestResetPasswordPage.module.scss";
import { Button, Card, TextInput } from "components/shared";
import { useSendResetPasswordLinkMutation } from "redux/service/userSettingsService";
import { useTranslation } from "react-i18next";
import ReCAPTCHA from "react-google-recaptcha";
import { useAppDispatch } from "redux/hooks";
import { push, ToastTypes } from "redux/slices/toastSlice";
import { ErrorResponse, Toast } from "types";
import { parseError } from "util/errorUtil";
import { useNavigate } from "react-router-dom";

export const RequestResetPasswordPage = () => {
   const { t } = useTranslation();
   const dispatch = useAppDispatch();
   const navigate = useNavigate();

   const recaptchaRef = useRef(null);

   const [login, setLogin] = useState<string>("");
   const [resetPassword, mutationState] = useSendResetPasswordLinkMutation();

   const onSubmit = async (e) => {
      e.preventDefault();
      const captchaToken = await recaptchaRef.current.getValue();

      await resetPassword({ login: login, captcha: captchaToken });
   };

   useEffect(() => {
      if (mutationState.isSuccess) {
         const successToast: Toast = {
            type: ToastTypes.SUCCESS,
            text: t("toast.success_send_reset_password_link_message"),
         };
         dispatch(push(successToast));
         navigate("/", { replace: true });
      }

      if (mutationState.isError) {
         let err = [];
         if (login == "") {
            err = ["validator.incorrect.login.regexp"];
         } else {
            err = parseError(mutationState.error as ErrorResponse);
         }

         const errorToast: Toast = {
            type: ToastTypes.ERROR,
            text: t(err),
         };
         dispatch(push(errorToast));
      }
   }, [mutationState]);

   return (
      <section className={styles.reset_password_page_wrapper}>
         <Card className={styles.card_wrapper}>
            <form>
               <p className="category-title">{t("request_reset_password_page.title")}</p>
               <p>{t("request_reset_password_page.details_message")}</p>
               <TextInput
                  label={t("global.label.login")}
                  type="login"
                  placeholder={t("global.label.login")}
                  value={login}
                  onChange={(e) => setLogin(e.target.value)}
               />
               <ReCAPTCHA
                  ref={recaptchaRef}
                  sitekey="6LeIxAcTAAAAAJcZVRqyHh71UMIEGNQ_MXjiZKhI"
               />
               <div className={styles.footer}>
                  <Button loading={mutationState.isLoading} onClick={(e) => onSubmit(e)}>
                     {t("request_reset_password_page.confirm")}
                  </Button>
               </div>
            </form>
         </Card>
      </section>
   );
};

export default RequestResetPasswordPage;
