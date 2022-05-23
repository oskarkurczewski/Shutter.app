import React from "react";
import "./style.scss";

interface Props {
   onSubmit: React.FormEventHandler<HTMLFormElement>;
   children?: JSX.Element | JSX.Element[];
   isLoading?: boolean;
   className?: string;
}

const Form: React.FC<Props> = ({ onSubmit, children, isLoading, className }) => {
   return (
      <form onSubmit={onSubmit} className={`form-wrapper ${className && className}`}>
         {children}
         {isLoading && (
            <div className="loader-wrapper">
               <p>Loading...</p>
            </div>
         )}
      </form>
   );
};

export default Form;
