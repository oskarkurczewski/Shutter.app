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
   refetch: () => void;
}

export const ChangeBaseInfo: React.FC<Props> = ({ userInfoData, refetch }) => {
   const { t } = useTranslation();

   const [login, setLogin] = useState<string>("");
   const [active, setActive] = useState<boolean>(false);
   const [registered, setRegistered] = useState<boolean>(false);

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

   const [canSubmit, setCanSubmit] = useState(
      nameValidationMessage &&
         surnameValidationMessage &&
         emailValidationMessages[0] === "" &&
         emailValidationMessages[1] === ""
   );

   useEffect(() => {
      setCanSubmit(
         nameValidationMessage &&
            surnameValidationMessage &&
            emailValidationMessages[0] === "" &&
            emailValidationMessages[1] === ""
      );
   }, [nameValidationMessage, surnameValidationMessage, emailValidationMessages]);

   useEffect(() => {
      setLogin(userInfoData?.data.login || "");
      setActive(userInfoData?.data.active || false);
      setRegistered(userInfoData?.data.registered || false);
      setName(userInfoData?.data.name || "");
      setSurname(userInfoData?.data.surname || "");
      setEmail({
         valueA: userInfoData?.data.email || "",
         valueB: userInfoData?.data.email || "",
      });
   }, [userInfoData]);

   const [infoMutation, infoMutationState] = useChangeAccountInfoMutation();

   useEffect(() => {
      // TODO: add toast
   }, [infoMutationState]);

   const submit = () => {
      canSubmit &&
         infoMutation({
            body: {
               login: login,
               email: emails[0],
               name: name,
               surname: surname,
               active: active,
            },
            etag: userInfoData.etag,
         });
   };

   useEffect(() => {
      infoMutationState.isSuccess && refetch();
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
               <Checkbox id="registered" value={registered} onChange={null} disabled>
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
                  validation={nameValidationMessage}
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
                  validation={surnameValidationMessage}
               />
            </div>
            <div className={styles.email}>
               <div>
                  <TextInput
                     value={emails.valueA}
                     onChange={(e) => {
                        setEmail({ valueA: e.target.value });
                     }}
                     label={t("edit_account_page.basic_info.email")}
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
                     label={t("edit_account_page.basic_info.repeat_email")}
                     required
                     className="text"
                     type="email"
                     validation={emailValidationMessages.valueB}
                  />
               </div>
            </div>
         </div>

         <div className={styles.save}>
            <Button onClick={submit} disabled={!canSubmit} className={styles.btn}>
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
