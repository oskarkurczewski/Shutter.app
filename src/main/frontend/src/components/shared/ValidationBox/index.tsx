import React from "react";
import Card from "../Card";
import "./style.scss";

type Validator = {
   label: string;
   valid: boolean | null;
};

interface Props {
   data: Validator[][];
}

const ValidationBox: React.FC<Props> = ({ data }) => {
   return (
      <Card className="validation-box-wrapper">
         {data.map((section, index) => (
            <div className="section" key={`section-${index}`}>
               {section.map((validator, validatorIndex) => (
                  <div
                     className={`validator ${
                        validator.valid !== null &&
                        (!validator.valid ? "invalid" : "valid")
                     }`}
                     key={`validator-${index}-${validatorIndex}`}
                  >
                     <div className="dot" />
                     <p className="label">{validator.label}</p>
                  </div>
               ))}
            </div>
         ))}
      </Card>
   );
};

export default ValidationBox;
