/* eslint-disable jsx-a11y/no-static-element-interactions */
/* eslint-disable jsx-a11y/click-events-have-key-events */
import Button from "components/shared/Button";
import React, { FC } from "react";
import { useAppDispatch } from "redux/hooks";
import { remove } from "redux/slices/toastSlice";
import "./style.scss";

export interface ToastType {
   name: string;
   label: string;
   text: string;
   content?: JSX.Element;
}

const Toast: FC<ToastType> = ({ label, name, text, content }) => {
   const dispatch = useAppDispatch();

   return (
      <div className="toast">
         <p className="label-bold">{label}</p>
         <p>{text}</p>
         {content}

         <div className="close-btn-wrapper" onClick={() => dispatch(remove(name))}>
            <span className="close-btn" />
         </div>
      </div>
   );
};

export default Toast;
