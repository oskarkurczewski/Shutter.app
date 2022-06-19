import React, { useState } from "react";
import styles from "./dashboardPage.module.scss";
import { Button, Card } from "components/shared";
import { useAppDispatch, useAppSelector } from "redux/hooks";
import { logout } from "redux/slices/authSlice";

export const DashboardPage = () => {
   const dispatch = useAppDispatch();
   const { token } = useAppSelector((state) => state.auth);
   const [time, setTime] = useState<string>(null);

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
      </section>
   );
};
