import React from "react";
import "./style.scss";

interface Props {
   className?: string;
}

const Card: React.FC<Props> = ({ children, className }) => {
   return <div className={`card-wrapper ${className ? className : ""}`}>{children}</div>;
};

export default Card;
