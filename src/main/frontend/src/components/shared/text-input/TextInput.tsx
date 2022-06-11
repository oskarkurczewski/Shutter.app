import React from "react";
import styles from "./TextInput.module.scss";

interface TextInputProps {
   icon?: string;
   type?: React.HTMLInputTypeAttribute;
   label?: string;
   className?: string;
   placeholder?: string;
   required?: boolean;
   value: string;
   onChange: React.ChangeEventHandler<HTMLInputElement>;
   name?: string;
   disabled?: boolean;
   validation?: number | boolean;
   validationMessages?: string[];
}

export const TextInput = ({
   icon,
   type,
   label,
   className,
   placeholder,
   required,
   value,
   onChange,
   name,
   disabled,
   validation,
   validationMessages,
}: TextInputProps) => {
   return (
      <div className={`${styles.text_input_wrapper} ${className ? className : ""}`}>
         {label && <p className={`label ${required && styles.required}`}>{label}</p>}
         <div className={styles.field}>
            {icon && <span className="material-icons">{icon}</span>}
            <input
               type={type ? type : "text"}
               value={value}
               onChange={onChange}
               placeholder={placeholder}
               name={name}
               disabled={disabled}
               className={`${
                  typeof validation === "number"
                     ? validation !== undefined && validation !== null && styles.invalid
                     : validation === false && styles.invalid
               }`}
            />
         </div>
         <div className={styles.messages}>
            {typeof validation === "number" ? (
               <p>{validationMessages[validation]}</p>
            ) : (
               validation === false && <p>{validationMessages[0]}</p>
            )}
         </div>
      </div>
   );
};
