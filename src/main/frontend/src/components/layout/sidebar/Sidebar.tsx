import React, { useState } from "react";
import styles from "./Sidebar.module.scss";
import { useAppSelector } from "redux/hooks";
import { AccessLevel } from "types/AccessLevel";
import { AuthCard, LanguageDropdown } from "components/layout";
import { MultiSelectDropdown } from "components/shared/dropdown/multi-select-dropdown/MultiSelectDropdown";
import { ExampleMultiSelectDropdown } from "components/shared";

export const Sidebar = () => {
   const auth = useAppSelector((state) => state.auth);

   const selectedAccessLevel = localStorage.getItem("accessLevel");

   return (
      <div className={`layout-bar ${styles.sidebar_wrapper}`}>
         <AuthCard
            selectedAccessLevel={selectedAccessLevel as AccessLevel}
            username={auth.username}
            accessLevelList={auth.roles}
         />
         <LanguageDropdown />
      </div>
   );
};
