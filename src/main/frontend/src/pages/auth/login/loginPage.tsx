import React, { ChangeEvent, useState } from "react";
import styles from "./loginPage.module.scss";
import { useAppDispatch } from "redux/hooks";
import { Link, useNavigate } from "react-router-dom";
import { useLoginMutation, useSendTwoFACodeMutation } from "redux/service/authService";
import { login } from "redux/slices/authSlice";
import { LoginRequest } from "redux/types/api/authTypes";
import { useTranslation } from "react-i18next";
import { Button, Card, TextInput } from "components/shared";
import { getLoginPayload } from "util/loginUtil";
import { useGetAccountLocaleMutation } from "redux/service/userSettingsService";

export const LoginPage: React.FC = () => {
   const { t, i18n } = useTranslation();

   const navigate = useNavigate();
   const dispatch = useAppDispatch();

   const [formState, setFormState] = useState<LoginRequest>({
      login: "",
      password: "",
      twoFACode: "000000",
   });

   const [loginMutation, loginMutationState] = useLoginMutation();
   const [sendTwoFACodeMutation, sendTwoFACodeMutationState] = useSendTwoFACodeMutation();
   const [getLocale] = useGetAccountLocaleMutation();

   const handleChange = ({ target: { name, value } }: ChangeEvent<HTMLInputElement>) =>
      setFormState((prev) => ({ ...prev, [name]: value }));

   const onSendTwoFA = (e: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
      e.preventDefault();
      sendTwoFACodeMutation(formState.login);
   };

   const onSubmit = async (e) => {
      e.preventDefault();
      const token = await loginMutation(formState).unwrap();
      localStorage.setItem("token", token.token);
      dispatch(login(getLoginPayload()));
      const language = await getLocale().unwrap();
      await i18n.changeLanguage(language.languageTag);
      navigate("/");
   };

   return (
      <section className={styles.login_page_wrapper}>
         <Card className={styles.card_wrapper}>
            <div className={styles.aside}>
               <img src="images/logo_new_black.svg" alt="logo" />
               <div>
                  <p className="section-title">{t("login_page.not_user_message")}</p>
                  <p>{t("login_page.inviting_message")}</p>
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
               <div className={styles.two_factory_auth}>
                  <TextInput
                     label={t("global.label.2fa_code")}
                     placeholder={t("login_page.2fa_code")}
                     value={formState.twoFACode}
                     name="twoFACode"
                     onChange={handleChange}
                  />
                  <Button onClick={onSendTwoFA}>{t("login_page.send_2fa_code")}</Button>
               </div>

               <p>
                  {(loginMutationState.isLoading ||
                     sendTwoFACodeMutationState.isLoading) &&
                     t("login_page.loading")}
               </p>

               {loginMutationState.isError && (
                  <p className="message">{t("exception.login_failed")}</p>
               )}
               {sendTwoFACodeMutationState.isError && (
                  <p className="message">{t("exception.2_fa_code_required")}</p>
               )}
               {sendTwoFACodeMutationState.isSuccess && (
                  <p>{t("exception.login_code")}</p>
               )}

               <div className={styles.footer}>
                  <Link to={"/request-reset-password"}>
                     {t("login_page.recover_password")}
                  </Link>
                  <Button onClick={(e) => onSubmit(e)}>{t("login_page.sign_in")}</Button>
               </div>
            </form>
         </Card>
      </section>
   );
};
