import React, { useEffect, useState } from "react";
import styles from "./PhotographerReviewsCardWrapper.module.scss";
import { Button, Card, Loader, SquareButton } from "components/shared";
import { useTranslation } from "react-i18next";
import { PhotographerReview } from "../photographer-review";
import { useGetPhotographerReviewsQuery } from "redux/service/reviewService";
import AddReviewModal from "../add-review-modal/AddReviewModal";
import { MdKeyboardArrowLeft, MdKeyboardArrowRight } from "react-icons/md";
// import { GetPhotographerReviewsResponse } from "redux/types/api";
interface Props {
   photographerLogin: string;
   reviewCount: number;
}

export const PhotographerReviewsCardWrapper: React.FC<Props> = ({
   photographerLogin,
}) => {
   const { t } = useTranslation();
   const [reviewPage, setReviewPage] = useState<number>(1);
   const [reviewModalIsOpen, setReviewModalIsOpen] = useState<boolean>(false);
   const openReviewModal = () => setReviewModalIsOpen(true);

   const { data, isLoading } = useGetPhotographerReviewsQuery({
      pageNo: reviewPage,
      recordsPerPage: 1,
      photographerLogin,
   });

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
      if (reviewPage < data?.allPages) {
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
                  <SquareButton
                     className={data?.pageNo == 1 ? styles.disabled : ""}
                     disabled={data?.pageNo == 1}
                     onClick={showPreviousReview}
                  >
                     <MdKeyboardArrowLeft />
                  </SquareButton>
                  <SquareButton
                     className={data?.pageNo == data?.allPages ? styles.disabled : ""}
                     disabled={data?.pageNo == data?.allPages}
                     onClick={showNextReview}
                  >
                     <MdKeyboardArrowRight />
                  </SquareButton>
               </div>
            </div>
            <>
               {isLoading ? (
                  <div className={styles.loader_wrapper}>
                     <Loader />
                  </div>
               ) : (
                  data &&
                  data?.list?.map((review, index) => (
                     <PhotographerReview
                        key={index}
                        id={review.id}
                        name={review.name}
                        surname={review.surname}
                        authorLogin={review.authorLogin}
                        email={review.email}
                        stars={review.score}
                        description={review.content}
                        likeCount={review.likeCount}
                        liked={review.liked}
                     />
                  ))
               )}
            </>
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
