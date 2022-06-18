import { IconDropdown } from "components/shared";
import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { RiShieldUserFill } from "react-icons/ri";
import { useAppDispatch, useAppSelector } from "redux/hooks";
import { useSwitchCurrentAccessLevelMutation } from "redux/service/authService";
import { setAccessLevel } from "redux/slices/authSlice";
import { push, ToastTypes } from "redux/slices/toastSlice";
import { AccessLevel, Toast } from "types";

export const RoleDropdown = () => {
   const { t } = useTranslation();
   const dispatch = useAppDispatch();

   const { roles, accessLevel } = useAppSelector((state) => state.auth);
   const [options, setOptions] = useState({});

   const [notifyBackend] = useSwitchCurrentAccessLevelMutation();

   useEffect(() => {
      const opts = {};
      roles.forEach((v: AccessLevel) => (opts[v] = t(`global.roles.${v.toLowerCase()}`)));

      if (Object.keys(roles).length === 0)
         opts[AccessLevel.GUEST] = t(`global.roles.guest`);

      setOptions(opts);
   }, [roles]);

   return (
      <IconDropdown
         options={options}
         value={accessLevel}
         onChange={(key) => {
            notifyBackend(key as AccessLevel);
            dispatch(setAccessLevel({ accessLevel: key as AccessLevel }));
            const successToast: Toast = {
               type: ToastTypes.SUCCESS,
               text: t("sidebar.changed_role_message", {
                  role: t(`global.roles.${key.toLowerCase()}`),
               }),
            };
            dispatch(push(successToast));
         }}
         icon={<RiShieldUserFill />}
         disabled={Object.keys(options).length < 2}
      />
   );
};
