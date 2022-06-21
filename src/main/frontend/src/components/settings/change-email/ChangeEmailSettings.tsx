import React, { useEffect } from "react";
import styles from "./ChangeEmailSettings.module.scss";
import { Button, Card } from "components/shared";
import { useTranslation } from "react-i18next";
import { useSendChangeEmailLinkMutation } from "redux/service/userSettingsService";
import { useAppDispatch } from "redux/hooks";
import { push, ToastTypes } from "redux/slices/toastSlice";
import { ErrorResponse, Toast } from "types";
import { parseError } from "util/errorUtil";

export const ChangeEmailSettings = () => {
   const { t } = useTranslation();
   const dispatch = useAppDispatch();

   const [mutation, mutationState] = useSendChangeEmailLinkMutation();

   useEffect(() => {
      if (mutationState.isSuccess) {
         const successToast: Toast = {
            type: ToastTypes.SUCCESS,
            text: t("toast.success_send_reset_email_link_message"),
         };
         dispatch(push(successToast));
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
      <Card id="change-email" className={styles.card_wrapper}>
         <p className={`category-title ${styles.category_title}`}>
            {t("settings_page.email_settings.title")}
         </p>
         <p>{t("settings_page.email_settings.details_message")}</p>
         <Button
            loading={mutationState.isLoading}
            onClick={() => {
               mutation();
            }}
         >
            {t("settings_page.email_settings.send_link")}
         </Button>
      </Card>
   );
};
