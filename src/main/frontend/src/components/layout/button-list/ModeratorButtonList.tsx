import React, { FC } from "react";
import { BarButton } from "components/layout";
import { MdError, MdSpaceDashboard } from "react-icons/md";
import { IoSettingsSharp } from "react-icons/io5";
import { HiUsers } from "react-icons/hi";
import { useTranslation } from "react-i18next";

interface Props {
   path: string;
   expanded?: boolean;
}

export const ModeratorButtonList: FC<Props> = ({ path, expanded }) => {
   const { t } = useTranslation();

   const arr = [
      { to: "/", icon: <MdSpaceDashboard />, text: t("navbar.buttons.homepage") },
      { to: "/users", icon: <HiUsers />, text: t("navbar.buttons.users") },
      { to: "/reports", icon: <MdError />, text: t("navbar.buttons.reports") },
      { to: "/settings", icon: <IoSettingsSharp />, text: t("navbar.buttons.settings") },
   ];

   return (
      <>
         {arr.map((btn) => (
            <BarButton
               active={path === btn.to}
               expanded={expanded}
               to={btn.to}
               icon={btn.icon}
               key={btn.to}
               text={btn.text}
            />
         ))}
      </>
   );
};
