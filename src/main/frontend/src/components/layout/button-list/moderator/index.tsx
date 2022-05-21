import React, { FC } from "react";
import BarButton from "components/layout/bar-button";
import { MdError, MdSpaceDashboard } from "react-icons/md";
import { IoSettingsSharp } from "react-icons/io5";
import { HiUsers } from "react-icons/hi";

interface Props {
   path: string;
   expanded?: boolean;
}

const ModeratorButtonList: FC<Props> = ({ path, expanded }) => {
   const arr = [
      { to: "/", icon: <MdSpaceDashboard />, text: "strona główna" },
      { to: "/users", icon: <HiUsers />, text: "użytkownicy" },
      { to: "/reports", icon: <MdError />, text: "zgłoszenia" },
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

export default ModeratorButtonList;
