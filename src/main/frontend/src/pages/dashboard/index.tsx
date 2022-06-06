import React from "react";
import styles from "./style.module.scss";
import Button from "components/shared/button";
import { useAppDispatch, useAppSelector } from "redux/hooks";
import { logout } from "redux/slices/authSlice";

const DashboardPage = () => {
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
      </section>
   );
};

export default DashboardPage;
