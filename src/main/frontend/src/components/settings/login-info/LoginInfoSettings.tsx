import React from "react";
import styles from "./LoginInfoSettings.module.scss";
import { DateTime } from "luxon";
import { useTranslation } from "react-i18next";
import { useAppSelector } from "redux/hooks";
import { useGetUserInfoQuery } from "redux/service/usersManagementService";

export const LoginInfoSettings = () => {
   const { t } = useTranslation();

   const { username } = useAppSelector((state) => state.auth);
   const { data } = useGetUserInfoQuery(username, { skip: !username });

   return (
      <div className={styles.login_info_wrapper}>
         <p className="label-bold">
            {t("settings_page.login_info.last_successful_login")}:{" "}
            <span className="label">
               {DateTime.fromISO(data?.lastLogIn).toFormat("dd.LL.yyyy HH:mm:ss")}
            </span>
         </p>
         <p className="label-bold">
            {t("settings_page.login_info.last_unsuccessful_login")}:{" "}
            <span className="label">
               {DateTime.fromISO(data?.lastFailedLogInAttempt).toFormat(
                  "dd.LL.yyyy HH:mm:ss"
               )}{" "}
               - {data?.lastFailedLoginIp}
            </span>
         </p>
      </div>
   );
};
