import React from "react";
import styles from "./ExpandableCard.module.scss";
import { Card } from "../Card";
import { SquareButton } from "components/shared/square-button";
import { MdKeyboardArrowDown, MdKeyboardArrowUp } from "react-icons/md";

interface Props {
   isOpen: boolean;
   setIsOpen: (open: boolean) => void;
   className?: string;
   children: JSX.Element | JSX.Element[];
}

export const ExpandableCard: React.FC<Props> = ({
   children,
   isOpen,
   className,
   setIsOpen,
}) => {
   return (
      <Card
         className={`${styles.expandable_card_wrapper} ${!isOpen ? styles.closed : ""} ${
            className ? className : ""
         }`}
      >
         <>
            {children}
            <div
               className={styles.button}
               onClick={() => setIsOpen(!isOpen)}
               onKeyDown={() => setIsOpen(!isOpen)}
               tabIndex={-1}
               role="button"
            >
               {isOpen ? <MdKeyboardArrowUp /> : <MdKeyboardArrowDown />}
            </div>
         </>
      </Card>
   );
};
