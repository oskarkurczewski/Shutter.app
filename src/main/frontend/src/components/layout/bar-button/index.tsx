import React, { FC } from "react";
import { Link } from "react-router-dom";
import styles from "./style.module.scss";

interface Props {
   icon: JSX.Element;
   active?: boolean;
   to: string;
   text: string;
   style?: React.CSSProperties;
   expanded: boolean;
}

const BarButton: FC<Props> = ({ to, icon, active, style, text, expanded }) => {
   return (
      <Link
         to={to}
         className={`${styles.bar_button} label-bold ${
            active && styles.bar_button_active
         } ${expanded && styles.expanded}`}
         style={style}
      >
         {icon}
         {expanded && <p>{text}</p>}
      </Link>
   );
};

export default BarButton;
