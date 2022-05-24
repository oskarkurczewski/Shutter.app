import React, { FC } from "react";
import { useTranslation } from "react-i18next";
import { useAppSelector } from "redux/hooks";
import { AccessLevel } from "types/AccessLevel";
import "./style.scss";

interface Props {
   username?: string;
   accessLevelList?: AccessLevel[];
   selectedAccessLevel: AccessLevel;
}

const AuthCard: FC<Props> = ({ username, selectedAccessLevel }) => {
   const { t } = useTranslation();

   const { token, exp } = useAppSelector((state) => state.auth);

   return (
      <div className="auth-card-wrapper">
         <img src="/images/auth-image.png" alt="user sidebar" />
         <div className="auth-card-data-wrapper">
            <img src="/images/avatar.png" alt="user" className="auth-card-photo" />
            <div className="auth-label-wrapper">
               <p className="label">{selectedAccessLevel}</p>
               <p className="label-bold">{username ? username : t("auth.notloggedin")}</p>
            </div>
         </div>
         <p>
            {t("misc.token")}: {token}
         </p>
         <p>
            {t("misc.exp")}: {new Date(exp).toLocaleString()}
         </p>
      </div>
   );
};

export default AuthCard;
