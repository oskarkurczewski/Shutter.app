import React from "react";
import styles from "./Sidebar.module.scss";
import { useAppSelector } from "redux/hooks";
import { AccessLevel } from "types/AccessLevel";
import AuthCard from "../auth-card/AuthCard";

const Sidebar = () => {
   const auth = useAppSelector((state) => state.auth);

   const selectedAccessLevel = localStorage.getItem("accessLevel");

   return (
      <div className={`layout-bar ${styles.sidebar_wrapper}`}>
         <AuthCard
            selectedAccessLevel={selectedAccessLevel as AccessLevel}
            username={auth.username}
            accessLevelList={auth.roles}
         />
      </div>
   );
};

export default Sidebar;
