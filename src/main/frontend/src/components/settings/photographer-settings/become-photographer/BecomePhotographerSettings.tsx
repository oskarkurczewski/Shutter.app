import React, { useEffect } from "react";
import styles from "./BecomePhotographerSettings.module.scss";
import { Button, Card } from "components/shared";
import { useRefreshTokenMutation } from "redux/service/authService";
import { useBecomePhotographerMutation } from "redux/service/userSettingsService";
import { refreshToken } from "util/loginUtil";
import { login } from "redux/slices/authSlice";
import { useAppDispatch } from "redux/hooks";
import { useTranslation } from "react-i18next";
import { push, ToastTypes } from "redux/slices/toastSlice";
import { ErrorResponse, Toast } from "types";
import { parseError } from "util/errorUtil";

export const BecomePhotographerSettings = () => {
   const { t } = useTranslation();

   const [becomePhotographerMutation, becomePhotographerMutationState] =
      useBecomePhotographerMutation();
   const [refreshTokenMutation, refreshTokenMutationState] = useRefreshTokenMutation();
   const dispatch = useAppDispatch();

   useEffect(() => {
      if (refreshTokenMutationState.isSuccess) {
         const userData = refreshToken(refreshTokenMutationState.data.token);

         dispatch(login(userData));
      }
   }, [refreshTokenMutationState]);

   useEffect(() => {
      if (becomePhotographerMutationState.isSuccess) {
         const successToast: Toast = {
            type: ToastTypes.SUCCESS,
            text: t("toast.success_become_photographer_message"),
         };
         refreshTokenMutation();
         dispatch(push(successToast));
      }
      if (becomePhotographerMutationState.isError) {
         const err = becomePhotographerMutationState.error as ErrorResponse;
         const errorToast: Toast = {
            type: ToastTypes.ERROR,
            text: t(parseError(err)),
         };
         dispatch(push(errorToast));
      }
   }, [becomePhotographerMutationState]);

   return (
      <Card id="photographer-settings" className={styles.card_wrapper}>
         <p className={`category-title ${styles.category_title}`}>
            {t("settings_page.photographer_settings.title")}
         </p>
         <div className="content">
            <p>{t("settings_page.photographer_settings.become_message")}</p>
            <div className={styles.buttons}>
               <Button
                  loading={
                     becomePhotographerMutationState.isLoading ||
                     refreshTokenMutationState.isLoading
                  }
                  className={styles.button_wrapper}
                  onClick={() => {
                     const errorToast: Toast = {
                        type: ToastTypes.WARNING,
                        text: t("settings_page.photographer_settings.become_confirm"),
                        confirm: {
                           onClick: () => {
                              becomePhotographerMutation();
                           },
                           text: t("global.label.confirm"),
                        },
                     };
                     dispatch(push(errorToast));
                  }}
               >
                  {t("settings_page.photographer_settings.become_label")}
               </Button>
            </div>
         </div>
      </Card>
   );
};
