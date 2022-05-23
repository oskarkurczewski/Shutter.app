import React from "react";
import "./style.scss";

interface Props {
   children: string;
   className?: string;
   icon?: string;
   onClick: React.MouseEventHandler<HTMLDivElement> &
      React.KeyboardEventHandler<HTMLDivElement>;
   title?: string;
}

const Button: React.FC<Props> = ({ children, className, icon, onClick, title }) => {
   return (
      <div
         className={`button-wrapper ${className ? className : ""}`}
         onClick={(e) => onClick(e)}
         onKeyDown={(e) => onClick(e)}
         tabIndex={0}
         role="button"
         title={title}
      >
         {icon ? <span className="material-icons">{icon}</span> : null}
         <p>{children}</p>
      </div>
   );
};

export default Button;
