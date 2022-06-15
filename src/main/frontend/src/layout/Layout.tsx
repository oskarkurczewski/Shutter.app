import React from "react";
import styles from "./Layout.module.scss";
import { Navbar, Sidebar, ToastHandler } from "components/layout";
import { Breadcrumbs } from "components/shared";
import { Outlet } from "react-router-dom";

export const PageLayout: React.FC = () => {
   return (
      <>
         <Navbar />
         <div className={styles.content}>
            <Breadcrumbs />
            <Outlet />
            <ToastHandler />
         </div>
         <Sidebar />
      </>
   );
};
