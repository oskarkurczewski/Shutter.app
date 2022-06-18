import React, { FC } from "react";
import styles from "./MenuDropdownItem.module.scss";

interface Props {
   onClick: () => void;
   value: string;
}

export const MenuDropdownItem: FC<Props> = ({ onClick, value }) => {
   return (
      <button
         className={styles.menu_dropdown_item}
         onClick={() => {
            onClick();
         }}
      >
         <span className="label-bold">{value}</span>
      </button>
   );
};
