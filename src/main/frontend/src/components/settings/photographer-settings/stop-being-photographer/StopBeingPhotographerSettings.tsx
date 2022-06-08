import styles from "./StopBeingPhotographerSettings.module.scss";
import React, { useEffect } from "react";
import { Button, Card } from "components/shared";
import { useStopBeingPhotographerMutation, useRefreshMutation } from "redux/service/api";
import { refreshToken } from "util/loginUtil";
import { login } from "redux/slices/authSlice";
import { useAppDispatch } from "redux/hooks";
import { useTranslation } from "react-i18next";

export const StopBeingPhotographerSettings = () => {
   const { t } = useTranslation();

   const [stopBeingPhotographerMutation] = useStopBeingPhotographerMutation();
   const [refreshMutation, refreshMutationState] = useRefreshMutation();
   const dispatch = useAppDispatch();

   useEffect(() => {
      if (refreshMutationState.isSuccess) {
         const userData = refreshToken(refreshMutationState.data.token);

         dispatch(login(userData));
      }
   }, [refreshMutationState.isSuccess]);

   return (
      <Card id="photographer-settings" className={styles.card_wrapper}>
         <p className={`category-title ${styles.category_title}`}>
            {t("label.photographer-settings")}
         </p>
         <div className="content">
            <p>{t("message.info.photographer-settings-general")}</p>
            <p>{t("message.info.photographer-settings-warning")}</p>
            <div className={styles.buttons}>
               <Button
                  className={`${styles.red_button} ${styles.button_wrapper}`}
                  onClick={async () => {
                     await stopBeingPhotographerMutation({});
                     refreshMutation({});
                  }}
               >
                  {t("label.hide-photographer")}
               </Button>
            </div>
         </div>
      </Card>
   );
};
