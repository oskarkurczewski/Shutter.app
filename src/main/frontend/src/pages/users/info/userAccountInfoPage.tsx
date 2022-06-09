import React from "react";
import styles from "./userAccountInfoPage.module.scss";
import { AccountChangeLog, BaseAccountInfo } from "components/account-info";
import { useParams } from "react-router-dom";

export const UserAccountInfoPage = () => {
   const { login } = useParams();

   return (
      <div className={styles.account_info_wrapper}>
         <BaseAccountInfo login={login} />
         <AccountChangeLog login={login} />
      </div>
   );
};
