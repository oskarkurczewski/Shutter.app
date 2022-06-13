/* eslint-disable jsx-a11y/no-static-element-interactions */
/* eslint-disable jsx-a11y/click-events-have-key-events */
import React from "react";
import { useAppDispatch } from "redux/hooks";
import { remove } from "redux/slices/toastSlice";
import styles from "./Toast.module.scss";
import { ImCross } from "react-icons/im";

export interface ToastType {
   text: string;
   icon?: JSX.Element;
   buttons?: JSX.Element;
   className?: string;
   id: number;
}

export const Toast: React.FC<ToastType> = ({ icon, text, buttons, className, id }) => {
   const dispatch = useAppDispatch();

   return (
      <div className={`${styles.toast} ${className ? className : ""}`}>
         <div className={styles.bar} />
         <div className={styles.container}>
            <div className={styles.wrapper}>
               <div className={styles.content}>
                  {icon}
                  <p className="label-bold">{text}</p>
               </div>
               <div className={styles.buttons_wrapper}>{buttons}</div>
            </div>

            <button onClick={() => dispatch(remove(id))} className={styles.close_button}>
               {<ImCross />}
            </button>
         </div>
      </div>
   );
};
