import React from "react";
import "./style.scss";

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
      <div className={`text-input-wrapper ${className ? className : ""}`}>
         {label && <p className={`label ${required && "required"}`}>{label}</p>}
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
