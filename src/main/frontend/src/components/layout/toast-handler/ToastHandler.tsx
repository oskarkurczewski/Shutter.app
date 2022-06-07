import React from "react";
import { useAppSelector } from "redux/hooks";
import Toast from "../toast/Toast";
import styles from "./ToastHandler.module.scss";

const ToastHandler = () => {
   const toasters = useAppSelector((state) => state.toast.stack);

   return (
      <div className={styles.toast_handler_wrapper}>
         {toasters.map((t) => (
            <Toast key={t.name} {...t}></Toast>
         ))}
      </div>
   );
};

export default ToastHandler;
