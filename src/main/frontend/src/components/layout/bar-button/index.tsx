import React, { FC } from "react";
import { Link } from "react-router-dom";
import "./style.scss";

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
         className={`bar-button label-bold ${active && "bar-button-active"} ${
            expanded && "expanded"
         }`}
         style={style}
      >
         {icon}
         {expanded && <p>{text}</p>}
      </Link>
   );
};

export default BarButton;
