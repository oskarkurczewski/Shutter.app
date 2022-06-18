import React, { FC } from "react";
import { IconDropdown } from "../icon-dropdown/IconDropdown";

interface Option {
   [key: string]: string;
}

interface Props {
   options: Option;
   label: string;
   onChange: (selected: string[]) => void;
   icon?: JSX.Element;
   multi?: boolean;
   selected: string[];
}

export const MultiSelectDropdown: FC<Props> = ({
   options,
   label,
   icon,
   selected,
   onChange,
}) => {
   return (
      <IconDropdown
         options={options}
         value={label}
         selected={selected}
         multi
         icon={icon}
         onChange={(key) => {
            if (selected.includes(key)) {
               onChange(selected.filter((v) => v !== key));
            } else {
               onChange([...selected, key]);
            }
         }}
      />
   );
};
