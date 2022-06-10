import React from "react";
import styles from "./dashboardPage.module.scss";
import { Button } from "components/shared";
import { useAppDispatch, useAppSelector } from "redux/hooks";
import { logout } from "redux/slices/authSlice";
import { Calendar } from "components/shared/calendar";

export const DashboardPage = () => {
   const dispatch = useAppDispatch();
   const { token } = useAppSelector((state) => state.auth);

   return (
      <section className={styles.dashboard_page_wrapper}>
         {token && (
            <Button
               onClick={() => {
                  dispatch(logout());
               }}
            >
               Logout
            </Button>
         )}
         <Calendar />
      </section>
   );
};
