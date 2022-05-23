import ListUsersFilter from "components/ListUsers/ListUsersFilter";
import React from "react";
import "./style.scss";

const DashboardPage = () => {
   return (
      <section className="dashboard-page-wrapper">
         <ListUsersFilter />
      </section>
   );
};

export default DashboardPage;
