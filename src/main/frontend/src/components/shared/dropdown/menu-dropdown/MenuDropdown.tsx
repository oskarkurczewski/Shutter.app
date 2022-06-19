import React, { FC, ReactElement, useRef, useState } from "react";
import { FaEllipsisH } from "react-icons/fa";
import styles from "./MenuDropdown.module.scss";
import { MenuDropdownItem } from "./menu-dropdown-item";

interface Props {
   children:
      | ReactElement<typeof MenuDropdownItem>
      | ReactElement<typeof MenuDropdownItem>[];
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
