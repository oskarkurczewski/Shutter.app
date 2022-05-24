import "./style.scss";
import React, { useEffect } from "react";
import Card from "components/shared/Card";
import Button from "components/shared/Button";
import { useStopBeingPhotographerMutation, useRefreshMutation } from "redux/service/api";
import { refreshToken } from "util/loginUtil";
import { login } from "redux/slices/authSlice";
import { useAppDispatch } from "redux/hooks";
import { useTranslation } from "react-i18next";

const StopBeingPhotographer = () => {
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
      <Card id="photographer-settings" className="stop-being-photographer-wrapper">
         <p className="category-title">{t("label.photographer-settings")}</p>
         <div className="content">
            <p>{t("message.info.photographer-settings-general")}</p>
            <p>{t("message.info.photographer-settings-warning")}</p>
            <div className="buttons">
               <Button
                  className="red-button"
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

export default StopBeingPhotographer;
