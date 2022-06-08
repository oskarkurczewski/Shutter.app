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
import { useRegisterMutation } from "redux/service/api";
import { useTranslation } from "react-i18next";
import ReCAPTCHA from "react-google-recaptcha";

export const RegisterPage = () => {
   const { t } = useTranslation();

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
      validateFields({ ...formState, ...checkboxState })
   );

   const [registerMutation, { isLoading, isSuccess, isError }] = useRegisterMutation();

   useEffect(() => {
      setValidation(validateFields({ ...formState, ...checkboxState }));
   }, [formState, checkboxState]);

   const handleChange = ({
      target: { name, value },
   }: React.ChangeEvent<HTMLInputElement>) =>
      setFormState((prev) => ({ ...prev, [name]: value }));

   const onSubmit = async (e) => {
      e.preventDefault();
      const captchaToken = await recaptchaRef.current.getValue();
      registerMutation({ ...formState, reCaptchaToken: captchaToken });
   };

   return (
      <section className={styles.register_page_wrapper}>
         <div className={styles.form_wrapper}>
            <ValidationBox data={validation} className={styles.validation_card} />

            <Card className={styles.register_card}>
               <Form onSubmit={onSubmit} isLoading={isLoading}>
                  <p className="section-title">{t("label.register-title")}</p>
                  <div className={styles.inputs_wrapper}>
                     <div className={styles.column}>
                        <TextInput
                           className={styles.text_input_wrapper}
                           label={t("label.login-label")}
                           placeholder={t("label.login-label")}
                           required
                           name="login"
                           value={formState.login}
                           onChange={handleChange}
                        />
                        <TextInput
                           className={styles.text_input_wrapper}
                           label={t("label.email-short")}
                           placeholder={t("label.email-short")}
                           required
                           name="email"
                           value={formState.email}
                           onChange={handleChange}
                        />
                        <TextInput
                           className={styles.text_input_wrapper}
                           label={t("label.password")}
                           placeholder={t("label.password")}
                           required
                           name="password"
                           type="password"
                           value={formState.password}
                           onChange={handleChange}
                        />
                        <TextInput
                           className={styles.text_input_wrapper}
                           label={t("label.repeat-password")}
                           placeholder={t("label.password")}
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
                           label={t("label.first-name")}
                           placeholder={t("label.first-name")}
                           required
                           name="name"
                           value={formState.name}
                           onChange={handleChange}
                        />
                        <TextInput
                           className={styles.text_input_wrapper}
                           label={t("label.second-name")}
                           placeholder={t("label.second-name")}
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
                        required
                        value={checkboxState.userDataChecked}
                        onChange={(e) => {
                           setCheckboxState({
                              ...checkboxState,
                              userDataChecked: e.target.checked,
                           });
                        }}
                     >
                        {t("message.info.processing")}
                     </Checkbox>
                     <Checkbox
                        required
                        value={checkboxState.termsOfUseChecked}
                        onChange={(e) => {
                           setCheckboxState({
                              ...checkboxState,
                              termsOfUseChecked: e.target.checked,
                           });
                        }}
                     >
                        {t("message.info.tos")}
                     </Checkbox>
                  </div>

                  {isSuccess && <p>{t("message.success.register")}</p>}
                  {isError && <p>{t("message.error.register")}</p>}

                  <div className={styles.footer}>
                     <Link to="/login">
                        {t("message.info.got-account")} {t("label.login")}
                     </Link>
                     <Button onClick={onSubmit}>{t("label.register")}</Button>
                  </div>
               </Form>
            </Card>
         </div>
      </section>
   );
};
