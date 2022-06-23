import React, { useEffect, useMemo } from "react";
import styles from "./Layout.module.scss";
import { Navbar, Sidebar, ToastHandler } from "components/layout";
import { Breadcrumbs } from "components/shared";
import { Outlet, useLocation } from "react-router-dom";
import { useTranslation } from "react-i18next";
import { useAppDispatch, useAppSelector } from "redux/hooks";
import { useRefreshTokenMutation } from "redux/service/authService";
import { login, logout } from "redux/slices/authSlice";
import { ToastTypes, push } from "redux/slices/toastSlice";
import { Toast } from "types";
import { getLoginPayload, getTokenExp } from "util/loginUtil";
import { motion } from "framer-motion";

const variants = {
   hidden: { opacity: 0, x: -200, y: 0 },
   enter: { opacity: 1, x: 0, y: 0 },
};

export const PageLayout: React.FC = () => {
   const { t } = useTranslation();
   const dispatch = useAppDispatch();
   const exp = useAppSelector((state) => state.auth.exp);
   const [refreshToken] = useRefreshTokenMutation();

   const location = useLocation();

   const logoutToast: Toast = {
      type: ToastTypes.SUCCESS,
      name: "logout-success",
      text: t("logout_modal.successfully_logged_out"),
   };

   const sessionToast: Toast = useMemo(
      () => ({
         type: ToastTypes.WARNING,
         text: t("toast.renew_session_message"),
         name: "renew-session",
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

   if (localStorage.getItem("token") && Date.now() < getTokenExp()) {
      dispatch(login(getLoginPayload()));
   } else {
      dispatch(logout());
   }

   useEffect(() => {
      if (exp !== 0 && exp - Date.now() < 1000 * 60 * 2) dispatch(push(sessionToast));
      const timeoutID = setInterval(() => {
         // logout automatically
         if (exp < Date.now()) {
            return dispatch(logout());
            dispatch(push(logoutToast));
         }

         // push notification for session renewal
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
         <div
            className={`${styles.content} ${
               location.pathname === "/" ? styles.background : ""
            }`}
         >
            <Breadcrumbs />

            <motion.div
               key={location.pathname}
               className={styles.animation_wrapper}
               variants={variants}
               initial="hidden"
               animate="enter"
               exit="exit"
               transition={{ type: "linear" }}
            >
               <Outlet />
            </motion.div>

            <ToastHandler />
         </div>
         <Sidebar />
      </>
   );
};
