import React from "react";
import "./style.scss";

interface Props {
   children: string;
   className?: string;
   icon?: string;
   onClick: (e: React.MouseEvent<HTMLButtonElement, MouseEvent>) => void;
   title?: string;
   disabled?: boolean;
}

const Button: React.FC<Props> = ({
   children,
   className,
   icon,
   onClick,
   title,
   disabled,
}) => {
   return (
      <button
         className={`button-wrapper ${className ? className : ""}`}
         onClick={(e) => onClick(e)}
         title={title}
         disabled={disabled}
      >
         {icon ? <span className="material-icons">{icon}</span> : null}
         <p>{children}</p>
      </button>
   );
};

export default Button;
