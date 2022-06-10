import React from "react";
import styles from "./CalendarHeader.module.scss";
import { SquareButton } from "components/shared";
import { FaChevronLeft, FaChevronRight } from "react-icons/fa";
import { MdEvent } from "react-icons/md";

interface Props {
   title: string;
   changeWeek: (diff: 1 | -1) => void;
   weekLabel: string;
}

export const CalendarHeader: React.FC<Props> = ({ title, changeWeek, weekLabel }) => {
   return (
      <div className={styles.header_wrapper}>
         <p className="section-title">{title}</p>
         <div className={styles.nav}>
            <SquareButton onClick={() => changeWeek(-1)}>
               <FaChevronLeft />
            </SquareButton>
            <SquareButton className={styles.week_range} disabled>
               <MdEvent />
               <span>{weekLabel}</span>
            </SquareButton>
            <SquareButton onClick={() => changeWeek(1)}>
               <FaChevronRight />
            </SquareButton>
         </div>
      </div>
   );
};
