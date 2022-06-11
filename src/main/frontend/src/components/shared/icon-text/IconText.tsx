import React from "react";
import { IconType } from "react-icons/lib/cjs/iconBase";
import styles from "./IconText.module.scss";

interface Props {
   textStyle?: string;
   className?: string;
   text?: string;
   Icon?: IconType;
}

export const IconText: React.FC<Props> = ({ className, textStyle, text, Icon }) => {
   return (
      <div className={`${styles.text_wrapper} ${className ? className : ""}`}>
         {Icon && <Icon />}
         {text && <p className={`${textStyle ? textStyle : ""}`}>{text}</p>}
      </div>
   );
};
