import React from "react";
import styles from "./Checkbox.module.scss";

interface Props {
   children?: string;
   className?: string;
   required?: boolean;
   value: boolean;
   onChange: React.ChangeEventHandler<HTMLInputElement>;
}

export const Checkbox: React.FC<Props> = ({
   className,
   children,
   required,
   value,
   onChange,
}) => {
   return (
      <div className={`${styles.checkbox_wrapper} ${className ? className : ""}`}>
         <input type="checkbox" checked={value} onChange={onChange} />
         <p className={`label ${required && "required"}`}>{children}</p>
      </div>
   );
};
