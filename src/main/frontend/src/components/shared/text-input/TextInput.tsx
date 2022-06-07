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
}

const TextInput = ({
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
}: TextInputProps) => {
   return (
      <div className={`${styles.text_input_wrapper} ${className ? className : ""}`}>
         {label && <p className={`label ${required && styles.required}`}>{label}</p>}
         <div>
            {icon && <span className="material-icons">{icon}</span>}
            <input
               type={type ? type : "text"}
               value={value}
               onChange={onChange}
               placeholder={placeholder}
               name={name}
               disabled={disabled}
            />
         </div>
      </div>
   );
};

export default TextInput;
