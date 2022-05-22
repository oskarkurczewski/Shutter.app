import React from "react";
import { useAppSelector } from "redux/hooks";
import { AccessLevel } from "types/AccessLevel";
import AuthCard from "../auth-card";
import "./style.scss";

const Sidebar = () => {
   const auth = useAppSelector((state) => state.auth);

   const selectedAccessLevel = localStorage.getItem("accessLevel");

   return (
      <div className="layout-bar sidebar">
         <AuthCard
            selectedAccessLevel={selectedAccessLevel as AccessLevel}
            username={auth.username}
            accessLevelList={auth.roles}
         />
      </div>
   );
};

export default Sidebar;
