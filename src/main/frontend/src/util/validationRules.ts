import type { TFunction } from "react-i18next";
import {
   emailPattern,
   nameSurnameFirstLetterPattern,
   nameSurnamePattern,
   passwordPattern,
   loginPattern,
   loginFirstLastPattern,
} from "./regex";

export const loginRules = (t: TFunction<"translation", undefined>) => {
   return [
      {
         function: (login) => login.length >= 5,
         message: t("validator.incorrect.length.min", {
            field: t("edit_account_page.basic_info.login"),
            min: 5,
         }),
      },
      {
         function: (login) => login.length <= 15,
         message: t("validator.incorrect.length.max", {
            field: t("edit_account_page.basic_info.login"),
            max: 15,
         }),
      },
      {
         function: (login) => loginPattern.test(login),
         message: t("validator.incorrect.regx.login"),
      },
      {
         function: (login) => loginFirstLastPattern.test(login),
         message: t("validator.incorrect.regx.login_first_last"),
      },
   ];
};

export const nameRules = (t: TFunction<"translation", undefined>) => {
   return [
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
   ];
};

export const surnameRules = (t: TFunction<"translation", undefined>) => {
   return [
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
   ];
};

export const emailRules = (t: TFunction<"translation", undefined>) => {
   return [
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
   ];
};

export const passwordRules = (t: TFunction<"translation", undefined>) => {
   return [
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
   ];
};
