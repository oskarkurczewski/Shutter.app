import React from "react";
import styles from "./PhotographerReviewsCardWrapper.module.scss";
import { Card, IconText } from "components/shared";

interface Props {
   specializationList?: string[];
   description?: string;
}

export const PhotographerReviewsCardWrapper: React.FC<Props> = () => {
   return (
      <div className={styles.photographer_reviews_card_wrapper}>
         <Card className={styles.data_wrapper}>
            <div className={styles.label_wrapper}>
               <div>
                  <p className="section-title">{"Recenzje"}</p>
               </div>
            </div>
         </Card>
      </div>
   );
};
