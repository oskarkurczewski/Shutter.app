import React from "react";
import "./style.scss";
import Card from "components/shared/Card";

interface DashboardProps {
   token: string;
}

const DashboardPage = ({ token }: DashboardProps) => {
   return (
      <section className="dashboard-wrapper">
         <Card>
            <h3>Witaj UnderMan4</h3>
         </Card>
      </section>
   );
};

export default DashboardPage;
