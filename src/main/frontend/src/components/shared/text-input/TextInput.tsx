import React, { useEffect, useRef } from "react";
import styles from "./TextInput.module.scss";

interface TextInputProps {
   icon?: JSX.Element;
   type?: React.HTMLInputTypeAttribute;
   label?: string;
   className?: string;
   placeholder?: string;
   required?: boolean;
   value: string;
   onChange: React.ChangeEventHandler<HTMLInputElement>;
   name?: string;
   disabled?: boolean;
   validation?: string;
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
   validation = "",
}: TextInputProps) => {
   const input = useRef<HTMLInputElement>(null);

   return (
      <div className={`${styles.text_input_wrapper} ${className ? className : ""}`}>
         {label && <p className={`label ${required ? styles.required : ""}`}>{label}</p>}
         <div className={`${styles.field} ${validation !== "" ? styles.invalid : ""}`}>
            <input
               type={type ? type : "text"}
               value={value}
               onChange={onChange}
               placeholder={placeholder}
               name={name}
               disabled={disabled}
               ref={input}
            />
            {icon}
         </div>
         <div className={styles.messages} title={validation}>
            <p style={{ width: `${input?.current?.getBoundingClientRect().width}px` }}>
               {validation}
            </p>
         </div>
      </div>
   );
};
