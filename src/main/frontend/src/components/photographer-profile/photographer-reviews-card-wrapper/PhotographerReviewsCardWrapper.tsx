import React, { useState } from "react";
import styles from "./PhotographerReviewsCardWrapper.module.scss";
import { Button, Card, SquareButton } from "components/shared";
import { useTranslation } from "react-i18next";
import { FaArrowLeft, FaArrowRight } from "react-icons/fa";
import { PhotographerReview } from "../photographer-review";
import { getPhotographerReviewsRequest } from "redux/types/api";
import { useGetPhotographerReviewsQuery } from "redux/service/photographerManagementService";
interface Props {
   photographerLogin: string;
   reviewCount: number;
}

export const PhotographerReviewsCardWrapper: React.FC<Props> = ({
   photographerLogin,
   reviewCount,
}) => {
   const { t } = useTranslation();
   const [reviewPage, setReviewPage] = useState<number>(1);
   const { data } = useGetPhotographerReviewsQuery({
      pageNo: reviewPage,
      photographerLogin: photographerLogin,
   });

   //TODO: add review
   const addReview = () => {
      return;
   };

   const showPreviousReview = () => {
      if (reviewPage > 1) {
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
               {data && (
                  <PhotographerReview
                     id={data[0]?.id}
                     name={data[0]?.name}
                     surname={data[0]?.surname}
                     stars={data[0]?.score}
                     description={data[0]?.content}
                     likeCount={data[0]?.likeCount}
                  />
               )}
            </div>
         </Card>
      </div>
   );
};
