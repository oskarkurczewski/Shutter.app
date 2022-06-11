import React from "react";
import styles from "./PhotographerDescription.module.scss";
import { Card, IconText } from "components/shared";

interface Props {
   specializationList?: string[];
   description?: string;
}

export const PhotographerDescription: React.FC<Props> = ({
   specializationList,
   description,
}) => {
   const specElement = (spec: string) => {
      return (
         <IconText
            text={`.${spec}`}
            className={styles.text_wrapper}
            textStyle={styles.text_style}
         />
      );
   };

   const specList = (specList: string[]) => {
      const result = [];
      for (let i = 0; i < specList.length; i++) {
         result.push(<li key={i}>{specElement(specList[i])}</li>);
      }
      return result;
   };

   return (
      <div className={styles.photographer_description_wrapper}>
         <Card className={styles.data_wrapper}>
            <div className={styles.label_wrapper}>
               <div>
                  <p className="section-title">{"specializations"}</p>
                  <ul>{specList(specializationList)}</ul>
               </div>
               <div>
                  <p className="section-title">{"description"}</p>
                  <p>{description}</p>
               </div>
            </div>
         </Card>
      </div>
   );
};
