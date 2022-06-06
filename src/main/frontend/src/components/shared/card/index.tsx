import React from "react";
import "./style.scss";

interface Props {
   id?: string;
   className?: string;
   children: JSX.Element | JSX.Element[];
}

const Card: React.FC<Props> = ({ id, children, className }) => {
   return (
      <div id={id} className={`card-wrapper ${className ? className : ""}`}>
         {children}
      </div>
   );
};

export default Card;
