import React, { FC } from "react";
import { AccessLevel } from "types/AccessLevel";
import "./style.scss";

interface Props {
   username?: string;
   accessLevelList?: AccessLevel[];
   selectedAccessLevel: AccessLevel;
}

const AuthCard: FC<Props> = ({ username, selectedAccessLevel }) => {
   return (
      <div className="auth-card-wrapper">
         <img src="/images/auth-image.png" alt="user sidebar" />
         <div className="auth-card-data-wrapper">
            <img src="/images/avatar.png" alt="user" className="auth-card-photo" />
            <div className="auth-label-wrapper">
               <p className="label">{selectedAccessLevel}</p>
               <p className="label-bold">{username ? username : "Niezalogowany"}</p>
            </div>
         </div>
      </div>
   );
};

export default AuthCard;
