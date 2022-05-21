import React from "react";
import "./style.scss";

interface TextInputProps {
   icon?: string;
   type?: React.HTMLInputTypeAttribute;
   label?: string;
   className?: string;
   placeholder?: string;
   value: string;
   onChange: React.ChangeEventHandler<HTMLInputElement>;
}

const TextInput = ({
   icon,
   type,
   label,
   className,
   placeholder,
   value,
   onChange,
}: TextInputProps) => {
   return (
      <div className={`text-input-wrapper ${className ? className : ""}`}>
         {label && <p className="label">{label}</p>}
         <div>
            {icon && <span className="material-icons">{icon}</span>}
            <input
               type={type ? type : "text"}
               value={value}
               onChange={onChange}
               placeholder={placeholder}
            />
         </div>
      </div>
   );
};

export default TextInput;
