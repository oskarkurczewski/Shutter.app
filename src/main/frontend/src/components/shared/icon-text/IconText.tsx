import React from "react";
import { IconType } from "react-icons/lib/cjs/iconBase";
import styles from "./IconText.module.scss";

interface Props {
   color?: "purple" | "red" | "blue" | "green";
   className?: string;
   text?: string;
   Icon?: IconType;
}

export const IconText: React.FC<Props> = ({ color, className, text, Icon }) => {
   return (
      <div
         className={`${styles.text_wrapper} ${color && styles[color]} ${
            className ? className : ""
         }`}
      >
         {Icon && <Icon />}
         {text && <p>{text}</p>}
      </div>
   );
};
