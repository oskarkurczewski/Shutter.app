import { Button, TextInput } from "components/shared";
import { useStateWithValidationAndComparison } from "hooks";
import React from "react";
import { useTranslation } from "react-i18next";
import { useChangeSomeonesPasswordMutation } from "redux/service/usersManagementService";
import { passwordRules } from "util/validationRules";
import styles from "./ChangePassword.module.scss";

interface Props {
   login: string;
   isRegistered: boolean;
}

export const ChangePassword: React.FC<Props> = ({ login, isRegistered }) => {
   const { t } = useTranslation();

   const [passwordMutation, passwordMutationState] = useChangeSomeonesPasswordMutation();

   const [password, setPassword, passwordValidation] =
      useStateWithValidationAndComparison<string>(passwordRules(t), ["", ""]);

   const save = () => {
      passwordValidation[0] === "" &&
         passwordValidation[1] === "" &&
         passwordMutation({ login: login, data: { password: password[0] } });
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
               disabled={!isRegistered}
            />
         </div>
         <Button
            onClick={save}
            className={styles.save}
            disabled={
               !isRegistered ||
               passwordValidation[0] !== "" ||
               passwordValidation[1] !== ""
            }
            title={!isRegistered && t("edit_account_page.password.cant_change")}
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
