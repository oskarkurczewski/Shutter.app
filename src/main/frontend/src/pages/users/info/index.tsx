import BaseAccountInfo from "components/accountInfo/BaseAccountInfo";
import React from "react";
import { useParams } from "react-router-dom";
import "./style.scss";

const AccountInfoPage = () => {
   const { login } = useParams();

   return (
      <div>
         <BaseAccountInfo login={login} />
      </div>
   );
};

export default AccountInfoPage;
