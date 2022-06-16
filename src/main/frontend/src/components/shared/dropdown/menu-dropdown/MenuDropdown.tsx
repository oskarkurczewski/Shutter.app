import React, { FC, useRef, useState } from "react";
import { FaEllipsisH } from "react-icons/fa";
import styles from "./MenuDropdown.module.scss";

interface Props {
   children: any | any[];
}

export const MenuDropdown: FC<Props> = ({ children }) => {
   const [active, setActive] = useState(false);

   const selectedElement = useRef(null);

   return (
      <div
         className={styles.container}
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
