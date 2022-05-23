import React from "react";
import { useAppSelector } from "redux/hooks";
import Toast from "../toast";
import "./style.scss";

const ToastHandler = () => {
   const toasters = useAppSelector((state) => state.toast.stack);

   return <div className="toast-handler-wrapper">{toasters.map((t) => t)}</div>;
};

export default ToastHandler;
