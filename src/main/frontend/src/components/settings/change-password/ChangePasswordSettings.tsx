import React, { useEffect, useState } from "react";
import styles from "./ChangePasswordSettings.module.scss";
import { Button, Card, TextInput } from "components/shared";
import { useTranslation } from "react-i18next";
import { useChangePasswordMutation } from "redux/service/userSettingsService";
import { useStateWithValidationAndComparison } from "hooks";
import { passwordPattern } from "util/regex";
import { Toast } from "types";
import { push, ToastTypes } from "redux/slices/toastSlice";
import { useAppDispatch } from "redux/hooks";

export const ChangePasswordSettings = () => {
   const { t } = useTranslation();
   const dispatch = useAppDispatch();

   const [oldPassword, setOldPassword] = useState("");
   const [newPassword, setNewPassword, newPasswordValidation] =
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

   const [mutation, mutationState] = useChangePasswordMutation();

   const onSubmit = () => {
      if (newPasswordValidation.valueA === "" && newPasswordValidation.valueB === "") {
         mutation({
            oldPassword: oldPassword,
            password: newPassword.valueA,
         });
      }
   };

   useEffect(() => {
      if (mutationState.isSuccess) {
         const successToast: Toast = {
            type: ToastTypes.SUCCESS,
            text: t("toast.success_update"),
         };
         dispatch(push(successToast));
      }
      if (mutationState.isError) {
         const errorToast: Toast = {
            type: ToastTypes.ERROR,
            text: t("toast.error_update"),
         };
         dispatch(push(errorToast));
      }
   }, [mutationState]);

   return (
      <Card id="change-password" className={styles.card_wrapper}>
         <p className={`category-title ${styles.category_title}`}>
            {t("settings_page.password_settings.title")}
         </p>
         <p>{t("settings_page.password_settings.details_message")}</p>
         <div className={styles.row}>
            <TextInput
               label={t("global.label.current_password")}
               placeholder={t("global.label.current_password")}
               type="password"
               required
               value={oldPassword}
               onChange={(e) => setOldPassword(e.target.value)}
            />
         </div>
         <div className={styles.row}>
            <TextInput
               label={t("global.label.password")}
               placeholder={t("global.label.password")}
               type="password"
               required
               value={newPassword.valueA}
               onChange={(e) => setNewPassword({ valueA: e.target.value })}
               validation={newPasswordValidation.valueA}
            />
            <TextInput
               label={t("global.label.repeat_password")}
               placeholder={t("global.label.repeat_password")}
               type="password"
               required
               value={newPassword.valueB}
               onChange={(e) => setNewPassword({ valueB: e.target.value })}
               validation={newPasswordValidation.valueB}
            />
         </div>
         <Button loading={mutationState.isLoading} onClick={onSubmit}>
            {t("settings_page.password_settings.confirm")}
         </Button>
      </Card>
   );
};
