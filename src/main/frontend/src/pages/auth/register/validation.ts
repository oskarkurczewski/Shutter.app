import type { TFunction } from "react-i18next";

export const validateFields = (
   formData: {
      login: string;
      email: string;
      password: string;
      confirmPassword: string;
      name: string;
      surname: string;
      userDataChecked: boolean | null;
      termsOfUseChecked: boolean | null;
   },
   t: TFunction<"translation", undefined>
) => [
   [
      // Login section
      {
         label: t("register_page.validation.login_min"),
         valid: formData.login.length == 0 ? null : formData.login.length >= 3,
      },
      {
         label: t("register_page.validation.login_max"),
         valid: formData.login.length == 0 ? null : formData.login.length <= 15,
      },
   ],
   [
      // Email section
      {
         label: t("register_page.validation.email_format"),
         valid:
            formData.email.length == 0
               ? null
               : /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/.test(formData.email),
      },
   ],
   [
      // Password section
      {
         label: t("register_page.validation.password_min"),
         valid: formData.password.length == 0 ? null : formData.password.length >= 8,
      },
      {
         label: t("register_page.validation.password_max"),
         valid: formData.password.length == 0 ? null : formData.password.length <= 64,
      },
      {
         label: t("register_page.validation.password_match"),
         valid:
            formData.password.length == 0
               ? null
               : formData.password === formData.confirmPassword,
      },
   ],
   [
      // Name section
      {
         label: t("register_page.validation.first_name_empty"),
         valid: formData.name.length == 0 ? null : formData.name.length > 0,
      },
      {
         label: t("register_page.validation.second_name_empty"),
         valid: formData.surname.length == 0 ? null : formData.surname.length > 0,
      },
   ],
   [
      {
         label: t("register_page.validation.processing_unchecked"),
         valid: formData.userDataChecked === null ? null : formData.userDataChecked,
      },
      {
         label: t("register_page.validation.tos_unchecked"),
         valid: formData.termsOfUseChecked === null ? null : formData.termsOfUseChecked,
      },
   ],
];
