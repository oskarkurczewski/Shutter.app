import React from "react";
import styles from "./editUserAccountPage.module.scss";
import { useParams } from "react-router-dom";
import { useGetAdvancedUserInfoQuery } from "redux/service/usersManagementService";
import {
   BaseInfo,
   AccessLevels,
   Password,
} from "components/account-management/edit-account-by-admin";

export const EditUserAccountPage: React.FC = () => {
   const { login } = useParams();

   const { data: userInfoData } = useGetAdvancedUserInfoQuery(login);

   return (
      <div className={styles.create_account_page_wrapper}>
         <BaseInfo userInfoData={userInfoData} />
         <AccessLevels userInfoData={userInfoData} />
         <Password />
      </div>
   );
};
