import React, { FC, ReactNode, useRef, useState } from "react";
import { FaEllipsisH } from "react-icons/fa";
import { MenuDropdownItem } from "./menu-dropdown-item";
import styles from "./MenuDropdown.module.scss";

interface Props {
   children: ReactNode;
   className?: string;
}

export const MenuDropdown: FC<Props> = ({ children, className }) => {
   const [active, setActive] = useState(false);

   const selectedElement = useRef(null);

   return (
      <div
         className={`${styles.container}  ${className && className}`}
         onBlur={(e) => {
            if (
               !e.relatedTarget ||
               !e.currentTarget.childNodes[1]?.contains(e.relatedTarget)
            ) {
               setActive(false);
            }
         }}
      >
         <button
            className={styles.option_wrapper}
            onClick={() => {
               setActive(!active);
            }}
            ref={selectedElement}
         >
            <FaEllipsisH />
         </button>
         {active && (
            <div
               className={styles.option_list}
               style={{ top: selectedElement.current?.clientHeight + 7 }}
               tabIndex={-1}
            >
               {children}
            </div>
         )}
      </div>
   );
};
