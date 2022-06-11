import React, { useState } from "react";
import styles from "./BaseAccountInfo.module.scss";
import { Card } from "components/shared";
import { useTranslation } from "react-i18next";
import { FaCheck } from "react-icons/fa";
import { useGetAdvancedUserInfoQuery } from "redux/service/usersManagementService";

interface Props {
   login: string;
}

export const BaseAccountInfo: React.FC<Props> = ({ login }) => {
   const { t } = useTranslation();

   const [userInfo, setUserInfo] = useState(useGetAdvancedUserInfoQuery(login).data);

   return (
      <Card className={styles.base_account_info_wrapper}>
         {userInfo && (
            <table>
               <tbody>
                  <tr>
                     <td>{t("global.label.login")}:</td>
                     <td>{userInfo.data.login}</td>
                  </tr>
                  <tr>
                     <td>{t("global.label.first_name")}:</td>
                     <td>{userInfo.data.name}</td>
                  </tr>
                  <tr>
                     <td>{t("global.label.second_name")}:</td>
                     <td>{userInfo.data.surname}</td>
                  </tr>
                  <tr>
                     <td>{t("global.label.email")}:</td>
                     <td>{userInfo.data.email}</td>
                  </tr>
                  <tr>
                     <td>{t("global.label.registered")}:</td>
                     <td>
                        {userInfo.data.registered ? <FaCheck className="check" /> : <></>}
                     </td>
                  </tr>
                  <tr>
                     <td>{t("global.label.active")}:</td>
                     <td>
                        {userInfo.data.active ? <FaCheck className="check" /> : <></>}
                     </td>
                  </tr>
                  <tr>
                     <td>{t("user_account_info_page.created_at")}:</td>
                     <td>
                        {userInfo.data.createdAt
                           ? new Date(userInfo.data.createdAt).toUTCString()
                           : "-"}
                     </td>
                  </tr>
                  <tr>
                     <td>{t("user_account_info_page.created_by")}:</td>
                     <td>{userInfo.data.createdBy ? userInfo.data.createdBy : "-"}</td>
                  </tr>
                  <tr>
                     <td>{t("user_account_info_page.modified_at")}:</td>
                     <td>
                        {userInfo.data.createdAt
                           ? new Date(userInfo.data.createdAt).toUTCString()
                           : "-"}
                     </td>
                  </tr>
                  <tr>
                     <td>{t("user_account_info_page.modified_at")}:</td>
                     <td>{userInfo.data.createdBy ? userInfo.data.createdBy : "-"}</td>
                  </tr>
               </tbody>
            </table>
         )}
      </Card>
   );
};
