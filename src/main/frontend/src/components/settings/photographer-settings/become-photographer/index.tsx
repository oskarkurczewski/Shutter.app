import "./style.scss";
import React, { useEffect } from "react";
import Card from "components/shared/card";
import Button from "components/shared/button";
import { useBecomePhotographerMutation, useRefreshMutation } from "redux/service/api";
import { refreshToken } from "util/loginUtil";
import { login } from "redux/slices/authSlice";
import { useAppDispatch } from "redux/hooks";
import { useTranslation } from "react-i18next";

const BecomePhotographer = () => {
   const { t } = useTranslation();

   const [becomePhotographerMutation] = useBecomePhotographerMutation();
   const [refreshMutation, refreshMutationState] = useRefreshMutation();
   const dispatch = useAppDispatch();

   useEffect(() => {
      if (refreshMutationState.isSuccess) {
         const userData = refreshToken(refreshMutationState.data.token);

         dispatch(login(userData));
      }
   }, [refreshMutationState.isSuccess]);

   return (
      <Card id="photographer-settings" className="become-photographer-wrapper">
         <p className="category-title">{t("label.photographer-settings")}</p>
         <div className="content">
            <p>{t("message.info.photographer-settings")}</p>
            <div className="buttons">
               <Button
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

export default BecomePhotographer;
