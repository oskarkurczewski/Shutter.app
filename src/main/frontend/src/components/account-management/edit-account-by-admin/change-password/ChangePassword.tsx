import { Button, Card, TextInput } from "components/shared";
import { useStateWithComparison } from "hooks/useStateWithComparison";
import { useStateWithValidation } from "hooks/useStateWithValidation";
import React, { useState } from "react";
import { useTranslation } from "react-i18next";
import { useChangeSomeonesPasswordMutation } from "redux/service/userSettingsService";
import { passwordPattern } from "util/regex";
import styles from "./ChangePassword.module.scss";

interface Props {
   login: string;
}

export const ChangePassword: React.FC<Props> = ({ login }) => {
   const { t } = useTranslation();

   const [passwordMutation, passwordMutationState] = useChangeSomeonesPasswordMutation();
   const [passwordCheck, setPasswordCheck] = useState(false);

   const save = () => {
      passwordCheck && passwordMutation({ login: login, data: { password } });
   };

   const [password, setPassword, passwordValidation] = useStateWithValidation(
      [
         (password) => password.length >= 8,
         (password) => password.length <= 64,
         // TODO: fix password regex
         (password) => passwordPattern.test(password),
      ],
      ""
   );

   const [password2, setPassword2, password2Validation] = useStateWithComparison(
      "",
      password
   );

   return (
      <Card className={styles["changePassword-wrapper"]}>
         <p className={`category-title ${styles.category_title}`}>
            {t("edit_account_page.password.title")}
         </p>

         <div>
            <TextInput
               value={password}
               label={t("edit_account_page.password.password")}
               onChange={(e) => {
                  setPassword(e.target.value);
               }}
               type="password"
               required
               validation={passwordValidation}
               validationMessages={[
                  t("edit_account_page.password.password_validation.min"),
                  t("edit_account_page.password.password_validation.max"),
                  t("edit_account_page.password.password_validation.regex"),
               ]}
            />
            <TextInput
               value={password2}
               label={t("edit_account_page.password.repeat_password")}
               onChange={(e) => {
                  setPassword2(e.target.value);
               }}
               type="password"
               required
               validation={password2Validation}
               validationMessages={[
                  t("edit_account_page.password.password_validation.repeat"),
               ]}
            />
         </div>
         <Button onClick={save} className={styles.save}>
            {t("edit_account_page.confirm")}
         </Button>
      </Card>
   );
};
