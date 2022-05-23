import React from "react";
import "./style.scss";

interface Props {
   children?: string;
   className?: string;
   value: boolean;
   onChange: React.ChangeEventHandler<HTMLInputElement>;
}

const Checkbox: React.FC<Props> = ({ className, children, value, onChange }) => {
   return (
      <div className={`checkbox-wrapper ${className ? className : ""}`}>
         <input type="checkbox" checked={value} onChange={onChange} />
         <p className="label">{children}</p>
      </div>
   );
};

export default Checkbox;
