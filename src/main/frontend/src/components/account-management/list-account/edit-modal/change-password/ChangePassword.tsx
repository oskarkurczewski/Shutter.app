import { Button, TextInput } from "components/shared";
import { useStateWithValidationAndComparison } from "hooks";
import React, { useEffect } from "react";
import { useTranslation } from "react-i18next";
import { useAppDispatch } from "redux/hooks";
import { useChangeSomeonesPasswordMutation } from "redux/service/usersManagementService";
import { push, ToastTypes } from "redux/slices/toastSlice";
import { ErrorResponse, Toast } from "types";
import { parseError } from "util/errorUtil";
import { passwordPattern } from "util/regex";
import { passwordRules } from "util/validationRules";
import styles from "./ChangePassword.module.scss";

interface Props {
   login: string;
   isRegistered: boolean;
   refetch: () => void;
}

export const ChangePassword: React.FC<Props> = ({ login, isRegistered, refetch }) => {
   const { t } = useTranslation();
   const dispatch = useAppDispatch();

   const [passwordMutation, passwordMutationState] = useChangeSomeonesPasswordMutation();

   const [password, setPassword, passwordValidation] =
      useStateWithValidationAndComparison<string>(passwordRules(t), ["", ""]);

   const save = () => {
      passwordValidation.valueA === "" &&
         passwordValidation.valueB === "" &&
         passwordMutation({ login: login, data: { password: password.valueA } });
   };

   useEffect(() => {
      if (passwordMutationState.isSuccess) {
         const successToast: Toast = {
            type: ToastTypes.SUCCESS,
            text: t("toast.success_edit_account_info"),
         };
         dispatch(push(successToast));
         refetch();
      }

      if (passwordMutationState.isError) {
         const err = passwordMutationState.error as ErrorResponse;
         const errorToast: Toast = {
            type: ToastTypes.ERROR,
            text: t(parseError(err)),
         };
         dispatch(push(errorToast));
      }
   }, [passwordMutationState]);

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
            loading={passwordMutationState.isLoading}
            className={styles.save}
            disabled={
               !isRegistered ||
               passwordValidation.valueA !== "" ||
               passwordValidation.valueB !== ""
            }
            title={!isRegistered && t("edit_account_page.password.cant_change")}
         >
            {t("edit_account_page.confirm")}
         </Button>
      </div>
   );
};
