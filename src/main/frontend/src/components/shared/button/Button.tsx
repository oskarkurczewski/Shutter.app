import React from "react";
import styles from "./Button.module.scss";
import { Player } from "@lottiefiles/react-lottie-player";
import Animation from "assets/animations/loading.json";

interface Props {
   children: string;
   className?: string;
   icon?: string;
   onClick: (e: React.MouseEvent<HTMLButtonElement, MouseEvent>) => void;
   title?: string;
   disabled?: boolean;
   loading?: boolean;
}

export const Button: React.FC<Props> = ({
   children,
   className,
   icon,
   onClick,
   title,
   disabled,
   loading,
}) => {
   return (
      <div
         className={`${styles.button_wrapper} ${loading ? styles.loading : ""} ${
            className ? className : ""
         }`}
      >
         <button onClick={(e) => onClick(e)} title={title} disabled={disabled}>
            {icon ? <span className="material-icons">{icon}</span> : null}
            <p>{children}</p>
         </button>
         {loading && (
            <div className={styles.loading_wrapper}>
               <Player autoplay loop src={Animation} background="transparent" />
            </div>
         )}
      </div>
   );
};
