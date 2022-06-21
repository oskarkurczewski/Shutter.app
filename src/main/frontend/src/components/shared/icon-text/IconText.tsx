import React from "react";
import { IconType } from "react-icons/lib/cjs/iconBase";
import styles from "./IconText.module.scss";

interface Props {
   color?: "purple" | "red" | "blue" | "green";
   className?: string;
   text?: string;
   icon?: IconType;
   onClick?: () => void;
}

export const IconText: React.FC<Props> = ({ color, className, text, icon, onClick }) => {
   return (
      <div
         tabIndex={-1}
         role="button"
         onKeyDown={null}
         onClick={onClick}
         className={`${styles.text_wrapper} ${color && styles[color]} ${
            className ? className : ""
         }`}
      >
         <>
            {icon}
            {text && <p>{text}</p>}
         </>
      </div>
   );
};
