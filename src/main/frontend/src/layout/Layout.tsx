import React, { useEffect, useMemo } from "react";
import styles from "./Layout.module.scss";
import { Navbar, Sidebar, ToastHandler } from "components/layout";
import { Breadcumbs } from "components/shared";
import { Outlet } from "react-router-dom";
import { useTranslation } from "react-i18next";
import { useAppDispatch, useAppSelector } from "redux/hooks";
import { useRefreshTokenMutation } from "redux/service/authService";
import { login, logout } from "redux/slices/authSlice";
import { ToastTypes, push } from "redux/slices/toastSlice";
import { Toast } from "types";
import { getTokenExp, getLoginPayload } from "util/loginUtil";

export const PageLayout: React.FC = () => {
   const { t } = useTranslation();
   const dispatch = useAppDispatch();
   const exp = useAppSelector((state) => state.auth.exp);
   const [refreshToken] = useRefreshTokenMutation();

   useEffect(() => {
      if (localStorage.getItem("token") && Date.now() < getTokenExp()) {
         dispatch(login(getLoginPayload()));
      } else {
         dispatch(logout());
      }
   }, []);

   const sessionToast: Toast = useMemo(
      () => ({
         type: ToastTypes.WARNING,
         text: t("toast.renew_session_message"),
         confirm: {
            onClick: async () => {
               try {
                  const token = await refreshToken().unwrap();
                  localStorage.setItem("token", token.token);
                  dispatch(login(getLoginPayload()));
               } catch (err) {
                  return;
               }
            },
            text: t("toast.renew"),
         },
      }),
      [t]
   );

   useEffect(() => {
      if (exp !== 0 && exp - Date.now() < 1000 * 60 * 2) dispatch(push(sessionToast));
      const timeoutID = setInterval(() => {
         if (exp !== 0 && exp - Date.now() < 1000 * 60 * 2) {
            dispatch(push(sessionToast));
         }
      }, 1000 * 60 * 0.5);
      return () => {
         clearTimeout(timeoutID);
      };
   }, [exp]);

   return (
      <>
         <Navbar />
         <div className={styles.content}>
            <Breadcumbs />
            <Outlet />
            <ToastHandler />
         </div>
         <Sidebar />
      </>
   );
};
