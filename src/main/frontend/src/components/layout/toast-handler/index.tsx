import React from "react";
import { useAppSelector } from "redux/hooks";
import Toast from "../toast";
import "./style.scss";

const ToastHandler = () => {
   const toasters = useAppSelector((state) => state.toast.stack);

   return (
      <div className="toast-handler-wrapper">
         {toasters.map((t) => (
            <Toast key={t.name} {...t}></Toast>
         ))}
      </div>
   );
};

export default ToastHandler;
