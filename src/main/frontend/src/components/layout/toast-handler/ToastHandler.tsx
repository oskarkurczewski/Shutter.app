import React from "react";
import { useAppSelector } from "redux/hooks";
import { ToastError, ToastInfo, ToastSuccess, ToastWarning } from "components/layout";
import styles from "./ToastHandler.module.scss";
import { ToastRenderType, ToastTypes } from "redux/slices/toastSlice";

export const ToastHandler = () => {
   const toasters = useAppSelector((state) => state.toast.stack);

   return (
      <div className={styles.toast_handler_wrapper}>
         {toasters.map((toast) => renderToast({ ...toast }))}
      </div>
   );
};

const renderToast = ({ type, id, cancel, confirm, text }: ToastRenderType) => {
   switch (type) {
      case ToastTypes.WARNING:
         return (
            <ToastWarning
               key={id}
               text={text}
               cancel={cancel}
               confirm={confirm}
               id={id}
            />
         );
      case ToastTypes.ERROR:
         return <ToastError text={text} id={id} key={id} />;
      case ToastTypes.SUCCESS:
         return <ToastSuccess text={text} id={id} key={id} />;
      default:
         return <ToastInfo text={text} id={id} key={id} />;
   }
};
