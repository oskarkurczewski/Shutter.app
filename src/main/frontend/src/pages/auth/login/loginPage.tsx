import React, { ChangeEvent, useEffect, useState } from "react";
import styles from "./loginPage.module.scss";
import { useAppDispatch } from "redux/hooks";
import { Link, useNavigate } from "react-router-dom";
import { useLoginMutation } from "redux/service/authService";
import { login } from "redux/slices/authSlice";
import { LoginRequest } from "redux/types/api/authTypes";
import { useTranslation } from "react-i18next";
import { Button, Card, TextInput } from "components/shared";
import { getLoginPayload } from "util/loginUtil";
import { useGetAccountLocaleMutation } from "redux/service/userSettingsService";
import { ErrorResponse, Toast } from "types";
import { push, ToastTypes } from "redux/slices/toastSlice";

export const LoginPage: React.FC = () => {
   const { t, i18n } = useTranslation();

   const navigate = useNavigate();
   const dispatch = useAppDispatch();

   const [show2FAPage, setShow2FAPage] = useState(false);
   const [formState, setFormState] = useState<LoginRequest>({
      login: "",
      password: "",
   });

   const [loginMutation, loginMutationState] = useLoginMutation();
   const [getLocale] = useGetAccountLocaleMutation();

   const handleChange = ({ target: { name, value } }: ChangeEvent<HTMLInputElement>) =>
      setFormState((prev) => ({ ...prev, [name]: value }));

   const onSubmit = async (e) => {
      e.preventDefault();
      const token = await loginMutation(formState).unwrap();
      localStorage.setItem("token", token.token);
      dispatch(login(getLoginPayload()));
      const language = await getLocale().unwrap();
      await i18n.changeLanguage(language.languageTag);
      navigate("/");
   };

   useEffect(() => {
      if (loginMutationState.isSuccess) {
         const successToast: Toast = {
            type: ToastTypes.SUCCESS,
            text: t("toast.success_login_message"),
         };
         dispatch(push(successToast));
      }
      if (loginMutationState.isError) {
         const err = loginMutationState.error as ErrorResponse;

         if (err.data.message == "exception.2_fa_code_required") {
            setShow2FAPage(true);
         } else {
            const successToast: Toast = {
               type: ToastTypes.ERROR,
               text: t("exception.invalid_credentials"),
            };
            dispatch(push(successToast));
         }
      }
   }, [loginMutationState]);

   return (
      <section className={styles.login_page_wrapper}>
         <Card className={styles.card_wrapper}>
            <div className={styles.aside}>
               <img src="images/logo_new_black.svg" alt="logo" />
               <div>
                  <div>
                     <p className="section-title">
                        {t("login_page.not_user_message_up")}
                     </p>
                     <p className="section-title">
                        {t("login_page.not_user_message_down")}
                     </p>
                  </div>
                  <div>
                     <p>{t("login_page.inviting_message_up")}</p>
                     <p>{t("login_page.inviting_message_down")}</p>
                  </div>
               </div>
               <Button
                  className={styles.button_wrapper}
                  onClick={() => {
                     navigate("/register");
                  }}
               >
                  {t("login_page.sign_up")}
               </Button>
            </div>
            <form onSubmit={onSubmit}>
               <p className="section-title">{t("login_page.form_title")}</p>
               {!show2FAPage ? (
                  <>
                     <TextInput
                        label={t("global.label.login")}
                        placeholder={t("global.label.login")}
                        type="text"
                        name="login"
                        value={formState.login}
                        onChange={handleChange}
                     />
                     <TextInput
                        label={t("global.label.password")}
                        type="password"
                        placeholder={t("global.label.password")}
                        value={formState.password}
                        name="password"
                        onChange={handleChange}
                     />
                  </>
               ) : (
                  <>
                     <p>{t("login_page.twoFA_desc")}</p>
                     <TextInput
                        label={t("global.label.2fa_code")}
                        placeholder="000000"
                        value={formState.twoFACode || ""}
                        name="twoFACode"
                        onChange={handleChange}
                     />
                  </>
               )}

               <div className={styles.footer}>
                  <Link to={"/request-reset-password"}>
                     {t("login_page.recover_password")}
                  </Link>
                  <Button
                     loading={loginMutationState.isLoading}
                     onClick={(e) => onSubmit(e)}
                  >
                     {t("login_page.sign_in")}
                  </Button>
               </div>
            </form>
         </Card>
      </section>
   );
};
