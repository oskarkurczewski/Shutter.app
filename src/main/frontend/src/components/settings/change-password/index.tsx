import Button from "components/shared/Button";
import Card from "components/shared/Card";
import TextInput from "components/shared/TextInput";
import React, { useState } from "react";
import { useTranslation } from "react-i18next";
import { useChangeOwnPasswordMutation } from "redux/service/api";

const ChangePassword = () => {
   const { t } = useTranslation();

   const [oldPassword, setOldPassword] = useState("");
   const [password, setPassword] = useState("");
   const [confirmPassword, setConfirmPassword] = useState("");

   const [equalityError, setEqualityError] = useState(false);

   const [mutation, { isLoading, isError, isSuccess, error }] =
      useChangeOwnPasswordMutation();

   const onSubmit = () => {
      setEqualityError(false);
      if (password == confirmPassword) {
         return mutation({
            oldPassword,
            password,
         });
      }
      setEqualityError(true);
   };

   return (
      <Card id="change-password">
         <p className="category-title">{t("label.password")}</p>
         <p>{t("message.change.password")}</p>
         <div className="row">
            <TextInput
               label={t("label.current-password")}
               placeholder={t("label.password")}
               type="password"
               required
               value={oldPassword}
               onChange={(e) => setOldPassword(e.target.value)}
            />
         </div>
         <div className="row">
            <TextInput
               label={t("label.password")}
               placeholder={t("label.password")}
               type="password"
               required
               value={password}
               onChange={(e) => setPassword(e.target.value)}
            />
            <TextInput
               label={t("label.repeat-password")}
               placeholder={t("label.password")}
               type="password"
               required
               value={confirmPassword}
               onChange={(e) => setConfirmPassword(e.target.value)}
            />
         </div>

         {(() => {
            if (isLoading) {
               return <p>{t("message.loading.change-password")}</p>;
            }
            if (equalityError) {
               return (
                  <p className="error">{t("message.error.equality-error-password")}</p>
               );
            }
            if (isError) {
               return <p className="error">{t("message.error.change-password")}</p>;
               const err = error as any;
               if (
                  err.status === 400 &&
                  err.data.message === "exception.password.not_unique"
               ) {
                  return (
                     <p className="error">
                        Nie możesz użyć hasła, z którego korzystałeś/aś niedawno!
                     </p>
                  );
               }

               return <p className="error">Nie udało się zmienić hasła.</p>;
            }
            if (isSuccess) {
               return <p>{t("message.success.change-password")}</p>;
            }
         })()}

         <Button onClick={onSubmit}>{t("label.change")}</Button>
      </Card>
   );
};

export default ChangePassword;
