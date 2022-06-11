import { Card, Checkbox } from "components/shared";
import React, { useEffect, useState } from "react";
import { useChangeAccessLevelMutation } from "redux/service/usersManagementService";
import { advancedUserInfoResponse } from "redux/types/api";
import { EtagData } from "redux/types/api/dataTypes";
import styles from "./ChangeAccessLevels.module.scss";

interface Props {
   userInfoData: EtagData<advancedUserInfoResponse>;
}

export const ChangeAccessLevels: React.FC<Props> = ({ userInfoData }) => {
   const [allRoles, setAllRoles] = useState({
      ADMINISTRATOR: false,
      MODERATOR: false,
      CLIENT: false,
      PHOTOGRAPHER: false,
   });
   const [accessLevelMutation, accessLevelMutationState] = useChangeAccessLevelMutation();

   useEffect(() => {
      console.log(userInfoData);
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
      <Card className={styles.access_levels}>
         <p className={`category-title ${styles.category_title}`}>Role</p>
         <div className={styles.content}>
            <Checkbox
               id="ADMINISTRATOR"
               value={allRoles.ADMINISTRATOR}
               onChange={null}
               disabled
            >
               ADMINISTRATOR
            </Checkbox>
            <Checkbox id="MODERATOR" value={allRoles.MODERATOR} onChange={changeRole}>
               MODERATOR
            </Checkbox>
            <Checkbox id="USER" value={allRoles.CLIENT} onChange={changeRole}>
               CLIENT
            </Checkbox>
            <Checkbox
               id="PHOTOGRAPHER"
               value={allRoles.PHOTOGRAPHER}
               onChange={changeRole}
            >
               PHOTOGRAPHER
            </Checkbox>
         </div>
      </Card>
   );
};
