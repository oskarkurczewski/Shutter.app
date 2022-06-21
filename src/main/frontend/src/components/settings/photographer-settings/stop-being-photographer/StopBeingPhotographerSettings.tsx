import React, { useEffect } from "react";
import styles from "./StopBeingPhotographerSettings.module.scss";
import { Button, Card } from "components/shared";
import { useStopBeingPhotographerMutation } from "redux/service/userSettingsService";
import { useRefreshTokenMutation } from "redux/service/authService";
import { refreshToken } from "util/loginUtil";
import { login } from "redux/slices/authSlice";
import { useAppDispatch } from "redux/hooks";
import { useTranslation } from "react-i18next";
import { ErrorResponse, Toast } from "types";
import { push, ToastTypes } from "redux/slices/toastSlice";
import { parseError } from "util/errorUtil";

export const StopBeingPhotographerSettings = () => {
   const { t } = useTranslation();

   const [stopBeingPhotographerMutation, stopBeingPhotographerMutationState] =
      useStopBeingPhotographerMutation();
   const [refreshTokenMutation, refreshTokenMutationState] = useRefreshTokenMutation();
   const dispatch = useAppDispatch();

   useEffect(() => {
      if (refreshTokenMutationState.isSuccess) {
         const userData = refreshToken(refreshTokenMutationState.data.token);

         dispatch(login(userData));
      }
   }, [refreshTokenMutationState.isSuccess]);

   useEffect(() => {
      if (stopBeingPhotographerMutationState.isSuccess) {
         const successToast: Toast = {
            type: ToastTypes.SUCCESS,
            text: t("toast.success_stop_being_photographer_message"),
         };
         dispatch(push(successToast));
         refreshTokenMutation();
      }
      if (stopBeingPhotographerMutationState.isError) {
         const err = stopBeingPhotographerMutationState.error as ErrorResponse;
         const errorToast: Toast = {
            type: ToastTypes.ERROR,
            text: t(parseError(err)),
         };
         dispatch(push(errorToast));
      }
   }, [stopBeingPhotographerMutationState]);

   return (
      <Card id="photographer-settings" className={styles.card_wrapper}>
         <p className={`category-title ${styles.category_title}`}>
            {t("settings_page.photographer_settings.title")}
         </p>
         <div className="content">
            <p>{t("settings_page.photographer_settings.details_message")}</p>
            <p>{t("settings_page.photographer_settings.warning_message")}</p>
            <div className={styles.buttons}>
               <Button
                  loading={
                     stopBeingPhotographerMutationState.isLoading ||
                     refreshTokenMutationState.isLoading
                  }
                  className={`${styles.red_button} ${styles.button_wrapper}`}
                  onClick={() => {
                     const errorToast: Toast = {
                        type: ToastTypes.WARNING,
                        text: t("settings_page.photographer_settings.resign_confirm"),
                        confirm: {
                           onClick: () => {
                              stopBeingPhotographerMutation();
                           },
                           text: t("global.label.confirm"),
                        },
                     };
                     dispatch(push(errorToast));
                  }}
               >
                  {t("settings_page.photographer_settings.resign_label")}
               </Button>
            </div>
         </div>
      </Card>
   );
};
