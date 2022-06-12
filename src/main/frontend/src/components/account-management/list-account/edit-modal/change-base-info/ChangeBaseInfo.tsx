import { Button, Card, Checkbox, SquareButton, TextInput } from "components/shared";
import { useStateWithValidation, useStateWithValidationAndComparison } from "hooks";
import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { FaCheck } from "react-icons/fa";
import { useChangeAccountInfoMutation } from "redux/service/usersManagementService";
import { advancedUserInfoResponse } from "redux/types/api";
import { EtagData } from "redux/types/api/dataTypes";
import {
   emailPattern,
   nameSurnameFirstLetterPattern,
   nameSurnamePattern,
} from "util/regex";
import styles from "./ChangeBaseInfo.module.scss";

interface Props {
   userInfoData: EtagData<advancedUserInfoResponse>;
}

export const ChangeBaseInfo: React.FC<Props> = ({ userInfoData }) => {
   const { t } = useTranslation();

   const [login, setLogin] = useState<string>("");
   const [active, setActive] = useState<boolean>(false);
   const [registered, setRegistered] = useState<boolean>(false);

   const [name, setName, nameIsValid] = useStateWithValidation<string>(
      [
         (name) => name.length <= 63,
         (name) => nameSurnamePattern.test(name),
         (name) => nameSurnameFirstLetterPattern.test(name),
      ],
      ""
   );
   const [surname, setSurname, surnameIsValid] = useStateWithValidation<string>(
      [
         (surname) => surname.length <= 63,
         (surname) => nameSurnamePattern.test(surname),
         (surname) => nameSurnameFirstLetterPattern.test(surname),
      ],
      ""
   );

   const [email, setEmail, emailIsValid] = useStateWithValidationAndComparison<string>(
      [
         (email) => email.length >= 1,
         (email) => email.length <= 64,
         (email) => emailPattern.test(email),
      ],
      "",
      ""
   );

   const [canSubmit, setCanSubmit] = useState(
      nameIsValid && surnameIsValid && emailIsValid.valueA === null && emailIsValid.valueB
   );

   useEffect(() => {
      setCanSubmit(
         nameIsValid === null &&
            surnameIsValid === null &&
            emailIsValid.valueA === null &&
            emailIsValid.valueB
            ? true
            : false
      );
   }, [nameIsValid, surnameIsValid, emailIsValid]);

   useEffect(() => {
      setLogin(userInfoData?.data.login || "");
      setActive(userInfoData?.data.active || false);
      setRegistered(userInfoData?.data.registered || false);
      setName(userInfoData?.data.name || "");
      setSurname(userInfoData?.data.surname || "");
      setEmail({
         ...email,
         valueA: userInfoData?.data.email || "",
         valueB: userInfoData?.data.email || "",
      });
   }, [userInfoData]);

   const [infoMutation, infoMutationState] = useChangeAccountInfoMutation();

   useEffect(() => {
      // TODO: add toast
   }, [infoMutationState.isSuccess]);

   return (
      <div className={styles.base_info_wrapper}>
         <p className={`section-title`}>{t("edit_account_page.basic_info.title")}</p>
         <div className={styles.content}>
            <div>
               <TextInput
                  value={login}
                  onChange={(e) => {
                     setLogin(e.target.value);
                  }}
                  label={t("edit_account_page.basic_info.login")}
                  className="text"
                  disabled
               />
            </div>
            <div className={styles.active_registered}>
               <Checkbox
                  id="registered"
                  value={registered}
                  onChange={() => {
                     null;
                  }}
                  disabled
               >
                  {t("edit_account_page.basic_info.registered")}
               </Checkbox>
               <Checkbox
                  id="active"
                  value={active}
                  onChange={(e) => {
                     setActive(e.target.checked);
                  }}
               >
                  {t("edit_account_page.basic_info.active")}
               </Checkbox>
            </div>
            <div>
               <TextInput
                  value={name}
                  onChange={(e) => {
                     setName(e.target.value);
                  }}
                  label={t("edit_account_page.basic_info.name")}
                  required
                  className="text"
                  validation={nameIsValid}
                  validationMessages={[
                     t("edit_account_page.basic_info.name_validation.max"),
                     t("edit_account_page.basic_info.name_validation.regex"),
                     t("edit_account_page.basic_info.name_validation.first_letter"),
                  ]}
               />
            </div>
            <div>
               <TextInput
                  value={surname}
                  onChange={(e) => {
                     setSurname(e.target.value);
                  }}
                  label={t("edit_account_page.basic_info.surname")}
                  required
                  className="text"
                  validation={surnameIsValid}
                  validationMessages={[
                     t("edit_account_page.basic_info.surname_validation.max"),
                     t("edit_account_page.basic_info.surname_validation.regex"),
                     t("edit_account_page.basic_info.surname_validation.first_letter"),
                  ]}
               />
            </div>
            <div className={styles.email}>
               <div>
                  <TextInput
                     value={email.valueA}
                     onChange={(e) => {
                        setEmail({ ...email, valueA: e.target.value });
                     }}
                     label={t("edit_account_page.basic_info.email")}
                     required
                     className="text"
                     type="email"
                     validation={emailIsValid.valueA}
                     validationMessages={[
                        t("edit_account_page.basic_info.email_validation.min"),
                        t("edit_account_page.basic_info.email_validation.max"),
                        t("edit_account_page.basic_info.email_validation.format"),
                     ]}
                  />
               </div>
               <div>
                  <TextInput
                     value={email.valueB}
                     onChange={(e) => {
                        setEmail({ ...email, valueB: e.target.value });
                     }}
                     label={t("edit_account_page.basic_info.repeat_email")}
                     required
                     className="text"
                     type="email"
                     validation={emailIsValid.valueB}
                     validationMessages={[
                        t("edit_account_page.basic_info.email_validation.repeat"),
                     ]}
                  />
               </div>
            </div>
         </div>

         <div className={styles.save}>
            <Button
               onClick={() => {
                  canSubmit &&
                     infoMutation({
                        body: {
                           login: login,
                           email: email.valueA,
                           name: name,
                           surname: surname,
                           active: active,
                        },
                        etag: userInfoData.etag,
                     });
               }}
               disabled={!canSubmit}
               className={styles.btn}
            >
               {t("edit_account_page.confirm")}
            </Button>

            {/* TODO: change to toast */}
            {infoMutationState.isError && (
               <p className={styles.error_message}>Nie można zapisać edycji</p>
            )}
         </div>
      </div>
   );
};
