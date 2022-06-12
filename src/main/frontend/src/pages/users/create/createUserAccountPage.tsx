import React, { useEffect, useState } from "react";
import styles from "./createUserAccountPage.module.scss";
import { Button, Card, Checkbox, TextInput } from "components/shared";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import { useCreateAccountMutation } from "redux/service/usersManagementService";

export const CreateUserAccountPage = () => {
   const { t } = useTranslation();

   const [formData, setFormData] = useState({
      login: "",
      password: "",
      password2: "",
      name: "",
      surname: "",
      email: "",
      email2: "",
      registered: false,
      active: false,
   });

   const [emailCheck, setEmailCheck] = useState(false);
   const [passwordCheck, setPasswordCheck] = useState(false);
   const [mutation, { isError, isSuccess }] = useCreateAccountMutation();

   useEffect(() => {
      setEmailCheck(formData.email == formData.email2);
      setPasswordCheck(formData.password == formData.password2);
   }, [formData]);

   useEffect(() => {
      isSuccess && navigate("/users");
   }, [isSuccess]);

   const navigate = useNavigate();

   return (
      <div className={styles.create_account_page_wrapper}>
         <Card className={styles.card_wrapper}>
            <div>
               <TextInput
                  value={formData.login}
                  onChange={(e) => {
                     setFormData({ ...formData, login: e.target.value });
                  }}
                  label={t("global.label.login")}
                  required
                  className="text"
               />
            </div>
            <div />
            <div>
               <TextInput
                  value={formData.name}
                  onChange={(e) => {
                     setFormData({ ...formData, name: e.target.value });
                  }}
                  label={t("global.label.first_name")}
                  required
                  className="text"
               />
            </div>
            <div>
               <TextInput
                  value={formData.surname}
                  onChange={(e) => {
                     setFormData({ ...formData, surname: e.target.value });
                  }}
                  label={t("global.label.second_name")}
                  required
                  className="text"
               />
            </div>
            <div>
               <TextInput
                  value={formData.email}
                  onChange={(e) => {
                     setFormData({ ...formData, email: e.target.value });
                  }}
                  label={t("global.label.email")}
                  required
                  className="text"
                  type="email"
               />
            </div>
            <div>
               <TextInput
                  value={formData.email2}
                  onChange={(e) => {
                     setFormData({ ...formData, email2: e.target.value });
                  }}
                  label={t("global.label.repeat_email")}
                  required
                  className="text"
                  type="email"
               />
               {!emailCheck && (
                  <p className={styles.error_message}>
                     {t("message.error.equality-error-email")}
                  </p>
               )}
            </div>
            <div>
               <TextInput
                  value={formData.password}
                  onChange={(e) => {
                     setFormData({ ...formData, password: e.target.value });
                  }}
                  label={t("global.label.password")}
                  required
                  className="text"
                  type="password"
               />
            </div>
            <div>
               <TextInput
                  value={formData.password2}
                  onChange={(e) => {
                     setFormData({ ...formData, password2: e.target.value });
                  }}
                  label={t("global.label.repeat_password")}
                  required
                  className="text"
                  type="password"
               />
               {!passwordCheck && (
                  <p className="error-message">
                     {t("message.error.equality-error-password")}
                  </p>
               )}
            </div>
            <div>
               <Checkbox
                  value={formData.active}
                  onChange={(e) => {
                     setFormData({ ...formData, active: e.target.checked });
                  }}
               >
                  {t("global.label.active")}
               </Checkbox>
               <Checkbox
                  value={formData.registered}
                  onChange={(e) => {
                     setFormData({ ...formData, registered: e.target.checked });
                  }}
               >
                  {t("global.label.registered")}
               </Checkbox>
            </div>
            <div>
               <Button
                  onClick={() => {
                     if (emailCheck && passwordCheck) {
                        mutation({
                           login: formData.login,
                           password: formData.password,
                           email: formData.email,
                           name: formData.name,
                           surname: formData.surname,
                           registered: formData.registered,
                           active: formData.active,
                        });
                     }
                  }}
               >
                  {t("create_user_account_page.create_account")}
               </Button>
               {isError && (
                  <p className={styles.error_message}>
                     {t("message.error.create-account")}
                  </p>
               )}
            </div>
         </Card>
      </div>
   );
};
