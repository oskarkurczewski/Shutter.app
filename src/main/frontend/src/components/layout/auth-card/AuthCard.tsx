import React, { FC, useEffect } from "react";
import styles from "./AuthCard.module.scss";
import { useTranslation } from "react-i18next";
import { useAppDispatch, useAppSelector } from "redux/hooks";
import { useGetUserInfoQuery } from "redux/service/usersManagementService";
import { setUserInfo } from "redux/slices/authSlice";
import { Button } from "components/shared";
import { useNavigate } from "react-router-dom";

export const AuthCard: FC = () => {
   const dispatch = useAppDispatch();
   const navigate = useNavigate();

   const { accessLevel, name, surname, username } = useAppSelector((state) => state.auth);
   const { data } = useGetUserInfoQuery(username, { skip: !username });

   useEffect(() => {
      data && dispatch(setUserInfo(data));
   }, [data]);
   const { t } = useTranslation();

   return (
      <div className={styles.auth_card_wrapper}>
         <div className={`${styles.background_wrapper} ${styles[accessLevel]}`}>
            <img src="/images/auth-image.png" alt="user sidebar" />
         </div>
         <div className={styles.data_wrapper}>
            <img src="/images/avatar.png" alt="user" className={styles.auth_card_photo} />
            <div className={styles.label_wrapper}>
               <p className="label-bold">
                  {name} {surname}
               </p>
               <div>
                  {username ? (
                     <p className="label">{username}</p>
                  ) : (
                     <Button
                        onClick={() => {
                           navigate("/login");
                        }}
                     >
                        {t("navbar.buttons.login")}
                     </Button>
                  )}
               </div>
            </div>
         </div>
      </div>
   );
};
