import React, { FC, useRef, useState } from "react";
import { MdKeyboardArrowRight } from "react-icons/md";
import styles from "./IconDropdown.module.scss";

interface Option {
   [key: string]: string;
}

interface Props {
   options: Option;
   value: string;
   onChange: (key: string) => void;
   icon?: JSX.Element;
   multi?: boolean;
   selected?: string[];
   className?: string;
}

export const IconDropdown: FC<Props> = ({
   options,
   value,
   onChange,
   icon,
   multi,
   selected,
   className = "",
}) => {
   const [active, setActive] = useState(false);

   const selectedElement = useRef(null);

   return (
      <div
         className={`${styles.container} ${className}`}
         onBlur={(e) => {
            if (
               !e.relatedTarget ||
               (!e.currentTarget.childNodes[1]?.contains(e.relatedTarget) &&
                  !e.currentTarget.contains(e.relatedTarget))
            ) {
               setActive(false);
            }
         }}
      >
         <button
            className={`${styles.option_wrapper} ${active ? styles.active : ""}`}
            onClick={() => {
               setActive(!active);
            }}
            ref={selectedElement}
         >
            {icon}
            <span className="label-bold">{options[value] ? options[value] : value}</span>
            <MdKeyboardArrowRight className={active ? styles.expand : ""} />
         </button>
         {active && (
            <div
               className={styles.option_list}
               style={{
                  top: selectedElement.current?.clientHeight + 7,
                  minWidth: selectedElement.current?.clientWidth,
               }}
               tabIndex={-1}
            >
               {Object.entries(options).map(([key, value]) => {
                  return (
                     <button
                        className={`${styles.option} ${
                           selected && selected.includes(key) ? styles.selected : ""
                        }`}
                        key={key}
                        onClick={() => {
                           onChange(key);
                           if (!multi) setActive(false);
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
