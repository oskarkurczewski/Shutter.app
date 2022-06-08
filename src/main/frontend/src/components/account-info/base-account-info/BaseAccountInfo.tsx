import React, { useState } from "react";
import styles from "./BaseAccountInfo.module.scss";
import { Card } from "components/shared";
import { useTranslation } from "react-i18next";
import { FaCheck } from "react-icons/fa";
import { useAdvancedUserInfoQuery } from "redux/service/api";

interface Props {
   login: string;
}

export const BaseAccountInfo: React.FC<Props> = ({ login }) => {
   const { t } = useTranslation();

   const [userInfo, setUserInfo] = useState(useAdvancedUserInfoQuery(login).data);

   return (
      <Card className={styles.base_account_info_wrapper}>
         {userInfo && (
            <table>
               <tbody>
                  <tr>
                     <td>{t("label.username")}:</td>
                     <td>{userInfo.data.login}</td>
                  </tr>
                  <tr>
                     <td>{t("label.name")}:</td>
                     <td>{userInfo.data.name}</td>
                  </tr>
                  <tr>
                     <td>{t("label.surname")}:</td>
                     <td>{userInfo.data.surname}</td>
                  </tr>
                  <tr>
                     <td>{t("label.email")}:</td>
                     <td>{userInfo.data.email}</td>
                  </tr>
                  <tr>
                     <td>{t("label.registered")}:</td>
                     <td>
                        {userInfo.data.registered ? <FaCheck className="check" /> : <></>}
                     </td>
                  </tr>
                  <tr>
                     <td>{t("label.active")}:</td>
                     <td>
                        {userInfo.data.active ? <FaCheck className="check" /> : <></>}
                     </td>
                  </tr>
                  <tr>
                     <td>{t("label.created-at")}:</td>
                     <td>
                        {userInfo.data.createdAt
                           ? new Date(userInfo.data.createdAt).toUTCString()
                           : "-"}
                     </td>
                  </tr>
                  <tr>
                     <td>{t("label.created-by")}:</td>
                     <td>{userInfo.data.createdBy ? userInfo.data.createdBy : "-"}</td>
                  </tr>
                  <tr>
                     <td>{t("label.last-modified-at")}:</td>
                     <td>
                        {userInfo.data.createdAt
                           ? new Date(userInfo.data.createdAt).toUTCString()
                           : "-"}
                     </td>
                  </tr>
                  <tr>
                     <td>{t("label.last-modified-by")}:</td>
                     <td>{userInfo.data.createdBy ? userInfo.data.createdBy : "-"}</td>
                  </tr>
               </tbody>
            </table>
         )}
      </Card>
   );
};
