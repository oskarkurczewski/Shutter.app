import React, { useEffect } from "react";
import styles from "./BecomePhotographerSettings.module.scss";
import { Button, Card } from "components/shared";
import { useRefreshTokenMutation } from "redux/service/authService";
import { useBecomePhotographerMutation } from "redux/service/userSettingsService";
import { refreshToken } from "util/loginUtil";
import { login } from "redux/slices/authSlice";
import { useAppDispatch } from "redux/hooks";
import { useTranslation } from "react-i18next";

export const BecomePhotographerSettings = () => {
   const { t } = useTranslation();

   const [becomePhotographerMutation] = useBecomePhotographerMutation();
   const [refreshMutation, refreshMutationState] = useRefreshTokenMutation();
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
            <p>{t("message.info.photographer-settings")}</p>
            <div className={styles.buttons}>
               <Button
                  className={styles.button_wrapper}
                  onClick={async () => {
                     await becomePhotographerMutation({});
                     refreshMutation({});
                  }}
               >
                  {t("label.become-photographer")}
               </Button>
            </div>
         </div>
      </Card>
   );
};
