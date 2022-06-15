import React from "react";
import styles from "./PhotographerReviewsCardWrapper.module.scss";
import { Card } from "components/shared";
import { useTranslation } from "react-i18next";

interface Props {
   specializationList?: string[];
   description?: string;
}

export const PhotographerReviewsCardWrapper: React.FC<Props> = () => {
   const { t } = useTranslation();
   return (
      <div className={styles.photographer_reviews_card_wrapper}>
         <Card className={styles.data_wrapper}>
            <div className={styles.label_wrapper}>
               <div>
                  <p className="section-title">{t("photographer_page.reviews")}</p>
               </div>
            </div>
         </Card>
      </div>
   );
};
