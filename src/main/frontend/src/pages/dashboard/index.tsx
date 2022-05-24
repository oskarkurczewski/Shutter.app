import React from "react";
import "./style.scss";
import Card from "components/shared/Card";
import Toast from "components/layout/toast";

const DashboardPage = () => {
   return (
      <section className="dashboard-page-wrapper">
         <Card>
            <Toast></Toast>
         </Card>
      </section>
   );
};

export default DashboardPage;
