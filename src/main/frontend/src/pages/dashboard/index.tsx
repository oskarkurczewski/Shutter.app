import React from "react";
import "./style.scss";
import Card from "components/shared/Card";
import jwtDecode from "jwt-decode";
import Button from "components/shared/Button";

interface DashboardProps {
   token: string;
   setToken: (token: string) => void;
}

const DashboardPage = ({ token, setToken }: DashboardProps) => {
   const decoded = jwtDecode(token);
   const decodedJson = JSON.parse(JSON.stringify(decoded));
   const roles = decodedJson.roles.split(",");
   const name = decodedJson.sub;

   const handleClick = () => {
      setToken("");
   };

   return (
      <section className="dashboard-wrapper">
         <Card>
            <div className="header">
               <h3>Witaj {name}</h3>
               <Button onClick={handleClick} icon="logout">
                  Log out
               </Button>
            </div>
            <div className="roles">
               <p className="roles-title">Twoje role to:</p>
               <ul className="roles-list">
                  {roles.map((role: string) => {
                     return (
                        <li key={role} className="role">
                           {role}
                        </li>
                     );
                  })}
               </ul>
            </div>
         </Card>
      </section>
   );
};

export default DashboardPage;
