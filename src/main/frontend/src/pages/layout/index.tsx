import Navbar from "components/layout/navbar";
import Sidebar from "components/layout/sidebar";
import Breadcumbs from "components/shared/Breadcumbs";
import React from "react";
import { Outlet } from "react-router-dom";
import "./style.scss";

const PageLayout: React.FC = () => {
   return (
      <>
         <Navbar />
         <div className="content">
            <Breadcumbs />
            <Outlet />
         </div>
         <Sidebar />
      </>
   );
};

export default PageLayout;
