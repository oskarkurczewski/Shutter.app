import { Checkbox } from "components/shared";
import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useAppDispatch } from "redux/hooks";
import { useChangeAccessLevelMutation } from "redux/service/usersManagementService";
import { push, ToastTypes } from "redux/slices/toastSlice";
import { advancedUserInfoResponse } from "redux/types/api";
import { EtagData } from "redux/types/api/dataTypes";
import { AccessLevel, ErrorResponse, Toast } from "types";
import { parseError } from "util/errorUtil";
import styles from "./ChangeAccessLevels.module.scss";

interface Props {
   userInfoData: EtagData<advancedUserInfoResponse>;
   refetch: () => void;
}

export const ChangeAccessLevels: React.FC<Props> = ({ userInfoData, refetch }) => {
   const { t } = useTranslation();
   const dispatch = useAppDispatch();

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
      if (accessLevelMutationState.isSuccess) {
         const successToast: Toast = {
            type: ToastTypes.SUCCESS,
            text: t("toast.success_edit_account_info"),
         };
         dispatch(push(successToast));
         refetch();
      }

      if (accessLevelMutationState.isError) {
         const err = accessLevelMutationState.error as ErrorResponse;
         const errorToast: Toast = {
            type: ToastTypes.ERROR,
            text: t(parseError(err)),
         };
         dispatch(push(errorToast));
      }
   }, [accessLevelMutationState]);

   return (
      <div className={styles.access_levels}>
         <p className={`section-title`}>{t("edit_account_page.roles.title")}</p>
         <div className={styles.content}>
            <Checkbox
               id={AccessLevel.ADMINISTRATOR}
               value={allRoles.ADMINISTRATOR}
               onChange={null}
               disabled
            >
               {t("global.roles.administrator")}
            </Checkbox>
            <Checkbox
               id={AccessLevel.MODERATOR}
               value={allRoles.MODERATOR}
               onChange={changeRole}
            >
               {t("global.roles.moderator")}
            </Checkbox>
            <Checkbox
               id={AccessLevel.CLIENT}
               value={allRoles.CLIENT}
               onChange={changeRole}
            >
               {t("global.roles.client")}
            </Checkbox>
            <Checkbox
               id={AccessLevel.PHOTOGRAPHER}
               value={allRoles.PHOTOGRAPHER}
               onChange={changeRole}
            >
               {t("global.roles.photographer")}
            </Checkbox>
         </div>
      </div>
   );
};
