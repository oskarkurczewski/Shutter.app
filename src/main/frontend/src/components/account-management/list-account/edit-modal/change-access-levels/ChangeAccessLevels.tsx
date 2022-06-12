import { Card, Checkbox } from "components/shared";
import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useChangeAccessLevelMutation } from "redux/service/usersManagementService";
import { advancedUserInfoResponse } from "redux/types/api";
import { EtagData } from "redux/types/api/dataTypes";
import styles from "./ChangeAccessLevels.module.scss";

interface Props {
   userInfoData: EtagData<advancedUserInfoResponse>;
}

export const ChangeAccessLevels: React.FC<Props> = ({ userInfoData }) => {
   const { t } = useTranslation();

   const [allRoles, setAllRoles] = useState({
      ADMINISTRATOR: false,
      MODERATOR: false,
      CLIENT: false,
      PHOTOGRAPHER: false,
   });
   const [accessLevelMutation, accessLevelMutationState] = useChangeAccessLevelMutation();

   useEffect(() => {
      let tempRoles = {};
      userInfoData?.data.accessLevelList.forEach((role) => {
         tempRoles = { ...tempRoles, [role.name]: true };
      });
      setAllRoles({ ...allRoles, ...tempRoles });
   }, [userInfoData]);

   const changeRole = (e: React.ChangeEvent<HTMLInputElement>) => {
      setAllRoles({
         ...allRoles,
         [e.target.id]: e.target.checked,
      });
      accessLevelMutation({
         params: { login: userInfoData.data.login },
         body: { accessLevel: e.target.id, active: e.target.checked },
      });
   };

   useEffect(() => {
      // TODO: add toast
   }, [accessLevelMutationState.isSuccess]);

   return (
      <div className={styles.access_levels}>
         <p className={`section-title`}>{t("edit_account_page.roles.title")}</p>
         <div className={styles.content}>
            <Checkbox
               id="ADMINISTRATOR"
               value={allRoles.ADMINISTRATOR}
               onChange={null}
               disabled
            >
               {t("edit_account_page.roles.admin")}
            </Checkbox>
            <Checkbox id="MODERATOR" value={allRoles.MODERATOR} onChange={changeRole}>
               {t("edit_account_page.roles.moderator")}
            </Checkbox>
            <Checkbox id="USER" value={allRoles.CLIENT} onChange={changeRole}>
               {t("edit_account_page.roles.client")}
            </Checkbox>
            <Checkbox
               id="PHOTOGRAPHER"
               value={allRoles.PHOTOGRAPHER}
               onChange={changeRole}
            >
               {t("edit_account_page.roles.photographer")}
            </Checkbox>
         </div>
      </div>
   );
};
