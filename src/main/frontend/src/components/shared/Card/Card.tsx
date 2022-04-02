import React from "react";
import "./style.scss";

interface CardProps {
   children: JSX.Element | JSX.Element[];
   classes?: string;
}

const Card = ({ children, classes }: CardProps) => {
   return <div className={`card-wrapper ${classes ? classes : ""}`}>{children}</div>;
};

export default Card;
