import React, { FC } from "react";
import BarButton from "components/layout/bar-button";
import { BsFillCameraFill } from "react-icons/bs";
import { MdSpaceDashboard } from "react-icons/md";

interface Props {
   path: string;
   expanded?: boolean;
}

const GuestButtonList: FC<Props> = ({ path, expanded }) => {
   const arr = [
      { to: "/", icon: <MdSpaceDashboard />, text: "strona główna" },
      { to: "/photographers", icon: <BsFillCameraFill />, text: "fotografowie" },
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

export default GuestButtonList;
