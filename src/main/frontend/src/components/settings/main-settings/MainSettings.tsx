import React, { useEffect } from "react";
import styles from "./MainSettings.module.scss";
import { Button, TextInput, Card } from "components/shared";
import { useChangeUserDataMutation } from "redux/service/userSettingsService";
import { useGetOwnUserInfoQuery } from "redux/service/authService";
import { useTranslation } from "react-i18next";
import { useAppDispatch, useAppSelector } from "redux/hooks";
import { useStateWithValidation } from "hooks";
import { Toast } from "types";
import { push, ToastTypes } from "redux/slices/toastSlice";
import { nameRules, surnameRules } from "util/validationRules";

export const MainSettings = () => {
   const { t } = useTranslation();
   const dispatch = useAppDispatch();

   const [name, setName, nameValidationMessage] = useStateWithValidation<string>(
      nameRules(t),
      ""
   );

   const [surname, setSurname, surnameValidationMessage] = useStateWithValidation<string>(
      surnameRules(t),
      ""
   );

   const { username } = useAppSelector((state) => state.auth);
   const [mutation, mutationState] = useChangeUserDataMutation();
   const { data } = useGetOwnUserInfoQuery();

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
      <Card id="main-settings" className={styles.card_wrapper}>
         <p className={`category-title ${styles.category_title}`}>
            {t("settings_page.main_settings.title")}
         </p>
         <p>{t("settings_page.main_settings.details_message")}</p>
         <div className={styles.row}>
            <TextInput
               label={t("global.label.first_name")}
               placeholder={t("global.label.first_name")}
               required
               value={name}
               onChange={(e) => setName(e.target.value)}
               validation={nameValidationMessage}
            />
            <TextInput
               label={t("global.label.second_name")}
               placeholder={t("global.label.second_name")}
               required
               value={surname}
               onChange={(e) => setSurname(e.target.value)}
               validation={surnameValidationMessage}
            />
         </div>

         <Button
            loading={mutationState.isLoading}
            onClick={() => {
               mutation({
                  data: {
                     name: name,
                     surname: surname,
                     login: username,
                  },
                  etag: data.etag,
               });
            }}
         >
            {t("settings_page.main_settings.confirm")}
         </Button>
      </Card>
   );
};
