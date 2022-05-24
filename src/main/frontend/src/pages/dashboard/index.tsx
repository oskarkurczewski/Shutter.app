import ListUsersFilter from "components/ListUsers/ListUsersFilter";
import Button from "components/shared/Button";
import React from "react";
import { useAppDispatch } from "redux/hooks";
import { logout } from "redux/slices/authSlice";
import "./style.scss";

const DashboardPage = () => {
   const dispatch = useAppDispatch();

   return (
      <section className="dashboard-page-wrapper">
         <Button
            onClick={() => {
               dispatch(logout());
            }}
         >
            Wyloguj się
         </Button>
         <ListUsersFilter />
      </section>
   );
};

export default DashboardPage;
