import React from "react";
import { IconType } from "react-icons/lib/cjs/iconBase";
import styles from "./IconText.module.scss";

interface Props {
   className?: string;
   text?: string;
   Icon?: IconType;
}

export const IconText: React.FC<Props> = ({ className, text, Icon }) => {
   return (
      <p className={`${styles.text_wrapper} ${className ? className : ""}`}>
         {Icon && <Icon />}
         {text && <p>{text}</p>}
      </p>
   );
};
