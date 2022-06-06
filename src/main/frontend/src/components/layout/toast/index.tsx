/* eslint-disable jsx-a11y/no-static-element-interactions */
/* eslint-disable jsx-a11y/click-events-have-key-events */
import Button from "components/shared/button";
import React, { FC } from "react";
import { useAppDispatch } from "redux/hooks";
import { remove } from "redux/slices/toastSlice";
import styles from "./style.module.scss";

export interface ToastType {
   name: string;
   label: string;
   text: string;
   content?: JSX.Element;
}

const Toast: FC<ToastType> = ({ label, name, text, content }) => {
   const dispatch = useAppDispatch();

   return (
      <div className={styles.toast}>
         <p className="label-bold">{label}</p>
         <p>{text}</p>
         {content}

         <div className={styles.close_btn_wrapper} onClick={() => dispatch(remove(name))}>
            <span className={styles.close_btn} />
         </div>
      </div>
   );
};

export default Toast;
