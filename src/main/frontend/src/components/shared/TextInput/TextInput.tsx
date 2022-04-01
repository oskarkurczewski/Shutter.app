import React from "react";
import "./style.scss";

interface TextInputProps {
   icon?: string;
   type?: React.HTMLInputTypeAttribute;
   classes?: string;
   placeholder?: string;
   value: string;
   onChange: React.ChangeEventHandler<HTMLInputElement>;
}

const TextInput = ({
   icon,
   type,
   classes,
   placeholder,
   value,
   onChange,
}: TextInputProps) => {
   return (
      <div className={`text-input-wrapper ${classes ? classes : ""}`}>
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
