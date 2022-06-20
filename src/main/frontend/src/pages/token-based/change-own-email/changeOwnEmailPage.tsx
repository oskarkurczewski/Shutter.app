import React, { useEffect, useState } from "react";
import styles from "./changeOwnEmailPage.module.scss";
import { Button, Card, TextInput } from "components/shared";
import { useParams } from "react-router-dom";
import { useTranslation } from "react-i18next";
import { useChangeEmailMutation } from "redux/service/tokenBasedService";
import { useAppDispatch } from "redux/hooks";
import { push, ToastTypes } from "redux/slices/toastSlice";
import { ErrorResponse, Toast } from "types";
import { parseError } from "util/errorUtil";

export const ChangeOwnEmailPage = () => {
   const { t } = useTranslation();
   const dispatch = useAppDispatch();
   const { token } = useParams();

   const [newEmail, setNewEmail] = useState("");
   const [confirmEmail, setConfirmEmail] = useState("");
   // TODO
   const [equalityError, setEqualityError] = useState(false);

   const [changeEmailMutation, changeEmailMutationState] = useChangeEmailMutation();

   const onSubmit = () => {
      setEqualityError(false);
      if (newEmail == confirmEmail) {
         return changeEmailMutation({
            newEmail,
            token,
         });
      }
      setEqualityError(true);
   };

   useEffect(() => {
      if (changeEmailMutationState.isSuccess) {
         const successToast: Toast = {
            type: ToastTypes.SUCCESS,
            text: t("toast.success_send_reset_password_link_message"),
         };
         dispatch(push(successToast));
      }
      if (changeEmailMutationState.isError) {
         const err = changeEmailMutationState.error as ErrorResponse;
         const errorToast: Toast = {
            type: ToastTypes.ERROR,
            text: t(parseError(err)),
         };
         dispatch(push(errorToast));
      }
   }, [changeEmailMutationState]);

   return (
      <section className={styles.change_own_email_wrapper}>
         <Card className={styles.card_wrapper}>
            <form>
               <p className="category-title">{t("change_own_email_page.title")}</p>
               <p>{t("change_own_email_page.description")}</p>

               <TextInput
                  label={t("change_own_email_page.email_new")}
                  placeholder={t("global.label.email")}
                  value={newEmail}
                  onChange={(e) => setNewEmail(e.target.value)}
               />
               <TextInput
                  label={t("global.label.repeat_email")}
                  placeholder={t("global.label.repeat_email")}
                  required
                  value={confirmEmail}
                  onChange={(e) => setConfirmEmail(e.target.value)}
               />

               <div className={styles.footer}>
                  <Button
                     loading={changeEmailMutationState.isLoading}
                     onClick={(e) => {
                        e.preventDefault();
                        onSubmit();
                     }}
                  >
                     {t("change_own_email_page.confirm")}
                  </Button>
               </div>
            </form>
         </Card>
      </section>
   );
};
