import React from "react";
import styles from "./SquareButton.module.scss";

interface Props {
   children?: React.ReactNode | string | number;
   disabled?: boolean;
   onClick?: (event: any) => void;
   title?: string;
}

export const SquareButton: React.FC<Props> = ({
   children,
   disabled = false,
   onClick,
   title,
}) => {
   return (
      <div
         className={`${styles.square_button_wrapper} ${!onClick && styles.inactive}`}
         title={title}
      >
         <button disabled={disabled} onClick={onClick}>
            {children}
         </button>
      </div>
   );
};
