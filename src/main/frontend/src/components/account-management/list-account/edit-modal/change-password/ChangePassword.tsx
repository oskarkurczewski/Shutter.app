import { Button, Card, SquareButton, TextInput } from "components/shared";
import { useStateWithValidationAndComparison } from "hooks";
import React from "react";
import { useTranslation } from "react-i18next";
import { useChangeSomeonesPasswordMutation } from "redux/service/userSettingsService";
import { passwordPattern } from "util/regex";
import styles from "./ChangePassword.module.scss";

interface Props {
   login: string;
   isRegistered: boolean;
}

export const ChangePassword: React.FC<Props> = ({ login, isRegistered }) => {
   const { t } = useTranslation();

   const [passwordMutation, passwordMutationState] = useChangeSomeonesPasswordMutation();

   const [password, setPassword, passwordValidation] =
      useStateWithValidationAndComparison<string>(
         [
            (password) => password.length >= 8,
            (password) => password.length <= 64,
            (password) => passwordPattern.test(password),
         ],
         "",
         ""
      );

   const save = () => {
      passwordValidation.valueA === null &&
         passwordValidation.valueB &&
         passwordMutation({ login: login, data: { password: password.valueA } });
   };

   return (
      <div className={styles["changePassword-wrapper"]}>
         <p className={`section-title`}>{t("edit_account_page.password.title")}</p>
         <div>
            <TextInput
               value={password.valueA}
               label={t("edit_account_page.password.password")}
               onChange={(e) => {
                  setPassword({ valueA: e.target.value });
               }}
               type="password"
               required
               validation={passwordValidation.valueA}
               validationMessages={[
                  t("edit_account_page.password.password_validation.min"),
                  t("edit_account_page.password.password_validation.max"),
                  t("edit_account_page.password.password_validation.regex"),
               ]}
               disabled={!isRegistered}
            />
            <TextInput
               value={password.valueB}
               label={t("edit_account_page.password.repeat_password")}
               onChange={(e) => {
                  setPassword({ valueB: e.target.value });
               }}
               type="password"
               required
               validation={passwordValidation.valueB}
               validationMessages={[
                  t("edit_account_page.password.password_validation.repeat"),
               ]}
               disabled={!isRegistered}
            />
         </div>
         <Button
            onClick={save}
            className={styles.save}
            disabled={!isRegistered}
            title={!isRegistered && t("edit_account_page.confirm.cant_change")}
         >
            {t("edit_account_page.confirm")}
         </Button>
         {/* TODO: change to toast */}
         {passwordMutationState.isError && (
            <p className={styles.error_message}>Nie można zapisać edycji</p>
         )}
      </div>
   );
};
