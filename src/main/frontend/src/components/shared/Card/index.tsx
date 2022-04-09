import React from "react";
import "./style.scss";

interface CardProps {
   children: JSX.Element | JSX.Element[];
   className?: string;
}

const Card = ({ children, className }: CardProps) => {
   return <div className={`card-wrapper ${className ? className : ""}`}>{children}</div>;
};

export default Card;
