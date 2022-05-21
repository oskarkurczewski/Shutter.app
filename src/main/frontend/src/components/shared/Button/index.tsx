import React from "react";
import "./style.scss";

interface Props {
   children: string;
   className?: string;
   icon?: string;
   onClick: React.MouseEventHandler<HTMLDivElement> &
      React.KeyboardEventHandler<HTMLDivElement>;
}

const Button: React.FC<Props> = ({ children, className, icon, onClick }) => {
   return (
      <div
         className={`button-wrapper ${className ? className : ""}`}
         onClick={(e) => onClick(e)}
         onKeyDown={(e) => onClick(e)}
         tabIndex={0}
         role="button"
      >
         {icon ? <span className="material-icons">{icon}</span> : null}
         <p>{children}</p>
      </div>
   );
};

export default Button;
