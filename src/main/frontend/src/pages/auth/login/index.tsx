import React, { ChangeEvent, useState } from "react";
import styles from "./style.module.scss";
import Card from "components/shared/card";
import TextInput from "components/shared/text-input";
import Button from "components/shared/button";
import { Link, useNavigate } from "react-router-dom";
import { useAppDispatch } from "redux/hooks";
import { getLoginPayload } from "util/loginUtil";
import { login } from "redux/slices/authSlice";
import { useLoginMutation, useSendTwoFACodeMutation } from "redux/service/api";
import { LoginRequest } from "redux/types/api/authTypes";
import { useTranslation } from "react-i18next";

const LoginPage: React.FC = () => {
   const { t } = useTranslation();

   const navigate = useNavigate();
   const dispatch = useAppDispatch();

   const [formState, setFormState] = useState<LoginRequest>({
      login: "",
      password: "",
      twoFACode: "000000",
   });

   const [loginMutation, loginMutationState] = useLoginMutation();
   const [sendTwoFACodeMutation, sendTwoFACodeMutationState] = useSendTwoFACodeMutation();

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
      navigate("/");
   };

   return (
      <section className={styles.login_page_wrapper}>
         <Card className={styles.card_wrapper}>
            <div className={styles.aside}>
               <img src="images/logo_new_black.svg" alt="logo" />
               <div>
                  <p className="section-title">{t("message.info.login-title")}</p>
                  <p>{t("message.info.login-register")}</p>
               </div>
               <Button
                  className={styles.button_wrapper}
                  onClick={() => {
                     navigate("/register");
                  }}
               >
                  {t("label.register")}
               </Button>
            </div>
            <form onSubmit={onSubmit}>
               <p className="section-title">{t("label.login-title")}</p>
               <TextInput
                  label={t("label.login-label")}
                  placeholder={t("label.login-label")}
                  type="text"
                  name="login"
                  value={formState.login}
                  onChange={handleChange}
               />
               <TextInput
                  label={t("label.password")}
                  type="password"
                  placeholder={t("label.password")}
                  value={formState.password}
                  name="password"
                  onChange={handleChange}
               />
               <div className={styles.two_factory_auth}>
                  <TextInput
                     label={t("label.code")}
                     placeholder={t("label.code-short")}
                     value={formState.twoFACode}
                     name="twoFACode"
                     onChange={handleChange}
                  />
                  <Button onClick={onSendTwoFA}>
                     {t("message.info.login-send-code")}
                  </Button>
               </div>

               <p>
                  {(loginMutationState.isLoading ||
                     sendTwoFACodeMutationState.isLoading) &&
                     t("message.loading.login")}
               </p>

               {loginMutationState.isError && (
                  <p className="message">{t("message.error.login-credentials")}</p>
               )}
               {sendTwoFACodeMutationState.isError && (
                  <p className="message">{t("label.message.error.login-code")}</p>
               )}
               {sendTwoFACodeMutationState.isSuccess && (
                  <p>{t("message.success.login-code")}</p>
               )}

               <div className={styles.footer}>
                  <Link to={"/request-reset-password"}>
                     {t("label.forgot-password")}?
                  </Link>
                  <Button onClick={(e) => onSubmit(e)}>{t("label.login")}</Button>
               </div>
            </form>
         </Card>
      </section>
   );
};

export default LoginPage;
