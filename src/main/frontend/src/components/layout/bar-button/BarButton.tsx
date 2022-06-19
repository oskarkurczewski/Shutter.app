import React, { FC } from "react";
import { Link } from "react-router-dom";
import styles from "./BarButton.module.scss";

type Props =
   | {
        icon: JSX.Element;
        active?: boolean;
        to?: string;
        onClick?: never;
        text: string;
        style?: React.CSSProperties;
        expanded: boolean;
     }
   | {
        icon: JSX.Element;
        active?: boolean;
        to?: never;
        onClick?: () => void;
        text: string;
        style?: React.CSSProperties;
        expanded: boolean;
     };

export const BarButton: FC<Props> = ({
   to,
   icon,
   active,
   style,
   text,
   expanded,
   onClick,
}) => {
   if (onClick) {
      return (
         <button
            onClick={onClick}
            className={`${styles.bar_button} label-bold ${
               active ? styles.bar_button_active : ""
            } ${expanded ? styles.expanded : ""}`}
            style={style}
         >
            {icon}
            {<p className={expanded ? "" : styles.hide_text}>{text}</p>}
         </button>
      );
   }

   return (
      <Link
         to={to}
         className={`${styles.bar_button} label-bold ${
            active ? styles.bar_button_active : ""
         } ${expanded ? styles.expanded : ""}`}
         style={style}
      >
         {icon}
         {<p className={expanded ? "" : styles.hide_text}>{text}</p>}
      </Link>
   );
};
