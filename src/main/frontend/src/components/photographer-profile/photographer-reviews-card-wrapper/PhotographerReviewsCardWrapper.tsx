import React, { useState } from "react";
import styles from "./PhotographerReviewsCardWrapper.module.scss";
import { Button, Card, SquareButton } from "components/shared";
import { useTranslation } from "react-i18next";
import { FaArrowLeft, FaArrowRight } from "react-icons/fa";
import { PhotographerStars } from "../photographer-stars";
import { PhotographerReview } from "../photographer-review";
interface Props {
   review?: any;
   reviewCount?: number;
}

export const PhotographerReviewsCardWrapper: React.FC<Props> = ({
   review,
   reviewCount,
}) => {
   const { t } = useTranslation();
   const [reviewPage, setReviewPage] = useState<number>(0);

   //TODO: add new review using modal
   const addReview = () => {
      return;
   };

   const showPreviousReview = () => {
      if (reviewPage > 0) {
         setReviewPage(reviewPage - 1);
      }
   };

   const showNextReview = () => {
      if (reviewPage < reviewCount) {
         setReviewPage(reviewPage + 1);
      }
   };

   return (
      <div className={styles.photographer_reviews_card_wrapper}>
         <Card className={styles.data_wrapper}>
            <div className={styles.reviews_wrapper}>
               <div className={styles.top_wrapper}>
                  <p className="section-title">{t("photographer_page.reviews")}</p>
                  <div className={styles.top_buttons_wrapper}>
                     <Button onClick={addReview} icon="add_box">
                        {t("photographer_page.add_review")}
                     </Button>
                     <SquareButton onClick={showPreviousReview}>
                        <FaArrowLeft />
                     </SquareButton>
                     <SquareButton onClick={showNextReview}>
                        <FaArrowRight />
                     </SquareButton>
                  </div>
               </div>
               <PhotographerReview
                  id={1}
                  name={review?.name}
                  surname={review?.surname}
                  stars={review?.stars}
                  description={review?.description}
               />
            </div>
         </Card>
      </div>
   );
};
