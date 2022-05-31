import AccountChangeLog from "components/accountInfo/AccountChangeLog";
import BaseAccountInfo from "components/accountInfo/BaseAccountInfo";
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
