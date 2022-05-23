import React from "react";
import "./style.scss";

interface Props {
   children?: React.ReactNode | string | number;
   disabled?: boolean;
   onClick?: (event: any) => void;
   title?: string;
}

const SquareButton: React.FC<Props> = ({
   children,
   disabled = false,
   onClick,
   title,
}) => {
   return (
      <div className={`square-button-wrapper ${!onClick && "inactive"}`} title={title}>
         <button disabled={disabled} onClick={onClick}>
            {children}
         </button>
      </div>
   );
};

export default SquareButton;
