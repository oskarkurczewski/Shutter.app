import React from "react";
import { Checkbox, TextInput } from "components/shared";

interface TextInputProps {
   label: string;
   isActive: boolean;
   setIsActive: (value: boolean) => void;
   value: string;
   setValue: (value: string) => void;
}

export const FilterTextInput: React.FC<TextInputProps> = ({
   label,
   isActive,
   setIsActive,
   value,
   setValue,
}) => {
   return (
      <div className={`filter text ${label}`}>
         <Checkbox
            onChange={(e) => {
               setIsActive(e.target.checked);
            }}
            value={isActive}
         />

         <TextInput
            value={value}
            onChange={(e) => {
               setValue(e.target.value);
            }}
            label={label}
         />
      </div>
   );
};
