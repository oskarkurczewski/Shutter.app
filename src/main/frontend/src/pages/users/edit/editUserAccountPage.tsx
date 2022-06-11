import React from "react";
import styles from "./editUserAccountPage.module.scss";
import { useParams } from "react-router-dom";
import { useGetAdvancedUserInfoQuery } from "redux/service/usersManagementService";
import {
   ChangeBaseInfo,
   ChangeAccessLevels,
   ChangePassword,
} from "components/account-management/edit-account-by-admin";
import { useAppSelector } from "redux/hooks";
import { AccessLevel } from "types";

export const EditUserAccountPage: React.FC = () => {
   const { login } = useParams();

   const { data: userInfoData } = useGetAdvancedUserInfoQuery(login);
   const { accessLevel } = useAppSelector((state) => state.auth);

   return (
      <div className={styles.edit_account_page_wrapper}>
         <ChangeBaseInfo userInfoData={userInfoData} />
         {accessLevel === AccessLevel.ADMINISTRATOR && (
            // <div>
            <>
               <ChangeAccessLevels userInfoData={userInfoData} />
               <ChangePassword login={login} />
            </>
            // </div>
         )}
      </div>
   );
};
