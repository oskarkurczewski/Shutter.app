import React, { useState, useEffect } from "react";
import styles from "./MainSettings.module.scss";
import { Button, TextInput, Card } from "components/shared";
import { useChangeUserDataMutation } from "redux/service/userSettingsService";
import { useGetUserInfoQuery } from "redux/service/authService";
import { useTranslation } from "react-i18next";
import { useAppDispatch, useAppSelector } from "redux/hooks";
import { nameSurnameFirstLetterPattern, nameSurnamePattern } from "util/regex";
import { useStateWithValidation } from "hooks";
import { Toast } from "types";
import { push, ToastTypes } from "redux/slices/toastSlice";

export const MainSettings = () => {
   const { t } = useTranslation();
   const dispatch = useAppDispatch();

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

   const { username } = useAppSelector((state) => state.auth);
   const [mutation, mutationState] = useChangeUserDataMutation();
   const { data } = useGetUserInfoQuery();

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
                     name: data.data.name,
                     surname: data.data.surname,
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
