import React from "react";
import "./style.scss";
import Button from "components/shared/button";
import { useAppDispatch, useAppSelector } from "redux/hooks";
import { logout } from "redux/slices/authSlice";

const DashboardPage = () => {
   const dispatch = useAppDispatch();
   const { token } = useAppSelector((state) => state.auth);

   return (
      <section className="dashboard-page-wrapper">
         {token && (
            <Button
               onClick={() => {
                  dispatch(logout());
               }}
            >
               Logout
            </Button>
         )}
      </section>
   );
};

export default DashboardPage;
