import React, { useEffect, useRef, useState } from "react";
import { getNthParent } from "util/domUtils";
import styles from "./InfoBox.module.scss";

interface Props {
   children: JSX.Element | JSX.Element[];
   className?: string;
}

export const InfoBox: React.FC<Props> = ({ children, className }) => {
   const boxElement = useRef(null);

   const [boxStyle, setBoxStyle] = useState<{
      left?: number | string;
      right?: number | string;
      visibility?: "visible";
   }>({});

   useEffect(() => {
      if (boxElement.current) {
         const column = getNthParent(boxElement.current, 3);
         const table = getNthParent(column, 4);

         // console.log(column, table, table.offsetWidth - column.offsetLeft);

         table.offsetWidth - column.offsetLeft < 300
            ? setBoxStyle({ right: "110%", visibility: "visible" })
            : setBoxStyle({ left: "110%", visibility: "visible" });
      }
   }, [boxElement]);

   return (
      <div
         className={`${styles.info_box_wrapper} ${className ? className : ""}`}
         ref={boxElement}
         style={boxStyle}
      >
         {children}
      </div>
   );
};
