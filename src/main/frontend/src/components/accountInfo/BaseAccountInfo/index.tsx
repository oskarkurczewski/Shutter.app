import Card from "components/shared/Card";
import React, { useState } from "react";
import { useTranslation } from "react-i18next";
import { FaCheck } from "react-icons/fa";
import { useAdvancedUserInfoQuery } from "redux/service/api";
import "./style.scss";

interface Props {
   login: string;
}

const BaseAccountInfo: React.FC<Props> = ({ login }) => {
   const { t } = useTranslation();

   const [userInfo] = useState(useAdvancedUserInfoQuery(login).data);

   console.log(userInfo);

   return (
      <Card className={"base-account-info-wrapper"}>
         {userInfo && (
            <table>
               <tbody>
                  <tr>
                     <td>{t("label.name")}:</td>
                     <td>{userInfo.name}</td>
                  </tr>
                  <tr>
                     <td>{t("label.surname")}:</td>
                     <td>{userInfo.surname}</td>
                  </tr>
                  <tr>
                     <td>{t("label.email")}:</td>
                     <td>{userInfo.email}</td>
                  </tr>
                  <tr>
                     <td>{t("label.registered")}:</td>
                     <td>
                        {userInfo.registered ? <FaCheck className="check" /> : <></>}
                     </td>
                  </tr>
                  <tr>
                     <td>{t("label.active")}:</td>
                     <td>{userInfo.active ? <FaCheck className="check" /> : <></>}</td>
                  </tr>
                  <tr>
                     <td>{t("label.created-at")}:</td>
                     <td>
                        {userInfo.createdAt
                           ? new Date(userInfo.createdAt).toUTCString()
                           : "-"}
                     </td>
                  </tr>
                  <tr>
                     <td>{t("label.created-by")}:</td>
                     <td>{userInfo.createdBy ? userInfo.createdBy : "-"}</td>
                  </tr>
                  <tr>
                     <td>{t("label.last-modified-at")}:</td>
                     <td>
                        {userInfo.createdAt
                           ? new Date(userInfo.createdAt).toUTCString()
                           : "-"}
                     </td>
                  </tr>
                  <tr>
                     <td>{t("label.last-modified-by")}:</td>
                     <td>{userInfo.createdBy ? userInfo.createdBy : "-"}</td>
                  </tr>
               </tbody>
            </table>
         )}
      </Card>
   );
};

export default BaseAccountInfo;
