import React, { FC } from "react";
import BarButton from "components/layout/bar-button";
import { BsFillCameraFill } from "react-icons/bs";
import { MdSpaceDashboard } from "react-icons/md";
import { IoSettingsSharp, IoCalendarSharp } from "react-icons/io5";

interface Props {
   path: string;
   expanded?: boolean;
}

const PhotographerButtonList: FC<Props> = ({ path, expanded }) => {
   const arr = [
      { to: "/", icon: <MdSpaceDashboard />, text: "strona główna" },
      { to: "/photographers", icon: <BsFillCameraFill />, text: "fotografowie" },
      { to: "/reservations", icon: <IoCalendarSharp />, text: "rezerwacje" },
      { to: "/settings", icon: <IoSettingsSharp />, text: "ustawienia" },
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

export default PhotographerButtonList;
