import React, { useEffect, useRef } from "react";
import styles from "./registerPage.module.scss";
import { Button, Card, TextInput } from "components/shared";
import { Link, useNavigate } from "react-router-dom";
import { useRegisterMutation } from "redux/service/authService";
import { useTranslation } from "react-i18next";
import ReCAPTCHA from "react-google-recaptcha";
import { Language } from "types/Language";
import { ErrorResponse, Toast } from "types";
import { push, ToastTypes } from "redux/slices/toastSlice";
import { useAppDispatch } from "redux/hooks";
import { parseError } from "util/errorUtil";
import { useStateWithValidation, useStateWithValidationAndComparison } from "hooks";
import {
   emailRules,
   loginRules,
   nameRules,
   passwordRules,
   surnameRules,
} from "util/validationRules";

export const RegisterPage = () => {
   const { t, i18n } = useTranslation();
   const dispatch = useAppDispatch();
   const navigate = useNavigate();

   const [login, setLogin, loginValidationMessage] = useStateWithValidation<string>(
      loginRules(t),
      ""
   );

   const [name, setName, nameValidationMessage] = useStateWithValidation<string>(
      nameRules(t),
      ""
   );

   const [surname, setSurname, surnameValidationMessage] = useStateWithValidation<string>(
      surnameRules(t),
      ""
   );

   const [email, setEmail, emailValidationMessage] = useStateWithValidation<string>(
      emailRules(t),
      ""
   );

   const [password, setPassword, passwordValidation] =
      useStateWithValidationAndComparison<string>(passwordRules(t), ["", ""]);

   const recaptchaRef = useRef(null);

   const [registerMutation, registerMutationState] = useRegisterMutation();

   const onSubmit = async (e) => {
      e.preventDefault();
      const captchaToken = await recaptchaRef.current.getValue();
      registerMutation({
         login: login,
         email: email,
         password: password.valueA,
         name: name,
         surname: surname,
         reCaptchaToken: captchaToken,
         locale: i18n.language as Language,
      });
   };

   useEffect(() => {
      if (registerMutationState.isSuccess) {
         const successToast: Toast = {
            type: ToastTypes.SUCCESS,
            text: t("toast.success_register_message"),
         };
         dispatch(push(successToast));
      }
      if (registerMutationState.isError) {
         const err = registerMutationState.error as ErrorResponse;
         const errorToast: Toast = {
            type: ToastTypes.ERROR,
            text: t(parseError(err)),
         };
         dispatch(push(errorToast));
      }
   }, [registerMutationState]);

   return (
      <section className={styles.register_page_wrapper}>
         <div className={styles.form_wrapper}>
            <Card className={styles.register_card}>
               <div className={styles.aside}>
                  <img src="images/logo_new_black.svg" alt="logo" />
                  <div>
                     <div>
                        <p className="section-title">
                           {t("register_page.not_user_message_up")}
                        </p>
                        <p className="section-title">
                           {t("register_page.not_user_message_down")}
                        </p>
                     </div>
                     <div>
                        <p>{t("register_page.inviting_message_up")}</p>
                        <p>{t("register_page.inviting_message_down")}</p>
                     </div>
                  </div>
                  <Button
                     className={styles.button_wrapper}
                     onClick={() => {
                        navigate("/login");
                     }}
                  >
                     {t("register_page.sign_in")}
                  </Button>
               </div>

               <form onSubmit={onSubmit}>
                  <p className="section-title">{t("register_page.form_title")}</p>
                  <div className={styles.inputs_wrapper}>
                     <div className={styles.column}>
                        <TextInput
                           className={styles.text_input_wrapper}
                           label={t("global.label.login")}
                           placeholder={t("global.label.login")}
                           required
                           name="login"
                           value={login}
                           onChange={(e) => {
                              setLogin(e.target.value);
                           }}
                           validation={loginValidationMessage}
                        />
                        <TextInput
                           className={styles.text_input_wrapper}
                           label={t("global.label.email")}
                           placeholder={t("global.label.email_short")}
                           required
                           name="email"
                           value={email}
                           onChange={(e) => {
                              setEmail(e.target.value);
                           }}
                           validation={emailValidationMessage}
                        />
                        <TextInput
                           className={styles.text_input_wrapper}
                           label={t("global.label.password")}
                           placeholder={t("global.label.password")}
                           required
                           name="password"
                           type="password"
                           value={password.valueA}
                           onChange={(e) => {
                              setPassword({ valueA: e.target.value });
                           }}
                           validation={passwordValidation.valueA}
                        />
                        <TextInput
                           className={styles.text_input_wrapper}
                           label={t("global.label.repeat_password")}
                           placeholder={t("global.label.password")}
                           required
                           name="confirmPassword"
                           type="password"
                           value={password.valueB}
                           onChange={(e) => {
                              setPassword({ valueB: e.target.value });
                           }}
                           validation={passwordValidation.valueB}
                        />
                     </div>
                     <div className={styles.column}>
                        <TextInput
                           className={styles.text_input_wrapper}
                           label={t("global.label.first_name")}
                           placeholder={t("global.label.first_name")}
                           required
                           name="name"
                           value={name}
                           onChange={(e) => {
                              setName(e.target.value);
                           }}
                           validation={nameValidationMessage}
                        />
                        <TextInput
                           className={styles.text_input_wrapper}
                           label={t("global.label.second_name")}
                           placeholder={t("global.label.second_name")}
                           required
                           name="surname"
                           value={surname}
                           onChange={(e) => {
                              setSurname(e.target.value);
                           }}
                           validation={surnameValidationMessage}
                        />
                     </div>
                  </div>
                  <ReCAPTCHA
                     ref={recaptchaRef}
                     sitekey="6LcOjh4gAAAAAJRdv-oKWqqj8565Bz6Y3QlmJv5L"
                  />
                  <div className={styles.footer}>
                     <Link to="/login">{t("register_page.sign_in")}</Link>
                     <Button loading={registerMutationState.isLoading} onClick={onSubmit}>
                        {t("register_page.sign_up")}
                     </Button>
                  </div>
               </form>
            </Card>
         </div>
      </section>
   );
};
