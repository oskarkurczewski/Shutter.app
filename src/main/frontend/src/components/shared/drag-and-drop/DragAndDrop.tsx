import React, { useState } from "react";
import styles from "./DragAndDrop.module.scss";

interface Props {
   label?: string;
   icon?: JSX.Element;
   onCapture: (file: File) => void;
}

export const DragAndDrop: React.FC<Props> = ({ label, icon, onCapture }) => {
   const [fileHover, setFileHover] = useState(false);

   return (
      <div
         className={`${styles.drag_and_drop_wrapper} ${
            fileHover ? styles.on_file_hover : ""
         }`}
      >
         <input
            type="file"
            onDragEnter={() => setFileHover(true)}
            onDragLeave={() => setFileHover(false)}
            onDrop={(e: React.DragEvent<HTMLInputElement>) => {
               const target = e.target as HTMLInputElement;
               onCapture(target.files[0]);
               setFileHover(false);
            }}
         />
         {icon && icon}
         {label && <p className="label-bold">{label}</p>}
      </div>
   );
};
