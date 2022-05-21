import React from "react";
import "./style.scss";
import Card from "components/shared/Card";
import Button from "components/shared/Button";
import { useAppDispatch, useAppSelector } from "redux/hooks";
import { logout } from "redux/slices/authSlice";
import { useNavigate } from "react-router-dom";

const DashboardPage = () => {
   const dispatch = useAppDispatch();
   const name = useAppSelector((state) => state.auth.username);
   const roles = useAppSelector((state) => state.auth.accessLevel);

   const navigate = useNavigate();

   const handleClick = () => {
      //TODO wylogowanie
      dispatch(logout());
      navigate("/", { replace: true });
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
