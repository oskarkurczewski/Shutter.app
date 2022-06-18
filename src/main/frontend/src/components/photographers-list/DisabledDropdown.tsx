import React, { FC } from "react";
import { MdKeyboardArrowRight } from "react-icons/md";

interface DisabledDropdownProps {
   label: string;
   className: string;
}

export const DisabledDropdown: FC<DisabledDropdownProps> = ({ label, className }) => {
   return (
      <div className={className}>
         <p className="label">{label}</p>
         <MdKeyboardArrowRight />
      </div>
   );
};
