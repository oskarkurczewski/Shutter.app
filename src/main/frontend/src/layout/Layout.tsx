import React from "react";
import styles from "./Layout.module.scss";
import Navbar from "components/layout/navbar/Navbar";
import Sidebar from "components/layout/sidebar/Sidebar";
import Breadcumbs from "components/shared/breadcumbs/Breadcumbs";
import ToastHandler from "components/layout/toast-handler/ToastHandler";
import { Outlet } from "react-router-dom";

const PageLayout: React.FC = () => {
   return (
      <>
         <Navbar />
         <div className={styles.content}>
            <Breadcumbs />
            <Outlet />
            <ToastHandler />
         </div>
         <Sidebar />
      </>
   );
};

export default PageLayout;
