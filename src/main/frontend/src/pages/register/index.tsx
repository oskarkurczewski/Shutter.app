import React, { useEffect, useRef, useState } from "react";
import "./style.scss";
import Button from "components/shared/Button";
import Card from "components/shared/Card";
import Checkbox from "components/shared/Checkbox";
import TextInput from "components/shared/TextInput";
import ValidationBox from "components/shared/ValidationBox";
import { Link } from "react-router-dom";
import { validateFields } from "./validation";
import { useRegisterMutation } from "redux/service/api";
import Form from "components/shared/Form";
import { useTranslation } from "react-i18next";
import ReCAPTCHA from "react-google-recaptcha";

const RegisterPage = () => {
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
      <section className="register-page-wrapper">
         <div className="form-wrapper">
            <ValidationBox data={validation} />
            <Card className="register-form">
               <Form onSubmit={onSubmit} isLoading={isLoading}>
                  <p className="section-title">{t("label.register-title")}</p>
                  <div className="inputs-wrapper">
                     <div className="column">
                        <TextInput
                           label={t("label.login-label")}
                           placeholder={t("label.login-label")}
                           required
                           name="login"
                           value={formState.login}
                           onChange={handleChange}
                        />
                        <TextInput
                           label={t("label.email-short")}
                           placeholder={t("label.email-short")}
                           required
                           name="email"
                           value={formState.email}
                           onChange={handleChange}
                        />
                        <TextInput
                           label={t("label.password")}
                           placeholder={t("label.password")}
                           required
                           name="password"
                           type="password"
                           value={formState.password}
                           onChange={handleChange}
                        />
                        <TextInput
                           label={t("label.repeat-password")}
                           placeholder={t("label.password")}
                           required
                           name="confirmPassword"
                           type="password"
                           value={formState.confirmPassword}
                           onChange={handleChange}
                        />
                     </div>
                     <div className="column">
                        <TextInput
                           label={t("label.first-name")}
                           placeholder={t("label.first-name")}
                           required
                           name="name"
                           value={formState.name}
                           onChange={handleChange}
                        />
                        <TextInput
                           label={t("label.second-name")}
                           placeholder={t("label.second-name")}
                           required
                           name="surname"
                           value={formState.surname}
                           onChange={handleChange}
                        />
                     </div>
                  </div>
                  <div className="checkboxes-wrapper">
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
                     <ReCAPTCHA
                        ref={recaptchaRef}
                        sitekey="6LcOjh4gAAAAAJRdv-oKWqqj8565Bz6Y3QlmJv5L"
                     />
                  </div>

                  {isSuccess && <p>{t("message.success.register")}</p>}
                  {isError && <p>{t("message.error.register")}</p>}

                  <div className="footer">
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

export default RegisterPage;
