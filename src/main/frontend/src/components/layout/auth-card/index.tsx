import React, { FC, useEffect, useState } from "react";
import "./style.scss";
import { useAppDispatch, useAppSelector } from "redux/hooks";
import { useSwitchCurrentAccessLevelMutation, useUserInfoQuery } from "redux/service/api";
import { AccessLevel } from "types/AccessLevel";
import Button from "components/shared/Button";
import { setAccessLevel, setUserInfo } from "redux/slices/authSlice";

interface Props {
   username?: string;
   accessLevelList?: AccessLevel[];
   selectedAccessLevel: AccessLevel;
}

const AuthCard: FC<Props> = ({ username, selectedAccessLevel }) => {
   const { roles, accessLevel, name, surname, email } = useAppSelector(
      (state) => state.auth
   );
   const [currentRole, setCurrentRole] = useState(null);
   const { data } = useUserInfoQuery(username);

   const [mutation, mutationState] = useSwitchCurrentAccessLevelMutation();
   const dispatch = useAppDispatch();

   useEffect(() => {
      if (mutationState.isSuccess) {
         dispatch(setAccessLevel({ accessLevel: currentRole }));
      }
   }, [mutationState.isSuccess]);

   useEffect(() => {
      accessLevel && setCurrentRole(accessLevel);
   }, [accessLevel]);

   useEffect(() => {
      data && dispatch(setUserInfo(data));
   }, [data]);

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
         {name && surname && email && (
            <>
               <p>
                  {name} {surname}
               </p>
               <p>{email}</p>
            </>
         )}
         {roles.length > 1 && (
            <>
               <select
                  value={currentRole}
                  onChange={(e) => {
                     setCurrentRole(e.target.value);
                     mutation(AccessLevel[e.target.value]);
                  }}
               >
                  {roles.map((role, index) => (
                     <option key={index} value={role}>
                        {role}
                     </option>
                  ))}
               </select>
            </>
         )}
         {/* <p>Token: {token}</p>
         <p>Exp: {new Date(exp).toLocaleString()}</p> */}
      </div>
   );
};

export default AuthCard;
