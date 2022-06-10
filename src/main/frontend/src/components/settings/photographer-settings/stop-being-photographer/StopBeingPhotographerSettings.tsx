import styles from "./StopBeingPhotographerSettings.module.scss";
import React, { useEffect } from "react";
import { Button, Card } from "components/shared";
import { useStopBeingPhotographerMutation } from "redux/service/userSettingsService";
import { useRefreshTokenMutation } from "redux/service/authService";
import { refreshToken } from "util/loginUtil";
import { login } from "redux/slices/authSlice";
import { useAppDispatch } from "redux/hooks";
import { useTranslation } from "react-i18next";

export const StopBeingPhotographerSettings = () => {
   const { t } = useTranslation();

   const [stopBeingPhotographerMutation] = useStopBeingPhotographerMutation();
   const [refreshTokenMutation, refreshTokenMutationState] = useRefreshTokenMutation();
   const dispatch = useAppDispatch();

   useEffect(() => {
      if (refreshTokenMutationState.isSuccess) {
         const userData = refreshToken(refreshTokenMutationState.data.token);

         dispatch(login(userData));
      }
   }, [refreshTokenMutationState.isSuccess]);

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
                  className={`${styles.red_button} ${styles.button_wrapper}`}
                  onClick={async () => {
                     await stopBeingPhotographerMutation();
                     refreshTokenMutation();
                  }}
               >
                  {t("settings_page.photographer_settings.resign_confirm")}
               </Button>
            </div>
         </div>
      </Card>
   );
};
