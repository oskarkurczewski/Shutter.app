import React, { FC, useRef, useState } from "react";
import { IoLanguage } from "react-icons/io5";
import { VscTriangleDown } from "react-icons/vsc";
import styles from "./IconDropdown.module.scss";

interface Option {
   [key: string]: string;
}

interface Props {
   options: Option;
   value: string;
   onChange: (key: string) => void;
   icon?: JSX.Element;
}

export const IconDropdown: FC<Props> = ({ options, value, onChange, icon }) => {
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
            {icon}
            <span className="label-bold">{options[value]}</span>
            <VscTriangleDown className={`${styles.arrow} ${active && styles.active}`} />
         </button>
         {active && (
            <div
               className={styles.option_list}
               style={{ top: selectedElement.current?.clientHeight + 7 }}
               tabIndex={-1}
            >
               {Object.entries(options).map(([key, value]) => {
                  return (
                     <button
                        className={styles.option}
                        key={key}
                        onClick={() => {
                           onChange(key);
                           setActive(false);
                        }}
                     >
                        <span className="label-bold">{value}</span>
                     </button>
                  );
               })}
            </div>
         )}
      </div>
   );
};
