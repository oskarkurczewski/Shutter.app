import React from "react";
import styles from "./ValidationBox.module.scss";
import { Card } from "components/shared";

type Validator = {
   label: string;
   valid: boolean | null;
};

interface Props {
   data: Validator[][];
   className?: string;
}

export const ValidationBox: React.FC<Props> = ({ data, className }) => {
   return (
      <Card className={`${styles.validation_box_wrapper} ${className ? className : ""}`}>
         {data.map((section, index) => (
            <div className={styles.section} key={`section-${index}`}>
               {section.map((validator, validatorIndex) => (
                  <div
                     className={`${styles.validator} ${
                        validator.valid !== null &&
                        (!validator.valid ? styles.invalid : styles.valid)
                     }`}
                     key={`validator-${index}-${validatorIndex}`}
                  >
                     <div className={styles.dot} />
                     <p className="label">{validator.label}</p>
                  </div>
               ))}
            </div>
         ))}
      </Card>
   );
};
