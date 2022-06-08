import React from "react";
import { useAppSelector } from "redux/hooks";
import { Toast } from "components/layout";
import styles from "./ToastHandler.module.scss";

export const ToastHandler = () => {
   const toasters = useAppSelector((state) => state.toast.stack);

   return (
      <div className={styles.toast_handler_wrapper}>
         {toasters.map((t) => (
            <Toast key={t.name} {...t}></Toast>
         ))}
      </div>
   );
};
