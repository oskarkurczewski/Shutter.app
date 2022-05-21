import React, { FC } from "react";
import BarButton from "components/layout/bar-button";
import { BsFillCameraFill } from "react-icons/bs";
import { MdSpaceDashboard } from "react-icons/md";
import { IoSettingsSharp } from "react-icons/io5";
import { FaRegCalendarCheck } from "react-icons/fa";

interface Props {
   path: string;
   expanded?: boolean;
}

const ClientButtonList: FC<Props> = ({ path, expanded }) => {
   const arr = [
      { to: "/", icon: <MdSpaceDashboard />, text: "strona główna" },
      { to: "/photographers", icon: <BsFillCameraFill />, text: "fotografowie" },
      { to: "/reservations", icon: <FaRegCalendarCheck />, text: "rezerwacje" },
      { to: "/settings", icon: <IoSettingsSharp />, text: "ustawienia" },
   ];

   return (
      <>
         {arr.map((btn) =>
            path === btn.to ? (
               <BarButton
                  active
                  expanded={expanded}
                  to={btn.to}
                  icon={btn.icon}
                  key={btn.to}
                  text={btn.text}
               />
            ) : (
               <BarButton
                  expanded={expanded}
                  to={btn.to}
                  icon={btn.icon}
                  key={btn.to}
                  text={btn.text}
               />
            )
         )}
      </>
   );
};

export default ClientButtonList;
