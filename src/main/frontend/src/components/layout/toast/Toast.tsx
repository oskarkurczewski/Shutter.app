import React, { useEffect, useRef } from "react";
import { useAppDispatch } from "redux/hooks";
import { remove } from "redux/slices/toastSlice";
import styles from "./Toast.module.scss";
import { ImCross } from "react-icons/im";
import { motion } from "framer-motion";

export interface ToastType {
   text: string;
   name?: string;
   icon?: JSX.Element;
   buttons?: JSX.Element;
   className?: string;
   id: number;
   permanent?: boolean;
}

export const Toast: React.FC<ToastType> = ({
   icon,
   text,
   buttons,
   className,
   id,
   permanent,
}) => {
   const dispatch = useAppDispatch();

   const timeout = useRef(null);

   useEffect(() => {
      if (!permanent) {
         timeout.current = setTimeout(() => {
            dispatch(remove(id));
         }, 10 * 1000);
      }
      return () => clearTimeout(timeout.current);
   }, []);

   const container = {
      rest: {
         opacity: [0, 1],
         transition: {
            duration: 1,
         },
      },
      exit: {
         x: "200%",
         transition: {
            duration: 0.5,
         },
      },
      hidden: {
         opacity: 0,
         transition: {
            duration: 0,
            delay: 0.4,
         },
      },
   };

   const progressBar = {
      hover: {
         width: "99%",
         transition: {
            ease: "easeOut",
            duration: 0.5,
         },
      },
      rest: {
         width: ["99%", "0%"],
         transition: {
            duration: 10,
         },
      },
   };

   return (
      <motion.div
         variants={container}
         whileHover="hover"
         initial="rest"
         animate="rest"
         exit={["exit", "hidden"]}
         layout
         className={`${styles.toast} ${className ? className : ""} `}
         tabIndex={-1}
         onMouseEnter={() => {
            clearTimeout(timeout.current);
         }}
         onMouseLeave={() => {
            if (!permanent) {
               timeout.current = setTimeout(() => {
                  dispatch(remove(id));
               }, 10 * 1000);
            }
         }}
      >
         <div className={styles.bar} />
         <div className={styles.container}>
            <div className={styles.wrapper}>
               <div className={styles.content}>
                  <div className={styles["icon-wrapper"]}>{icon}</div>
                  <p className="label-bold">{text}</p>
               </div>
               <div className={styles.buttons_wrapper}>{buttons}</div>
            </div>

            <button onClick={() => dispatch(remove(id))} className={styles.close_button}>
               {<ImCross />}
            </button>
         </div>
         <motion.div
            className={styles.progress_bar}
            variants={!permanent ? progressBar : {}}
         />
      </motion.div>
   );
};
