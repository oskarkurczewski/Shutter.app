import React, { FC } from "react";
import { BarButton } from "components/layout";
import { BsFillCameraFill } from "react-icons/bs";
import { MdSpaceDashboard } from "react-icons/md";
import { IoSettingsSharp, IoCalendarSharp } from "react-icons/io5";
import { useTranslation } from "react-i18next";

interface Props {
   path: string;
   expanded?: boolean;
}

export const PhotographerButtonList: FC<Props> = ({ path, expanded }) => {
   const { t } = useTranslation();

   const arr = [
      { to: "/", icon: <MdSpaceDashboard />, text: t("navbar.buttons.homepage") },
      {
         to: "/photographers",
         icon: <BsFillCameraFill />,
         text: t("navbar.buttons.photographers"),
      },
      {
         to: "/profile/jobs",
         icon: <IoCalendarSharp />,
         text: t("navbar.buttons.reservations"),
      },
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
