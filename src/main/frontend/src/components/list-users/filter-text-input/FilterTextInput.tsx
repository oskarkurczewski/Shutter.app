import Checkbox from "components/shared/checkbox/Checkbox";
import TextInput from "components/shared/text-input/TextInput";
import React from "react";

interface TextInputProps {
   label: string;
   isActive: boolean;
   setIsActive: (value: boolean) => void;
   value: string;
   setValue: (value: string) => void;
}

const FilterTextInput: React.FC<TextInputProps> = ({
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

export default FilterTextInput;
