import React, { FC } from "react";
import BarButton from "components/layout/bar-button/BarButton";
import { BsFillCameraFill } from "react-icons/bs";
import { MdSpaceDashboard } from "react-icons/md";
import { IoSettingsSharp } from "react-icons/io5";
import { FaRegCalendarCheck } from "react-icons/fa";
import { useTranslation } from "react-i18next";

interface Props {
   path: string;
   expanded?: boolean;
}

const ClientButtonList: FC<Props> = ({ path, expanded }) => {
   const { t } = useTranslation();

   const arr = [
      { to: "/", icon: <MdSpaceDashboard />, text: t("navbar.buttons.homepage") },
      {
         to: "/photographers",
         icon: <BsFillCameraFill />,
         text: t("navbar.buttons.photographers"),
      },
      {
         to: "/reservations",
         icon: <FaRegCalendarCheck />,
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

export default ClientButtonList;
