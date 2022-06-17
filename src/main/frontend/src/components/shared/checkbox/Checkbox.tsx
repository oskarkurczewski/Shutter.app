import React from "react";
import styles from "./Checkbox.module.scss";

interface Props {
   id: string;
   children?: string;
   className?: string;
   required?: boolean;
   value: boolean;
   onChange: React.ChangeEventHandler<HTMLInputElement>;
   disabled?: boolean;
}

export const Checkbox: React.FC<Props> = ({
   className,
   children,
   required,
   value,
   onChange,
   disabled,
   id,
}) => {
   return (
      <div className={`${styles.checkbox_wrapper} ${className ? className : ""}`}>
         <input
            id={id}
            type="checkbox"
            checked={value}
            onChange={onChange}
            disabled={disabled}
         />
         <label htmlFor={id} className={required && styles.required}>
            {children}
         </label>
      </div>
   );
};
