import React, { useState } from "react";
import styles from "./PhotographerReviewsCardWrapper.module.scss";
import { Button, Card, SquareButton } from "components/shared";
import { useTranslation } from "react-i18next";
import { PhotographerReview } from "../photographer-review";
import { useGetPhotographerReviewsQuery } from "redux/service/reviewService";
import AddReviewModal from "../add-review-modal/AddReviewModal";
import { MdKeyboardArrowLeft, MdKeyboardArrowRight } from "react-icons/md";
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
   const [reviewModalIsOpen, setReviewModalIsOpen] = useState<boolean>(false);
   const openReviewModal = () => setReviewModalIsOpen(true);
   const closeReviewModal = () => {
      setReviewModalIsOpen(false);
   };

   const addReview = () => {
      closeReviewModal();
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
      <>
         <Card className={styles.reviews_wrapper}>
            <div className={styles.header}>
               <p className="section-title">{t("photographer_page.reviews")}</p>
               <div className={styles.top_buttons_wrapper}>
                  <Button
                     className={styles.add_review_button}
                     onClick={openReviewModal}
                     icon="add_box"
                  >
                     {t("photographer_page.add_review")}
                  </Button>
                  <SquareButton onClick={showPreviousReview}>
                     <MdKeyboardArrowLeft />
                  </SquareButton>
                  <SquareButton onClick={showNextReview}>
                     <MdKeyboardArrowRight />
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
                  liked={data[0]?.liked}
               />
            )}
         </Card>
         <AddReviewModal
            isOpen={reviewModalIsOpen}
            photographerLogin={photographerLogin}
            onSubmit={addReview}
            onCancel={closeReviewModal}
         />
      </>
   );
};
