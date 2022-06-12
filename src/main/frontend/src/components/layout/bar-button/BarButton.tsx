import React, { FC } from "react";
import { Link } from "react-router-dom";
import styles from "./BarButton.module.scss";

interface Props {
   icon: JSX.Element;
   active?: boolean;
   to: string;
   text: string;
   style?: React.CSSProperties;
   expanded: boolean;
}

export const BarButton: FC<Props> = ({ to, icon, active, style, text, expanded }) => {
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
