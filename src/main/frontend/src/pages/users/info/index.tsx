import AccountChangeLog from "components/account-info/account-change-log";
import BaseAccountInfo from "components/account-info/base-account-info";
import React from "react";
import { useParams } from "react-router-dom";
import "./style.scss";

const AccountInfoPage = () => {
   const { login } = useParams();

   return (
      <div className="account-info-wrapper">
         <BaseAccountInfo login={login} />
         <AccountChangeLog login={login} />
      </div>
   );
};

export default AccountInfoPage;
