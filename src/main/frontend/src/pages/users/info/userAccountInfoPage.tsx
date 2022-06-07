import React from "react";
import styles from "./userAccountInfoPage.module.scss";
import AccountChangeLog from "components/account-info/account-change-log/AccountChangeLog";
import BaseAccountInfo from "components/account-info/base-account-info/BaseAccountInfo";
import { useParams } from "react-router-dom";

const UserAccountInfoPage = () => {
   const { login } = useParams();

   return (
      <div className={styles.account_info_wrapper}>
         <BaseAccountInfo login={login} />
         <AccountChangeLog login={login} />
      </div>
   );
};

export default UserAccountInfoPage;
