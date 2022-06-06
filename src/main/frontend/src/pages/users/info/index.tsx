import AccountChangeLog from "components/account-info/account-change-log";
import BaseAccountInfo from "components/account-info/base-account-info";
import React from "react";
import { useParams } from "react-router-dom";
import styles from "./style.module.scss";

const AccountInfoPage = () => {
   const { login } = useParams();

   return (
      <div className={styles.account_info_wrapper}>
         <BaseAccountInfo login={login} />
         <AccountChangeLog login={login} />
      </div>
   );
};

export default AccountInfoPage;
