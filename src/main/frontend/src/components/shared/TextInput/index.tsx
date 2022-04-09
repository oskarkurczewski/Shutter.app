import React from "react";
import "./style.scss";

interface TextInputProps {
   icon?: string;
   type?: React.HTMLInputTypeAttribute;
   className?: string;
   placeholder?: string;
   value: string;
   onChange: React.ChangeEventHandler<HTMLInputElement>;
}

const TextInput = ({
   icon,
   type,
   className,
   placeholder,
   value,
   onChange,
}: TextInputProps) => {
   return (
      <div className={`text-input-wrapper ${className ? className : ""}`}>
         {icon ? <span className="material-icons">{icon}</span> : null}
         <input
            type={type ? type : "text"}
            value={value}
            onChange={onChange}
            placeholder={placeholder}
         />
      </div>
   );
};

export default TextInput;
