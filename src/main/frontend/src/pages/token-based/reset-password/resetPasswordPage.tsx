import React, { useEffect, useState } from "react";
import styles from "./resetPasswordPage.module.scss";
import { Button, Card, TextInput } from "components/shared";
import { useResetPasswordMutation } from "redux/service/tokenBasedService";
import { useNavigate, useParams } from "react-router-dom";
import { useTranslation } from "react-i18next";
import { useAppDispatch } from "redux/hooks";
import { ErrorResponse, Toast } from "types";
import { parseError } from "util/errorUtil";
import { push, ToastTypes } from "redux/slices/toastSlice";

export const ResetPasswordPage = () => {
   const { t } = useTranslation();
   const dispatch = useAppDispatch();
   const navigate = useNavigate();

   const { token } = useParams();
   const [password, setPassword] = useState<string>("");
   const [resetPassword, mutationState] = useResetPasswordMutation();

   const onSubmit = async (e) => {
      e.preventDefault();
      await resetPassword({ token: token, newPassword: password });
   };

   useEffect(() => {
      if (mutationState.isSuccess) {
         const successToast: Toast = {
            type: ToastTypes.SUCCESS,
            text: t("toast.success_change_password"),
         };
         dispatch(push(successToast));
         navigate("/login", { replace: true });
      }

      if (mutationState.isError) {
         const err = mutationState.error as ErrorResponse;
         const errorToast: Toast = {
            type: ToastTypes.ERROR,
            text: t(parseError(err)),
         };
         dispatch(push(errorToast));
      }
   }, [mutationState]);

   return (
      <section className={styles.reset_password_page_wrapper}>
         <Card className={styles.card_wrapper}>
            <form>
               <p className="category-title">{t("reset_password_page.title")}</p>
               <p>{t("reset_password_page.details_message")}</p>
               <TextInput
                  label={t("reset_password_page.password-new")}
                  type="password"
                  placeholder={t("global.label.password")}
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
               />
               <div className={styles.footer}>
                  <Button loading={mutationState.isLoading} onClick={(e) => onSubmit(e)}>
                     {t("reset_password_page.confirm")}
                  </Button>
               </div>
            </form>
         </Card>
      </section>
   );
};
