import React from "react";
import styles from "./Dropdown.module.scss";

interface Props {
   values: string[] | number[] | boolean[];
   onChange?: React.ChangeEventHandler<HTMLSelectElement>;
   selectedValue: string | number | boolean;
   name?: string;
   id?: string;
   children?: string;
   className?: string;
}

export const Dropdown: React.FC<Props> = ({
   children,
   values,
   onChange,
   name,
   id,
   selectedValue,
   className,
}) => {
   return (
      <div className={`${styles.dropdown_wrapper} ${className ? className : ""}`}>
         {children && <p className="label">{children}</p>}
         <select
            name={name}
            id={id}
            onChange={(e) => {
               onChange(e);
            }}
            value={selectedValue.toString()}
         >
            {values.map((value, index) => {
               return (
                  <option key={index} value={value}>
                     {value}
                  </option>
               );
            })}
         </select>
      </div>
   );
};
