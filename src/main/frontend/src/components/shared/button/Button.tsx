import React from "react";
import styles from "./Button.module.scss";

interface Props {
   children: string;
   className?: string;
   icon?: string;
   onClick: (e: React.MouseEvent<HTMLButtonElement, MouseEvent>) => void;
   title?: string;
   disabled?: boolean;
}

export const Button: React.FC<Props> = ({
   children,
   className,
   icon,
   onClick,
   title,
   disabled,
}) => {
   return (
      <button
         className={`${styles.button_wrapper} ${className ? className : ""}`}
         onClick={(e) => onClick(e)}
         title={title}
         disabled={disabled}
      >
         {icon ? <span className="material-icons">{icon}</span> : null}
         <p className="label-bold">{children}</p>
      </button>
   );
};
