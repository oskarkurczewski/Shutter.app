import React, { useEffect, useRef, useState } from "react";
import styles from "./registerPage.module.scss";
import {
   Button,
   Card,
   Checkbox,
   TextInput,
   ValidationBox,
   Form,
} from "components/shared";
import { Link } from "react-router-dom";
import { validateFields } from "./validation";
import { useRegisterMutation } from "redux/service/authService";
import { useTranslation } from "react-i18next";
import ReCAPTCHA from "react-google-recaptcha";
import { Language } from "types/Language";

export const RegisterPage = () => {
   const { t, i18n } = useTranslation();

   const recaptchaRef = useRef(null);
   const [formState, setFormState] = useState({
      login: "",
      email: "",
      password: "",
      confirmPassword: "",
      name: "",
      surname: "",
   });
   const [checkboxState, setCheckboxState] = useState({
      userDataChecked: null,
      termsOfUseChecked: null,
   });

   const [validation, setValidation] = useState(
      validateFields({ ...formState, ...checkboxState }, t)
   );

   const [registerMutation, { isLoading, isSuccess, isError }] = useRegisterMutation();

   useEffect(() => {
      setValidation(validateFields({ ...formState, ...checkboxState }, t));
   }, [formState, checkboxState]);

   const handleChange = ({
      target: { name, value },
   }: React.ChangeEvent<HTMLInputElement>) =>
      setFormState((prev) => ({ ...prev, [name]: value }));

   const onSubmit = async (e) => {
      e.preventDefault();
      const captchaToken = await recaptchaRef.current.getValue();
      registerMutation({
         ...formState,
         reCaptchaToken: captchaToken,
         locale: i18n.language as Language,
      });
   };

   return (
      <section className={styles.register_page_wrapper}>
         <div className={styles.form_wrapper}>
            <ValidationBox data={validation} className={styles.validation_card} />

            <Card className={styles.register_card}>
               <Form onSubmit={onSubmit} isLoading={isLoading}>
                  <p className="section-title">{t("register_page.form_title")}</p>
                  <div className={styles.inputs_wrapper}>
                     <div className={styles.column}>
                        <TextInput
                           className={styles.text_input_wrapper}
                           label={t("global.label.login")}
                           placeholder={t("global.label.login")}
                           required
                           name="login"
                           value={formState.login}
                           onChange={handleChange}
                        />
                        <TextInput
                           className={styles.text_input_wrapper}
                           label={t("global.label.email")}
                           placeholder={t("global.label.email_short")}
                           required
                           name="email"
                           value={formState.email}
                           onChange={handleChange}
                        />
                        <TextInput
                           className={styles.text_input_wrapper}
                           label={t("global.label.password")}
                           placeholder={t("global.label.password")}
                           required
                           name="password"
                           type="password"
                           value={formState.password}
                           onChange={handleChange}
                        />
                        <TextInput
                           className={styles.text_input_wrapper}
                           label={t("global.label.repeat_password")}
                           placeholder={t("global.label.password")}
                           required
                           name="confirmPassword"
                           type="password"
                           value={formState.confirmPassword}
                           onChange={handleChange}
                        />
                     </div>
                     <div className={styles.column}>
                        <TextInput
                           className={styles.text_input_wrapper}
                           label={t("global.label.first_name")}
                           placeholder={t("global.label.first_name")}
                           required
                           name="name"
                           value={formState.name}
                           onChange={handleChange}
                        />
                        <TextInput
                           className={styles.text_input_wrapper}
                           label={t("global.label.second_name")}
                           placeholder={t("global.label.second_name")}
                           required
                           name="surname"
                           value={formState.surname}
                           onChange={handleChange}
                        />
                     </div>
                  </div>

                  <ReCAPTCHA
                     ref={recaptchaRef}
                     sitekey="6LcOjh4gAAAAAJRdv-oKWqqj8565Bz6Y3QlmJv5L"
                  />

                  <div className={styles.checkboxes_wrapper}>
                     <Checkbox
                        id="processing-data"
                        required
                        value={checkboxState.userDataChecked}
                        onChange={(e) => {
                           setCheckboxState({
                              ...checkboxState,
                              userDataChecked: e.target.checked,
                           });
                        }}
                     >
                        {t("register_page.processing_message")}
                     </Checkbox>
                     <Checkbox
                        id="terms-of-use"
                        required
                        value={checkboxState.termsOfUseChecked}
                        onChange={(e) => {
                           setCheckboxState({
                              ...checkboxState,
                              termsOfUseChecked: e.target.checked,
                           });
                        }}
                     >
                        {t("register_page.tos_message")}
                     </Checkbox>
                  </div>

                  {isSuccess && <p>{t("message.success.register")}</p>}
                  {isError && <p>{t("message.error.register")}</p>}

                  <div className={styles.footer}>
                     <Link to="/login">{t("register_page.sign_in")}</Link>
                     <Button onClick={onSubmit}>{t("register_page.sign_up")}</Button>
                  </div>
               </Form>
            </Card>
         </div>
      </section>
   );
};
