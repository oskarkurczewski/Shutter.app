import React, { useCallback, useEffect, useState } from "react";
import styles from "./createUserAccountPage.module.scss";
import { Button, Card, Checkbox, TextInput } from "components/shared";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import { useCreateAccountMutation } from "redux/service/usersManagementService";
import { useStateWithValidation, useStateWithValidationAndComparison } from "hooks";
import {
   emailPattern,
   loginPattern,
   nameSurnameFirstLetterPattern,
   nameSurnamePattern,
   passwordPattern,
} from "util/regex";

export const CreateUserAccountPage = () => {
   const { t } = useTranslation();

   const [login, setLogin, loginValidationMessage] = useStateWithValidation<string>(
      [
         {
            function: (name) => name.length >= 5,
            message: t("validator.incorrect.length.min", {
               field: t("edit_account_page.basic_info.login"),
               min: 5,
            }),
         },
         {
            function: (name) => name.length <= 15,
            message: t("validator.incorrect.length.max", {
               field: t("edit_account_page.basic_info.login"),
               max: 15,
            }),
         },
         {
            function: (name) => nameSurnamePattern.test(name),
            message: t("validator.incorrect.regx.login"),
         },
         {
            function: (name) => nameSurnameFirstLetterPattern.test(name),
            message: t("validator.incorrect.regx.login_first_last"),
         },
      ],
      ""
   );

   const [name, setName, nameValidationMessage] = useStateWithValidation<string>(
      [
         {
            function: (name) => name.length <= 63,
            message: t("validator.incorrect.length.max", {
               field: t("edit_account_page.basic_info.name"),
               max: 63,
            }),
         },
         {
            function: (name) => nameSurnamePattern.test(name),
            message: t("validator.incorrect.regx.upper_lower_only", {
               field: t("edit_account_page.basic_info.name"),
            }),
         },
         {
            function: (name) => nameSurnameFirstLetterPattern.test(name),
            message: t("validator.incorrect.regx.first_uppercase", {
               field: t("edit_account_page.basic_info.name"),
            }),
         },
      ],
      ""
   );

   const [surname, setSurname, surnameValidationMessage] = useStateWithValidation<string>(
      [
         {
            function: (surname) => surname.length <= 63,
            message: t("validator.incorrect.length.max", {
               field: t("edit_account_page.basic_info.surname"),
               max: 63,
            }),
         },
         {
            function: (surname) => nameSurnamePattern.test(surname),
            message: t("validator.incorrect.regx.upper_lower_only", {
               field: t("edit_account_page.basic_info.surname"),
            }),
         },
         {
            function: (surname) => nameSurnameFirstLetterPattern.test(surname),
            message: t("validator.incorrect.regx.first_uppercase", {
               field: t("edit_account_page.basic_info.surname"),
            }),
         },
      ],
      ""
   );

   const [emails, setEmail, emailValidationMessages] =
      useStateWithValidationAndComparison<string>(
         [
            {
               function: (email) => email.length >= 1,
               message: t("validator.incorrect.length.min", {
                  field: t("edit_account_page.basic_info.email"),
                  min: 1,
               }),
            },
            {
               function: (email) => email.length <= 64,
               message: t("validator.incorrect.length.max", {
                  field: t("edit_account_page.basic_info.email"),
                  max: 1,
               }),
            },
            {
               function: (email) => emailPattern.test(email),
               message: t("validator.incorrect.regx.email"),
            },
         ],
         ["", ""]
      );

   const [password, setPassword, passwordValidation] =
      useStateWithValidationAndComparison<string>(
         [
            {
               function: (password) => password.length >= 8,
               message: t("validator.incorrect.length.min", {
                  field: t("edit_account_page.password.title"),
                  min: 8,
               }),
            },
            {
               function: (password) => password.length <= 64,
               message: t("validator.incorrect.length.max", {
                  field: t("edit_account_page.password.title"),
                  max: 8,
               }),
            },
            {
               function: (password) => passwordPattern.test(password),
               message: t("validator.incorrect.regx.password"),
            },
         ],
         ["", ""]
      );
   const [active, setActive] = useState<boolean>(false);

   const [createAccountMutation, createAccountMutationState] = useCreateAccountMutation();

   const navigate = useNavigate();

   const checkValidation = useCallback((): boolean => {
      return (
         loginValidationMessage === "" &&
         nameValidationMessage === "" &&
         surnameValidationMessage === "" &&
         emailValidationMessages.valueA === "" &&
         emailValidationMessages.valueB === "" &&
         passwordValidation.valueA === "" &&
         passwordValidation.valueB === ""
      );
   }, []);

   return (
      <div className={styles.create_account_page_wrapper}>
         <Card className={styles.card_wrapper}>
            <div>
               <TextInput
                  value={login}
                  onChange={(e) => {
                     setLogin(e.target.value);
                  }}
                  label={t("global.label.login")}
                  required
                  className="text"
                  validation={loginValidationMessage}
               />
            </div>
            <div className={styles.checkboxes}>
               <Checkbox
                  id="active"
                  value={active}
                  onChange={(e) => {
                     setActive(e.target.checked);
                  }}
               >
                  {t("global.label.active")}
               </Checkbox>
            </div>
            <div>
               <TextInput
                  value={name}
                  onChange={(e) => {
                     setName(e.target.value);
                  }}
                  label={t("global.label.first_name")}
                  required
                  className="text"
                  validation={nameValidationMessage}
               />
            </div>
            <div>
               <TextInput
                  value={surname}
                  onChange={(e) => {
                     setSurname(e.target.value);
                  }}
                  label={t("global.label.second_name")}
                  required
                  className="text"
                  validation={surnameValidationMessage}
               />
            </div>
            <div>
               <TextInput
                  value={emails.valueA}
                  onChange={(e) => {
                     setEmail({ valueA: e.target.value });
                  }}
                  label={t("global.label.email")}
                  required
                  className="text"
                  type="email"
                  validation={emailValidationMessages.valueA}
               />
            </div>
            <div>
               <TextInput
                  value={emails.valueB}
                  onChange={(e) => {
                     setEmail({ valueB: e.target.value });
                  }}
                  label={t("global.label.repeat_email")}
                  required
                  className="text"
                  type="email"
                  validation={emailValidationMessages.valueB}
               />
            </div>
            <div>
               <TextInput
                  value={password.valueA}
                  onChange={(e) => {
                     setPassword({ valueA: e.target.value });
                  }}
                  label={t("global.label.password")}
                  required
                  className="text"
                  type="password"
                  validation={passwordValidation.valueA}
               />
            </div>
            <div>
               <TextInput
                  value={password.valueB}
                  onChange={(e) => {
                     setPassword({ valueB: e.target.value });
                  }}
                  label={t("global.label.repeat_password")}
                  required
                  className="text"
                  type="password"
                  validation={passwordValidation.valueB}
               />
            </div>
            <div />
            <div>
               <Button
                  onClick={() => {
                     if (checkValidation()) {
                        createAccountMutation({
                           login: login,
                           password: password.valueA,
                           email: emails.valueA,
                           name: name,
                           surname: surname,
                           registered: true,
                           active: active,
                        }).then(() => {
                           if (createAccountMutationState.isSuccess) {
                              navigate("/users");
                              //TODO add toast
                           }
                        });
                     }
                  }}
                  disabled={!checkValidation()}
               >
                  {t("create_user_account_page.create_account")}
               </Button>

               {/* TODO: change to toast */}
               {createAccountMutationState.isError && (
                  <p className={styles.error_message}>
                     {t("message.error.create-account")}
                  </p>
               )}
            </div>
         </Card>
      </div>
   );
};
