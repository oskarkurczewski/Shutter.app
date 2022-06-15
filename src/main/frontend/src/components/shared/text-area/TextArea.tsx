import React from "react";
import styles from "./TextArea.module.scss";

interface TextAreaProps {
   label?: string;
   className?: string;
   placeholder?: string;
   required?: boolean;
   value: string;
   onChange: React.ChangeEventHandler<HTMLTextAreaElement>;
   name?: string;
   disabled?: boolean;
   rows?: number;
}

export const TextArea = ({
   label,
   className,
   placeholder,
   required,
   value,
   onChange,
   name,
   disabled,
   rows
}: TextAreaProps) => {
   return (
      <div className={`${styles.text_area_wrapper} ${className ? className : ""}`}>
         {label && <p className={`label ${required ? styles.required : ""}`}>{label}</p>}
         <div>
            <textarea
               value={value}
               onChange={onChange}
               placeholder={placeholder}
               name={name}
               disabled={disabled}
               rows={rows}
            />
         </div>
      </div>
   );
};
