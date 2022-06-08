import React from "react";
import { Checkbox, Dropdown } from "components/shared";

interface DropdownInputProps {
   label: string;
   isActive: boolean;
   setIsActive: (value: boolean) => void;
   value: string;
   setValue: (value: string) => void;
   possibleValues: string[];
}

export const FilterDropdownInput: React.FC<DropdownInputProps> = ({
   label,
   isActive,
   setIsActive,
   value,
   setValue,
   possibleValues,
}) => {
   return (
      <div className={`filter dropdown ${label}`}>
         <Checkbox
            onChange={(e) => {
               setIsActive(e.target.checked);
            }}
            value={isActive}
         />
         <Dropdown
            values={possibleValues}
            selectedValue={value}
            onChange={(e) => {
               setValue(e.target.value);
            }}
            name={label}
            id={label}
         >
            {label}
         </Dropdown>
      </div>
   );
};
