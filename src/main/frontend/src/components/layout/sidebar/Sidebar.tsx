import React from "react";
import styles from "./Sidebar.module.scss";
import { AuthCard, LanguageDropdown, RoleDropdown } from "components/layout";

export const Sidebar = () => {
   return (
      <div className={`layout-bar ${styles.sidebar_wrapper}`}>
         <AuthCard />
         <div className={styles.dropdowns_wrapper}>
            <LanguageDropdown />
            <RoleDropdown />
         </div>
      </div>
   );
};
