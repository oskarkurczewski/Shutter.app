import React, { useState } from "react";
import { MultiSelectDropdown } from ".";

export const ExampleMultiSelectDropdown = () => {
   const [selected, setSelected] = useState<string[]>([]);

   const options = {
      a: "aa",
      b: "bb",
      c: "cc",
   };

   return (
      <div>
         <p>
            {selected.map((v) => (
               <span key={v}>{v}</span>
            ))}
         </p>
         <MultiSelectDropdown
            selected={selected}
            options={options}
            label="witam"
            onChange={setSelected}
         />
      </div>
   );
};
