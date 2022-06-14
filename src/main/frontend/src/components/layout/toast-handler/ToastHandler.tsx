/* eslint-disable react/jsx-key */
import React from "react";
import { useAppSelector } from "redux/hooks";
import { ToastError, ToastInfo, ToastSuccess, ToastWarning } from "components/layout";
import styles from "./ToastHandler.module.scss";
import { ToastRenderType, ToastTypes } from "redux/slices/toastSlice";
import { AnimatePresence } from "framer-motion";

export const ToastHandler = () => {
   const toasters = useAppSelector((state) => state.toast.stack);

   return (
      <div className={styles.toast_handler_wrapper}>
         <AnimatePresence>
            {toasters.map((toast) => renderToast({ ...toast }))}
         </AnimatePresence>
      </div>
   );
};

const renderToast = (props: ToastRenderType) => {
   switch (props.type) {
      case ToastTypes.WARNING:
         return <ToastWarning {...props} key={props.id} />;
      case ToastTypes.ERROR:
         return <ToastError {...props} key={props.id} />;
      case ToastTypes.SUCCESS:
         return <ToastSuccess {...props} key={props.id} />;
      default:
         return <ToastInfo {...props} key={props.id} />;
   }
};
