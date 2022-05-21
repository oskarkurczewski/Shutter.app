import Navbar from "components/layout/navbar";
import Sidebar from "components/layout/sidebar";
import React from "react";
import { Outlet } from "react-router-dom";
import "./style.scss";

const PageLayout: React.FC = () => {
   return (
      <>
         <Navbar />
         <div className="content">
            <Outlet />
         </div>
         <Sidebar />
      </>
   );
};

export default PageLayout;
